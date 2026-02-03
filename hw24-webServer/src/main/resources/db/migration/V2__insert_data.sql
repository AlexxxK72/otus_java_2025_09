insert into address(street) values ('Москва, ул. Совхозная 15, кв.3');
insert into address(street) values ('Москва, ул. Совхозная 25, кв.15');
insert into address(street) values ('Москва, ул. Совхозная 16, кв.133');
insert into address(street) values ('Москва, ул. Совхозная 7, кв.31');

insert into client(address_id, name) values (1, 'Алексей Фролов');
insert into client(address_id, name) values (2, 'Юрий Федоров');
insert into client(address_id, name) values (3, 'Андрей Соколов');
insert into client(address_id, name) values (4, 'Александр Петров');

insert into phone(client_id, number) values (2, '925 734-58-59');
insert into phone(client_id, number) values (4, '903-536-82-18');
insert into phone(client_id, number) values (4, '903-536-18-18');