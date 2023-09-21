alter table student
add constraint age check(age >=16);

alter table student
add constraint  name unique(name);
alter table student
alter column name set not null;

alter table faculty
add constraint name_color unique(name,color);

alter table student
alter colum age set default 20;