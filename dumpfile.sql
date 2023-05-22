drop schema if exists stackoverflow;
create schema stackoverflow;
use stackoverflow;

create table users (

user_id int primary key not null unique auto_increment,

username varchar(50) not null unique,

l_name varchar(50) not null,

f_name varchar(50) not null,

e_mail varchar(50) not null unique,

pword varchar(70) not null,

profile_img varchar(300) not null,

score float,

location varchar(50),

user_type varchar(20) not null
);

insert into users values
(1, 'cdorug', "Chete", "Doru", "cdorug@gmail.com", "65bb1cc2fd5feddea98e1d9e3ec89fae2a3f50ed42d6fb96962ef8cd0e2cfcb7", 'https://img.freepik.com/premium-vector/pixel-art-man-working-pc_665523-213.jpg?w=826', 0, 'Cluj', 'user');

insert into users (user_id, username, l_name, f_name, e_mail, pword, profile_img, score, location, user_type) values
(2, 'MrFlipflop', "Flip-Flopescu", "Dan", "flipflop@yahoo.com", "102c304fa0934097958dfb45fec492d8a4f1610c33211944a5a1eeefa4c93de5", 'https://i.imgur.com/o9fpo46.png', 0, 'Sibiu', 'user');

insert into users (user_id, username, l_name, f_name, e_mail, pword, profile_img, score, location, user_type) values
(3, 'BanLord', "Adminescu", "Atmin", "banHammer@gmail.com", "102c304fa0934097958dfb45fec492d8a4f1610c33211944a5a1eeefa4c93de5", 'https://wiki.teamfortress.com/w/images/d/dc/Banhammer.png', 0, 'Dej', 'admin');

create table questions (

question_id int primary key not null unique auto_increment,

author_id int not null,

question_title varchar(50) not null,

question_text varchar(300) not null,

creation_date date not null,

creation_time time not null,

image_URL varchar(300),

votes int,

foreign key (author_id) references users(user_id)
);

insert into questions values
(1, 1, "What is the meaning of life?", "I am experiencing an existential crisis. What programming language should I learn?", '2023-03-18', '09:10:00', "https://i.imgur.com/0dqdq3m.jpeg", 2),
(2, 2, "How do I debug VHDL code?", "I am really frustrated with Vivado, can somebody help?", '2023-03-19', '09:43:01', "https://i.imgur.com/neys6Vg.jpeg", 3),
(3, 2, "Is Python easy to learn?", "I want to try to learn Python.", '2023-03-19', '11:23:01', "https://upload.wikimedia.org/wikipedia/commons/c/c3/Python-logo-notext.svg", 4),
(4, 2, "Python Lists", "Can I really put all kinds of stuff into a single list?", '2023-03-20', '20:20:10', "https://i.imgur.com/3GmPd7O.png", 5);

create table answers (

answer_id int primary key not null unique auto_increment,

author_id int not null,

question_id int not null,

answer_text varchar(2000) not null,

creation_date date not null,

creation_time time not null,

votes int not null,

image_URL varchar(300),

foreign key (author_id) references users(user_id),

foreign key(question_id) references questions(question_id)
);

insert into answers values
(1, 2, 1, "I think you should take a break from programming abcd abcd reak from programming abcd reak from programming abcd reak from programming abcd reak from programming abcd reak from programming abcd  abcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcd abcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcd,ould take a break from programming abcd abcd abcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcd abcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdaould take a break from programming abcd abcd abcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcd abcd abcdabcd abcdabcd abcdabcd abcdabcd abcdabcd abcda", '2023-03-18', '19:10:00', 1, "https://i.imgur.com/MrGY5EL.jpeg");

create table tags (
tag_id int primary key not null unique auto_increment,

tag_text varchar(20) not null
);

insert into tags values
(1, "Python"), (2, "VHDL"), (3, "Spring"), (4, "Assembly"), (5, "Java"), (6, "Graphics"), (7, "MySQL"), (8, "Linux");

create table question_tags (
question_tag_id int primary key not null unique auto_increment,

tag_id int not null,

question_id int not null,

foreign key (tag_id) references tags(tag_id),

foreign key(question_id) references questions(question_id)
);

insert into question_tags values
(1, 1, 1), (2, 2, 2), (3, 8, 1);

create table votes (
vote_id int primary key not null unique auto_increment,

vote boolean not null,

question_id int,

answer_id int,

user_id int not null,

foreign key (answer_id) references answers(answer_id),

foreign key(question_id) references questions(question_id),

foreign key(user_id) references users(user_id),

constraint uc_user_question unique (user_id, question_id),

constraint uc_user_answer unique (user_id, answer_id)
);