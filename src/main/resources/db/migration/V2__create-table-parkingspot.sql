create table tb_parkingspot(

    id bigint not null auto_increment,
    code varchar(20) not null,
    status ENUM('AVAILABLE', 'OCCUPIED') not null,
    creationTime DATETIME DEFAULT CURRENT_TIMESTAMP,

    primary key(id)

);