create table com_menu (
  id int not null,
  menu varchar(30),
  menu_group varchar(30)
);

create primary key on com_menu (id);

create table com_user (
  user_id varchar(10) not null,
  login_id varchar(20) not null,
  login_pw varchar(200) not null,
  name varchar(20),
  email varchar(30),
  create_id varchar(20),
  create_date date,
  update_id varchar(20),
  update_date date
);

create primary key on com_user (user_id);

create table com_user_auth (
  user_id varchar(10) not null,
  auth varchar(20) not null,
  orders int,
  create_id varchar(20),
  create_date date,
  update_id varchar(20),
  update_date date
);

create primary key on com_user_auth (user_id, auth);