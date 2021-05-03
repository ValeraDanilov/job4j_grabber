create database meetings;

create table meeting
(
    id   serial primary key,
    name varchar
);

create table users
(
    id   serial primary key,
    name varchar
);

create table statuses
(
    id         serial primary key,
    status     boolean,
    meeting_id int references meeting (id),
    users_id   int references users (id)
);

insert into meeting(name)
values ('Корпоратив');
insert into meeting(name)
values ('Встреча одноклассников');
insert into meeting(name)
values ('Встреча директоров');
insert into meeting(name)
values ('Встреча анонимных алкоголиков');

insert into users(name)
values ('Emma');
insert into users(name)
values ('Ira');
insert into users(name)
values ('Caleb');
insert into users(name)
values ('Olivia');
insert into users(name)
values ('Andrew');
insert into users(name)
values ('Joshua');
insert into users(name)
values ('Charlotte');
insert into users(name)
values ('Ethan');

insert into statuses(status, meeting_id, users_id)
values (false, 1, 1);
insert into statuses(status, meeting_id, users_id)
values (true, 4, 1);
insert into statuses(status, meeting_id, users_id)
values (true, 2, 2);
insert into statuses(status, meeting_id, users_id)
values (false, 2, 3);
insert into statuses(status, meeting_id, users_id)
values (true, 3, 4);
insert into statuses(status, meeting_id, users_id)
values (true, 3, 5);
insert into statuses(status, meeting_id, users_id)
values (false, 4, 6);
insert into statuses(status, meeting_id, users_id)
values (true, 1, 7);
insert into statuses(status, meeting_id, users_id)
values (false, 2, 7);
insert into statuses(status, meeting_id, users_id)
values (false, 1, 4);
insert into statuses(status, meeting_id, users_id)
values (true, 3, 8);

select m.name, count(s.status)
from meeting m
         join statuses s on m.id = s.meeting_id
where status = true
group by m.name;

select m.name, count(s.users_id)
from meeting m
         left join statuses s on m.id = s.meeting_id
where status = false
   or s.meeting_id is null
group by m.name;
