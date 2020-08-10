USE baseball;

DROP TABLE IF EXISTS batterstats;
CREATE TABLE batterStats (
  pid varchar(50),
  firstName varchar(50),
  lastName varchar(50),
  position varchar(50),
  year int,
  pa int,
  ab int,
  H int,
  2B int,
  3B int,
  HR int,
  BB int,
  SO int,
  HBP int,
  primary key (pid)
);
