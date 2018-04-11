# DIS Übung 1

1.1
b.) ? SQL0911N = The current transaction has been rolled back because of a deadlock or timeout.

d.)
``` create table tab3
  (name varchar(20) not null,
   phone char(40),
   salary dec(7,2));
```   
e) 
-- comment:  db2 -svtf create.tab

Script:
```
connect to sample;
INSERT INTO tab3 VALUES ('john','243',21364235) ('bla','23',456789);
commit work;
connect reset;
```
