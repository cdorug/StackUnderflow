drop schema if exists stackoverflow;
create schema stackoverflow;
use stackoverflow;

create table users (

cnp int primary key not null unique,

l_name varchar(50) not null,

f_name varchar(50) not null,

e_mail varchar(50) not null unique,

pword varchar(50) not null
);

insert into users values
(1,"Chete", "Doru", "cdorug@gmail.com", "parola1");

insert into users (cnp, l_name, f_name, e_mail, pword) values
(2,"Flip-Flopescu", "Dan", "flipflop@yahoo.com", "parola2");

create table questions (

question_id int primary key not null unique,

author_id int not null,

question_title varchar(50) not null,

question_text varchar(300) not null,

creation_date date not null,

creation_time time not null,

image_URL varchar(100),

foreign key (author_id) references users(cnp)
);

insert into questions values
(1, 1, "What is the meaning of life?", "I am experiencing an existential crisis. What programming language should I learn?", '2023-03-18', '09:10:00', "https://i.imgur.com/0dqdq3m.jpeg"),
(2, 2, "How do I debug VHDL code?", "I am really frustrated with Vivado, can somebody help?", '2023-03-19', '09:43:01', "https://i.imgur.com/neys6Vg.jpeg"),
(3, 2, "Is Python easy to learn?", "I want to try to learn Python.", '2023-03-19', '11:23:01', "https://i.imgur.com/3GmPd7O.png"),
(4, 2, "Python Lists", "Can I really put all kinds of stuff into a single list?", '2023-03-20', '20:20:10', "https://i.imgur.com/3GmPd7O.png");

create table answers (

answer_id int primary key not null unique,

author_id int not null,

question_id int not null,

answer_text varchar(300) not null,

creation_date date not null,

creation_time time not null,

image_URL varchar(100),

foreign key (author_id) references users(cnp),

foreign key(question_id) references questions(question_id)
);

insert into answers values
(1, 2, 1, "I think you should take a break from programming", '2023-03-18', '19:10:00', "https://i.imgur.com/MrGY5EL.jpeg");

create table tags (
tag_id int primary key not null unique,

tag_text varchar(20) not null
);

insert into tags values
(1, "Python"), (2, "VHDL"), (3, "Spring"), (4, "Assembly"), (5, "Java"), (6, "Graphics"), (7, "MySQL"), (8, "Linux");

create table question_tags (
question_tag_id int primary key not null unique,

tag_id int not null,

question_id int not null,

foreign key (tag_id) references tags(tag_id),

foreign key(question_id) references questions(question_id)
);

insert into question_tags values
(1, 1, 1), (2, 2, 2);

create table votes (
vote_id int primary key not null unique,

vote boolean not null,

question_id int,

answer_id int,

user_id int not null,

foreign key (answer_id) references answers(answer_id),

foreign key(question_id) references questions(question_id),

foreign key(user_id) references users(cnp)
);

insert into votes values
(1,  true, 1, null, 1), (2,  true, null, 1, 1);