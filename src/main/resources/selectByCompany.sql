create
database companies;

CREATE TABLE company
(
    id   integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id         integer NOT NULL,
    name       character varying,
    company_id integer,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company(id, name)
values (1, 'EPAM');
insert into company(id, name)
values (2, 'SoftServe');
insert into company(id, name)
values (3, 'GlobalLogic');
insert into company(id, name)
values (4, 'Luxoft');

select *
from company;

insert into person(id, name, company_id)
values (1, 'Noah', 4);
insert into person(id, name, company_id)
values (2, 'Liam', 4);
insert into person(id, name, company_id)
values (3, 'Ethan', 4);
insert into person(id, name, company_id)
values (4, 'Aaliyah', 4);

insert into person(id, name, company_id)
values (5, 'Bailey', 2);
insert into person(id, name, company_id)
values (6, 'Donald', 2);

insert into person(id, name, company_id)
values (7, 'Evan', 3);
insert into person(id, name, company_id)
values (8, 'Austin', 3);
insert into person(id, name, company_id)
values (9, 'Brian', 3);

insert into person(id, name, company_id)
values (10, 'Jesse', 1);
insert into person(id, name, company_id)
values (11, 'Claire', 1);
insert into person(id, name, company_id)
values (12, 'Harold', 1);
insert into person(id, name, company_id)
values (13, 'Dorothy', 1);
insert into person(id, name, company_id)
values (14, 'Caroline', 1);

select p.name, c.name
from person p
         join company c on p.company_id = c.id
where p.company_id != 3;

select c.name, count(p.id) as count
from company c join person p
on c.id = p.company_id
group by c.name
having count (p.company_id) > 0
order by count desc limit 1;
