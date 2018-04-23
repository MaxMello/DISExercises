-- comment: db2 -svtf create.tab

-- connect to VSISP user vsisp27;

create table EstateAgent
(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 name varchar(30) not null,
 address varchar(255),
 login varchar(20) not null unique,
 password varchar(20) not null);

insert into EstateAgent (name, address, login, password) values ('Hugo', 'Habichtstr. 44', 'login', 'password');
insert into EstateAgent (name, address, login, password) values ('Gerda', 'Siemersplatz 1', 'gerda', '123456');

create table Estate
(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 address VARCHAR(255),
 area int not null);

insert into Estate (address, area) values ('Spitalerstr. 5', 250);
insert into Estate (address, area) values ('Lange Reihe 23', 150);

create table Apartment
(estate int not null,
 floor int,
 rent int,
 rooms SMALLINT,
 balcony SMALLINT,
 kitchen SMALLINT,
  PRIMARY KEY (estate),
  FOREIGN KEY (estate) REFERENCES Estate(id) ON DELETE CASCADE);

insert into Apartment values (1, 3, 123, 4, 0, 0);

create table House
(estate int not null,
 floors SMALLINT,
 price int,
 garden SMALLINT,
  PRIMARY KEY (estate),
  FOREIGN KEY (estate) REFERENCES Estate(id) ON DELETE CASCADE);

insert into House values (2, 4, 12345, 0);

create table Person
(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 firstName varchar(20) not null,
 name varchar(20) not null,
 address VARCHAR(255));

insert into Person (firstName, name, address) values ('Gustav', 'Gans', 'Entenhausenstr. 33');
insert into Person (firstName, name, address) values ('Mickey', 'Mouse', 'Kellinghusenstr. 4');

create table Contract
(no int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY,
 cDate date not null,
 place varchar(20) not null);

insert into Contract (cDate, place) values ('2004-06-04', 'Honululu');
insert into Contract (cDate, place) values ('2016-01-01', 'Berlin');

create table TenancyContract
(contract int not null,
 startDate date not null,
 duration int,
 addCosts int,
  PRIMARY KEY (contract),
  FOREIGN KEY (contract) REFERENCES Contract(no) ON DELETE CASCADE);

insert into TenancyContract values (1, '2004-09-01', 20, 100);

create table PurchaseContract
(contract int not null,
 nrInstallments int,
 rate int,
  PRIMARY KEY (contract),
  FOREIGN KEY (contract) REFERENCES Contract(no) ON DELETE CASCADE);

insert into PurchaseContract values (2, 24, 1234);

create table manages
(estate int not null,
 agent int not null,
  FOREIGN KEY (estate) REFERENCES Estate(id) ON DELETE CASCADE,
  FOREIGN KEY (agent) REFERENCES EstateAgent(id) ON DELETE CASCADE,
  PRIMARY KEY (estate));

insert into manages values (1, 1);
insert into manages values (2, 2);

create table sells
(contract int not null,
 house int not null,
 person int not null,
  FOREIGN KEY (house) REFERENCES House(estate) ON DELETE CASCADE,
  FOREIGN KEY (person) REFERENCES Person(id) ON DELETE CASCADE,
  FOREIGN KEY (contract) REFERENCES PurchaseContract(contract) ON DELETE CASCADE,
  PRIMARY KEY (contract));

insert into sells values(2, 2, 2);

create table rents
(tenancyContract int not null,
 apartment int not null,
 person int not null,
  FOREIGN KEY (apartment) REFERENCES Apartment(estate) ON DELETE CASCADE,
  FOREIGN KEY (person) REFERENCES Person(id) ON DELETE CASCADE,
  FOREIGN KEY (tenancyContract) REFERENCES TenancyContract(contract) ON DELETE CASCADE,
  PRIMARY KEY (tenancyContract));

insert into rents values(1, 1, 1);

-- commit work;

-- connect reset;