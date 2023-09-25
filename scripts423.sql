select s.name, s.age, f.name
from student  s
inner join faculty f on  s.faculty_id = faculty_id


select s.name, a.media_type
from student s
right join avatar a on s.id = a.student_id