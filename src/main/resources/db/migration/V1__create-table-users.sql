create table tb_users(

    id bigint not null auto_increment,
    name varchar(100) not null,
    email varchar(100) not null unique,
    password varchar(100) not null,
    cpf varchar(20) not null,

    primary key(id)

);