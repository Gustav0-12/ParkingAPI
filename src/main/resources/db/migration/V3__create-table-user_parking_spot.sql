create table tb_user_parkingspot(
    id bigint not null auto_increment,
    marca varchar(100) not null,
    cor varchar(100) not null,
    modelo varchar(100) not null,
    placa varchar(100) not null unique,
    valor decimal(10, 2) not null,
    recibo varchar(100) not null,
    dataEntrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    dataSaida DATETIME NULL,
    user_id bigint not null,
    parkingspot_id bigint not null,

    primary key(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_parkingspot FOREIGN KEY (parkingspot_id) REFERENCES tb_parkingspot(id) ON DELETE RESTRICT
);