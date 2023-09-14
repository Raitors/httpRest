-- liquibase formatted sql

-- changeset artem: =std_ind=
CREATE INDEX student_name_index ON student (name);

-- changeset artem: =fac_ind=
CREATE INDEX faculty_ind ON faculty (name,color);