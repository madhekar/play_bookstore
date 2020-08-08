package models

import javax.inject.Inject
import anorm.SqlParser.{ get, str, int , scalar}
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Int, total: Int) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

case class Book(id: Option[Int] = None,  title: String, price: Int, author: String, userId: Option[Int])

object Book{
  implicit def toParameters: ToParameterList[Book] =
    Macro.toParameters[Book]
}

@javax.inject.Singleton
class BookRepository @Inject()(dbapi: DBApi, userRepository: UserRepository)(implicit ec: DatabaseExecutionContext){

  private val db = dbapi.database("default")

  private val simple = {
    get[Option[Int]]("tbl_book.id") ~
      get[String]("tbl_book.title") ~
      get[Int]("tbl_book.price") ~
      get[String]("tbl_book.author") ~
      get[Option[Int]]("tbl_book.user_id") map {
      case id ~ title ~ price ~ author ~ userId => Book(id, title, price, author, userId)
    }
  }

  private val withUser = simple ~ (userRepository.simple.?) map {
    case book ~ user => book -> user
  }

  def findById(id: Int): Future[Option[Book]] = Future {
    db.withConnection { implicit connection =>
      SQL"select * from tbl_book where id = $id".as(simple.singleOpt)
    }
  }(ec)

  def insert(book: Book): Future[Option[Long]] = Future {
    db.withConnection { implicit connection =>
      SQL("""insert into tbl_book values (DEFAULT, {title}, {price}, {author}, {userId})""").bind(book).executeInsert()
        }
     }(ec)

  def update(id: Int, book: Book) = Future{
    db.withConnection { implicit connection =>
      SQL(
        """update tbl_book set title = {title}, price = {price}, author = {author}, user_id = {userId} where id = {id}""").bind(book.copy(id = Some(id))).executeUpdate()
      // case class binding using ToParameterList,
      // note using SQL(..) but not SQL.. interpolation
      }
  }(ec)

  def delete(id: Int) = Future {
    db.withConnection { implicit connection =>
      SQL"delete from tbl_book where id = ${id}".executeUpdate()
    }
  }(ec)

  def list(page: Int = 0, pageSize: Int = 9, orderBy: Int = 1, filter: String = "%"): Future[Page[(Book, Option[User])]] = Future {

    val offset = pageSize * page

    db.withConnection { implicit connection =>
      val books = SQL"""
        select * from tbl_book left join tbl_user on tbl_book.user_id = tbl_user.id
        where tbl_book.title like ${filter}
        order by ${orderBy} nulls last
        limit ${pageSize} offset ${offset};
      """.as(withUser.*)

      val totalRows = SQL"""
        select count(*) from tbl_book
        left join tbl_user on tbl_book.user_id = tbl_user.id
        where tbl_book.title like ${filter}
      """.as(scalar[Int].single)

      Page(books, page, offset, totalRows)
    }
  }(ec)
}

