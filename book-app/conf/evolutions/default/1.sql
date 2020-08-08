# --- First database schema

# --- !Ups

create table if not exists tbl_user(
  id                        SERIAL not null,
  name                      varchar(255) not null,
  constraint pk_user primary key (id));

create table if not exists tbl_book(
  id                        SERIAL not null,
  title                     varchar(255) not null,
  price                     int,
  author                    varchar(255),
  user_id                   int,
  constraint pk_book primary key (id));

alter table tbl_book add constraint fk_book_user_1 foreign key (user_id) references tbl_user (id) on delete restrict on update restrict;
create index ix_book_user_1 on tbl_book (user_id);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists tbl_book;

drop table if exists tbl_user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_seq;

drop sequence if exists book_seq;