create table message (
    id INTEGER NOT null AUTO_INCREMENT,
    subject varchar (100) not null,
    content  varchar(500) not null,
    primary key (id)
);

create table address (
    id INTEGER NOT NULL AUTO_INCRENEMT,
    address varchar(50) not null,
    primary key (id)
);

create table mail (
    id INTEGER  not null Auto_increment,
    message_id integer not null foreign key references message(id),
    address_id integer not null foreign key references address(id),
);