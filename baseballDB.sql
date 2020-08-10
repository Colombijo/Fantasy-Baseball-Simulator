CREATE DATABASE IF NOT EXISTS baseball;
USE baseball;

DROP TABLE IF EXISTS players;
CREATE TABLE players (
    pid varchar(50),
    firstName varchar(50),
    lastName varchar(50),
    position varchar(50),
    primary key (pid)
);

CREATE TABLE BATTERS (
  pid varchar(50),
  firstName varchar(50),
  lastName varchar(50),
  position varchar(50),
  primary key (pid)
);

CREATE TABLE PITCHERS (
  pid varchar(50),
  firstName varchar(50),
  lastName varchar(50),
  primary key (pid)
)
