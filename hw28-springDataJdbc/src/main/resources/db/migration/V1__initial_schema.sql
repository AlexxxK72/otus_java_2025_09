create table Client
(
    id   bigserial not null primary key,
    name varchar(255)
);

create table Address
(
    client_id bigint not null references Client (id),
    street    varchar(255)
);

create table Phone
(
    id        bigserial not null primary key,
    number    varchar(255),
    client_id bigint    not null references Client (id),
    phone_order int
);

