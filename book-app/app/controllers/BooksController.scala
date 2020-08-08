package controllers

import javax.inject.Inject
import models._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._


import scala.concurrent.{ExecutionContext, Future}

class BooksController @Inject()(bookService: BookRepository,
                                userService: UserRepository,
                                cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc){

  private val logger = play.api.Logger(this.getClass)

  val Home = Redirect(routes.BooksController.list(0,2,""))

  val bookForm = Form(
    mapping(
      "id" -> optional(number),
      "title" -> nonEmptyText,
      "price" -> number,
      "author" -> nonEmptyText,
      "user" -> optional(number)
    )(Book.apply)(Book.unapply)
  )

  def index = Action {
    Home
  }

  def list(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    bookService.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%")).map { page =>
      Ok(views.html.list(page, orderBy, filter))
    }
  }
  def create = Action.async { implicit request =>
    print("calling create...")
    userService.options.map { options =>
      Ok(views.html.create(bookForm, options))
    }
  }


  def save:Action[AnyContent] = Action.async { implicit request =>

    bookForm.bindFromRequest().fold(
      formWithErrors => userService.options.map { options =>
        logger.debug("CAME INTO errorFunction")
        BadRequest(html.create(formWithErrors, options))
      },
      book => {
          bookService.insert(book).map { _ =>
            logger.debug("CAME INTO successFunction")
          Home.flashing("success" -> "Book %s has been created".format(book.title))
        }
      }
    )
  }

  def edit(id: Int) = Action.async { implicit request =>
    bookService.findById(id).flatMap{
      case Some(book) =>
        userService.options.map { options =>
          Ok(html.edit(id, bookForm.fill(book), options))
        }
      case other =>
        Future.successful(NotFound(s"Error: Book not found id: " ))
    }
  }

  def update(id: Int)= Action.async { implicit request =>
    bookForm.bindFromRequest().fold(
      formWithErrors => {
        logger.warn(s"form error: $formWithErrors")
        userService.options.map { options =>
          BadRequest(html.edit(id, formWithErrors, options))
        }
      },
        book => {
          bookService.update(id, book).map { _ =>
            Home.flashing("success" -> "Book %s has been updated".format(book.title))
          }
        }
    )
  }

  def delete(id: Int)= Action.async {
    bookService.delete(id).map { _ =>
      Home.flashing("success" -> "Book has been deleted")
    }
  }
}
