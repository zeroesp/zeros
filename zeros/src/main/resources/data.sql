insert into com_menu values (1,'common','JAVA');
insert into com_menu values (2,'Kafka','BDA');
insert into com_menu values (3,'Flume','BDA');
insert into com_menu values (4,'Spark','BDA');

insert into com_user (user_id, login_id, login_pw, name, email, create_id, create_date, update_id, update_date)
values (1,'admin','$2a$10$RSJMSCN2LVJ4pjh8HdNsTuWflwDOIwWyQh7o8/BE2TUXDXy9KnyTu','어드민','admin@cloud.com','system',sysdate,'system',sysdate);

insert into com_user (user_id, login_id, login_pw, name, email, create_id, create_date, update_id, update_date)
values (2,'test','$2a$10$bcm2v4X/NJbWDXvNBBHtiuEK7vC4zCm.9w3nKeZ0ecMwjL18rkenW','테스트','test@cloud.com','system',sysdate,'system',sysdate);

insert into com_user_auth (user_id, auth, orders, create_id, create_date, update_id, update_date)
values (1, 'ADMIN', 1,'system',sysdate,'system',sysdate);

insert into com_user_auth (user_id, auth, orders, create_id, create_date, update_id, update_date)
values (2, 'USER', 1,'system',sysdate,'system',sysdate);

