# --- !Ups

CREATE TABLE records (
  pk VARCHAR (50) PRIMARY KEY,
  score VARCHAR (50) NOT NULL
);

# --- !Downs
;
drop table records;