create table car (
 id serial primary key,
 brand varchar,
 model varchar,
 price int
)


create table people (
 id serial primary key,
 name varchar,
 age int,
 driver_license boolean default true,
 car_id int references car (id)
  )
