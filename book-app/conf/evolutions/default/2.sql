# --- Second database inserts

# --- !Ups

insert into tbl_user (name) values ('bhalchandra');
insert into tbl_user (name) values ('esha');
insert into tbl_user (name) values ('anjali');
insert into tbl_user (name) values ('none');
insert into tbl_user (name) values ('guest1');
insert into tbl_user (name) values ('guest2');
insert into tbl_user (name) values ('guest3');

insert into tbl_book (title,price,author,user_id) values ('C++ primer',10,'Riche',2);
insert into tbl_book (title,price,author,user_id) values ('C language programming',12,'Kernigham Ritchie',2);
insert into tbl_book (title,price,author,user_id) values ('Java primer',12,'Goslin',2);
insert into tbl_book (title,price,author,user_id) values ('Practical electronics for inventors',12,'Scherz',1);
insert into tbl_book (title,price,author,user_id) values ('Outliers',12,'Malcolm Gladwell',3);
insert into tbl_book (title,price,author,user_id) values ('The tipping point',12,'Malcolm Gladwell',3);
insert into tbl_book (title,price,author,user_id) values ('When to jump',12,'Mike Lewis',1);
insert into tbl_book (title,price,author,user_id) values ('Data structures',12,'Esalow',2);
insert into tbl_book (title,price,author,user_id) values ('Expert C Programming',12,'Vander',2);
insert into tbl_book (title,price,author,user_id) values ('Algorithms Illustrated',12,'Roughgarden',1);
insert into tbl_book (title,price,author,user_id) values ('The first 90 days',12,'Watkins',3);
insert into tbl_book (title,price,author,user_id) values ('Cassandra the definitive guide',12,'Hewitt',5);
insert into tbl_book (title,price,author,user_id) values ('Managing and mining graph data',12,'Charu Aggrawal',1);
insert into tbl_book (title,price,author,user_id) values ('Amrutdhara',12,'Swami Madhawanand',3);
insert into tbl_book (title,price,author,user_id) values ('The joy of X',12,'Steven Strogatz',6);
insert into tbl_book (title,price,author,user_id) values ('Functional programming in scala',12,'Chiusana Runar',1);

# --- !Downs

delete from tbl_book;
delete from tbl_user;