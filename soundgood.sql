DROP TABLE IF EXISTS "person" CASCADE;
DROP TABLE IF EXISTS "email" CASCADE;
DROP TABLE IF EXISTS "phone" CASCADE;
DROP TABLE IF EXISTS "person_email" CASCADE;
DROP TABLE IF EXISTS "person_phone" CASCADE;
DROP TABLE IF EXISTS "student" CASCADE;
DROP TABLE IF EXISTS "instrument" CASCADE;
DROP TABLE IF EXISTS "stock" CASCADE;
DROP TABLE IF EXISTS "rental_instrument" CASCADE;
DROP TABLE IF EXISTS "application" CASCADE;
DROP TABLE IF EXISTS "student_enrollment" CASCADE;
DROP TABLE IF EXISTS "student_sibling" CASCADE;
DROP TABLE IF EXISTS "student_lesson" CASCADE;
DROP TABLE IF EXISTS "schedule" CASCADE;
DROP TABLE IF EXISTS "instructor" CASCADE;
DROP TABLE IF EXISTS "administrator" CASCADE;
DROP TABLE IF EXISTS "audition" CASCADE;
DROP TABLE IF EXISTS "parent" CASCADE;
DROP TABLE IF EXISTS "lesson" CASCADE;
DROP TABLE IF EXISTS "instructor_lesson" CASCADE;
DROP TABLE IF EXISTS "pricing" CASCADE;
DROP TABLE IF EXISTS "group_lesson" CASCADE;
DROP TABLE IF EXISTS "individual_lesson" CASCADE;
DROP TABLE IF EXISTS "facility" CASCADE;
DROP TABLE IF EXISTS "room" CASCADE;
DROP TABLE IF EXISTS "student_payment" CASCADE;
DROP TABLE IF EXISTS "instructor_student_payment" CASCADE;
DROP TABLE IF EXISTS "instructor_payment" CASCADE;
DROP TABLE IF EXISTS "sibling" CASCADE;
DROP TABLE IF EXISTS "ensemble" CASCADE;
DROP TABLE IF EXISTS "person_instrument" CASCADE;


CREATE TABLE "person"
(
    "id" serial PRIMARY KEY,
    "person_number" varchar(12) UNIQUE,
    "first_name" varchar(50),
    "last_name" varchar(50),
    "age" int,
    "street" varchar(100),
    "zip" varchar(5),
    "city" varchar(50)
);

CREATE TABLE "email"
(
    "id" serial PRIMARY KEY,
    "email" varchar(100) UNIQUE NOT NULL
);

CREATE TABLE "phone"
(
    "id" serial PRIMARY KEY,
    "phone_no" varchar(12) NOT NULL
);

CREATE TABLE "person_email"
(
    "email_id" int NOT NULL REFERENCES "email" ON DELETE CASCADE,
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE,
    PRIMARY KEY("email_id", "person_id")
);

CREATE TABLE "person_phone"
(
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE,
    "phone_id" int NOT NULL REFERENCES "phone" ON DELETE CASCADE,
    PRIMARY KEY("person_id", "phone_id")
);


CREATE TABLE "student"
(
    "id" serial PRIMARY KEY,
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE
);

CREATE TABLE "instrument"
(
    "id" serial PRIMARY KEY,
    "instrument_type" varchar(500) NOT NULL,
    "instrument_name" varchar(500) NOT NULL
);

CREATE TABLE "person_instrument"
(
    "instrument_id" int NOT NULL REFERENCES "instrument" ON DELETE CASCADE,
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE,
    "present_skill" varchar(12)
);

CREATE TABLE "rental_instrument"
(
    "id" SERIAL PRIMARY KEY,
    "instrument_id" int NOT NULL REFERENCES "instrument" ON DELETE CASCADE,
    "brand" varchar(500),
    "date" date,
    "time" time,
    "duration" int,
    "fee" int,
    "is_rented" boolean,
    "is_terminated" boolean
);

CREATE TABLE "stock"
(
    "instrument_id" int NOT NULL REFERENCES "instrument" ON DELETE CASCADE,
    "instrument_quantity" int NOT NULL
);

CREATE TABLE "administrator"
(
    "id" serial PRIMARY KEY,
    "employment_id" serial NOT NULL,
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE
);

CREATE TABLE "application"
(
    "application_id" serial PRIMARY KEY,
    "student_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE,
    "administrator_id" int NOT NULL REFERENCES "administrator" ON DELETE CASCADE,
    "instrument_id" int NOT NULL REFERENCES "instrument" ON DELETE CASCADE,
    "instrument_skill" varchar(500) NOT NULL,
    "date" timestamp,
    "time" time
);


CREATE TABLE "student_enrollment"
(
    "id" serial PRIMARY KEY,
    "student_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE,
    "date" date NOT NULL,
    "passed" varchar(5)
);

-- CREATE TABLE "sibling"
-- (
--     "id" serial PRIMARY KEY,
--     "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE
-- );

CREATE TABLE "student_sibling"
(
    "student_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE,
    "student_sibling_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE,
    PRIMARY KEY ("student_id", "student_sibling_id")
);

CREATE TABLE "facility"
(
    "id" serial PRIMARY KEY,
    "spots" int,
    "opening_hour" time NOT NULL,
    "closing_hour" time NOT NULL,
    "street" varchar(500),
    "city" varchar(500),
    "zip" varchar(500)
);

CREATE TABLE "room"
(
    "id" serial PRIMARY KEY,
    "room_number" int,
    "facility_id" int NOT NULL REFERENCES "facility" ON DELETE CASCADE
);

CREATE TABLE "lesson"
(
    "id" serial PRIMARY KEY,
    "level" varchar(500),
    "date" date NOT NULL,
    "time" time,
    "duration" int,
    "room_id" int NOT NULL REFERENCES "room" on DELETE CASCADE
);

CREATE TABLE "student_lesson"
(
    "student_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE,
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    PRIMARY KEY ("student_id", "lesson_id")
);

CREATE TABLE "instructor"
(
    "id" serial PRIMARY KEY,
    "employment_id" serial NOT NULL,
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE  
);


CREATE TABLE "audition"
(
    "student_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE,
    "administrator_id" int NOT NULL REFERENCES "administrator" ON DELETE CASCADE,
    "date" date NOT NULL,
    "time" time NOT NULL,
    "passed" varchar(5),
    PRIMARY KEY("student_id")
);

CREATE TABLE "parent"
(
    "id" serial PRIMARY KEY,
    "person_id" int NOT NULL REFERENCES "person" ON DELETE CASCADE,
    "student_id" int NOT NULL REFERENCES "student" ON DELETE CASCADE
);


CREATE TABLE "instructor_lesson"
(
    "instructor_id" int NOT NULL REFERENCES "instructor" ON DELETE CASCADE,
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    PRIMARY KEY ("instructor_id", "lesson_id")
);

CREATE TABLE "pricing"
(
    "id" serial PRIMARY KEY,
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    "weekday" date NOT NULL,
    "level" varchar(500) NOT NULL,
    "discount" int
);

CREATE TABLE "group_lesson"
(
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    "max_students" int,
    "min_students" int,
    "instrument_name" varchar(50) NOT NULL,
    PRIMARY KEY ("lesson_id")
);

CREATE TABLE "individual_lesson"
(
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    "instrument_name" varchar(50) NOT NULL,
    PRIMARY KEY ("lesson_id")
);

-- ensemble kanske ska ha en surrogate key?
CREATE TABLE "ensemble"
(
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    "max_students" int,
    "min_students" int,
    "genre" varchar(50),
    PRIMARY KEY ("lesson_id")
);



CREATE TABLE "schedule"
(
    "id" serial PRIMARY KEY,
    "lesson_id" int NOT NULL REFERENCES "lesson" ON DELETE CASCADE,
    "room_id" int NOT NULL REFERENCES "room" ON DELETE CASCADE
);

CREATE TABLE "student_payment"
(
    "id" serial PRIMARY KEY,
    "pricing_id" int NOT NULL REFERENCES "pricing" ON DELETE CASCADE,
    "fee" int,
    "extra_charge" int
);

CREATE TABLE "instructor_payment"
(
    "id" serial PRIMARY KEY,
    "instructor_id" int NOT NULL REFERENCES "instructor" ON DELETE CASCADE
);

CREATE TABLE "instructor_student_payment"
(
    "student_payment_id" int NOT NULL REFERENCES "student_payment" ON DELETE CASCADE,
    "instructor_payment_id" int NOT NULL REFERENCES "instructor_payment" ON DELETE CASCADE,
    PRIMARY KEY ("student_payment_id", "instructor_payment_id")
);


-- INSERTING DATA!

-- students

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('199810197989', 'Rigmor', 'Helgesson', '22', 'Lommaryd', '38232', 'Nybro');

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('198503176417', 'Kenth', 'Svärd', '35', 'Vittensten', '54244', 'Mariestad');

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('200503208375', 'Erik', 'Dahlén', '15', 'Lotusgränd', '44351', 'Lerum');

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('200312247829', 'Tova', 'Dahlén', '17', 'Lotusgränd', '44351', 'Lerum');

-- instructors

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('199711070970', 'Otto', 'Nordkvist', '23', 'Spångtorp', '81594', 'Tierp');

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('197608314147', 'Daisy', 'Larsen', '44', 'Norra åkershult solhöjden', '66992', 'Deje');

-- administrators

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('200211165436', 'Bjarne', 'Köhler', '18', 'Lyckåsvägen', '57393', 'Tranås');

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('197408044225', 'Marianne', 'Lilja', '46', 'Sigridstorp', '46876', 'Hällefors');

-- parent

INSERT INTO "person" ("person_number", "first_name", "last_name", "age", "street", "zip", "city")
VALUES ('196511247873', 'Elon', 'Dahlén', '55', 'Lotusgränd', '44351', 'Lerum');


-- student

INSERT INTO "student" ("person_id")
VALUES ('1');
INSERT INTO "student" ("person_id")
VALUES ('2');
INSERT INTO "student" ("person_id")
VALUES ('3');
INSERT INTO "student" ("person_id")
VALUES ('4');

-- instructor

INSERT INTO "instructor" ("person_id")
VALUES ('5');
INSERT INTO "instructor" ("person_id")
VALUES ('6');

-- administrator

INSERT INTO "administrator" ("person_id")
VALUES ('7');
INSERT INTO "administrator" ("person_id")
VALUES ('8');

-- parent
INSERT INTO "parent" ("student_id", "person_id")
VALUES ('3', '9');
INSERT INTO "parent" ("student_id", "person_id")
VALUES ('4', '9');

--sibling (student)
INSERT INTO "student_sibling" ("student_id", "student_sibling_id")
VALUES ('3', '4');

-- person
INSERT INTO "phone" ("phone_no")
VALUES ('01713158447');

INSERT INTO "phone" ("phone_no")
VALUES ('05874181700');

INSERT INTO "phone" ("phone_no")
VALUES ('04808378403');

INSERT INTO "phone" ("phone_no")
VALUES ('02478240984');

INSERT INTO "phone" ("phone_no")
VALUES ('06111447058');

INSERT INTO "phone" ("phone_no")
VALUES ('05837770983');

INSERT INTO "phone" ("phone_no")
VALUES ('02432880100');

INSERT INTO "phone" ("phone_no")
VALUES ('09401186602');

--person_phone
INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('1', '1');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('2', '2');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('3', '3');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('4', '4');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('5', '5');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('6', '6');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('7', '7');

INSERT INTO "person_phone" ("person_id", "phone_id")
VALUES ('8', '8');

-- email
INSERT INTO "email" ("email")
VALUES ('rigmor_helgesson@gmail.com');

INSERT INTO "email" ("email")
VALUES ('kenth.svärd@gmail.com');

INSERT INTO "email" ("email")
VALUES ('erik.dahlén@hotmail.com');

INSERT INTO "email" ("email")
VALUES ('tova.dahlén@gmail.com');

INSERT INTO "email" ("email")
VALUES ('otto_nordkvist@soundgood.com');

INSERT INTO "email" ("email")
VALUES ('daisy_larsen@soundgood.com');

INSERT INTO "email" ("email")
VALUES ('bjarne_köhler@soundgood.com');

INSERT INTO "email" ("email")
VALUES ('marianne_lilja@soundgood.com');

INSERT INTO "email" ("email")
VALUES ('elon_dahlén@soundgood.com');


--person_email
INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('1', '1');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('2', '2');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('3', '3');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('4', '4');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('5', '5');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('6', '6');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('7', '7');

INSERT INTO "person_email" ("person_id", "email_id")
VALUES ('8', '8');

-- instrument - TYPES: wind, string
INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'violin');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('wind', 'trumpet');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'piano');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'guitar');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'piano');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('wind', 'trumpet');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'guitar');

-- 8, 9
INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'guitar');

INSERT INTO "instrument" ("instrument_type", "instrument_name")
VALUES ('string', 'piano');


-- rental_instrument (duration in months)
INSERT INTO "rental_instrument" ("instrument_id", "brand", "date", "time", "duration", "fee", "is_rented", "is_terminated")
VALUES ('1', 'stentor', '2020-10-01', '13:00', '3', '400', TRUE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "date", "time", "duration", "fee", "is_rented", "is_terminated")
VALUES ('3', 'steinway and sons', '2020-12-07', '12:00', '2', '800', TRUE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "date", "time", "duration", "fee", "is_rented", "is_terminated")
VALUES ('4', 'fender', '2020-11-20', '15:00', '8', '600', TRUE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "fee", "is_rented", "is_terminated")
VALUES ('5', 'yamaha', '450', FALSE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "fee", "is_rented", "is_terminated")
VALUES ('5', 'yamaha', '450', FALSE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "fee", "is_rented", "is_terminated")
VALUES ('7', 'fender', '600', FALSE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "fee", "is_rented", "is_terminated")
VALUES ('7', 'fender', '600', FALSE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "fee", "is_rented", "is_terminated")
VALUES ('8', 'gibson', '700', FALSE, FALSE);

INSERT INTO "rental_instrument" ("instrument_id", "brand", "fee", "is_rented", "is_terminated")
VALUES ('9', 'bösendorfer', '1000', FALSE, FALSE);


INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('1', '0');

INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('3', '0');

INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('4', '0');

INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('5', '2');

INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('7', '2');

INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('8', '1');

INSERT INTO "stock" ("instrument_id", "instrument_quantity")
VALUES ('9', '1');




-- person_instrument
--      student instruments
INSERT INTO "person_instrument" ("instrument_id", "person_id", "present_skill")
VALUES ('1', '1', 'beginner');

INSERT INTO "person_instrument" ("instrument_id", "person_id", "present_skill")
VALUES ('2', '2', 'intermediate');

INSERT INTO "person_instrument" ("instrument_id", "person_id", "present_skill")
VALUES ('3', '3', 'advanced');

INSERT INTO "person_instrument" ("instrument_id", "person_id", "present_skill")
VALUES ('4', '4', 'intermediate');
--      instructor instruments

INSERT INTO "person_instrument" ("instrument_id", "person_id", "present_skill")
VALUES ('6', '6', 'advanced');


-- application
INSERT INTO "application" ("student_id", "administrator_id", "instrument_skill", "instrument_id", "date", "time")
VALUES ('1', '1', 'beginner', '1', '2020-09-25', '09:00');

INSERT INTO "application" ("student_id", "administrator_id", "instrument_skill", "instrument_id", "date", "time")
VALUES ('2', '1', 'intermediate', '2', '2020-12-01', '15:00');

INSERT INTO "application" ("student_id", "administrator_id", "instrument_skill", "instrument_id", "date", "time")
VALUES ('3', '2', 'advanced', '3',  '2020-12-01', '13:00');

INSERT INTO "application" ("student_id", "administrator_id", "instrument_skill", "instrument_id", "date", "time")
VALUES ('4', '1', 'intermediate', '4', '2020-10-31', '13:00');

-- student_enrollment
INSERT INTO "student_enrollment" ("student_id", "date", "passed")
VALUES ('1', '2020-09-30', 'true');

INSERT INTO "student_enrollment" ("student_id", "date", "passed")
VALUES ('2', '2020-12-06', 'true');

INSERT INTO "student_enrollment" ("student_id", "date", "passed")
VALUES ('3', '2020-10-31', 'true');

INSERT INTO "student_enrollment" ("student_id", "date", "passed")
VALUES ('4', '2020-11-04', 'true');

-- audition
INSERT INTO "audition" ("student_id", "date", "time", "passed", "administrator_id")
VALUES ('3', '2020-12-03', '14:00', 'true', '2');

-- facility
INSERT INTO "facility" ("spots", "opening_hour", "closing_hour", "street", "city", "zip")
VALUES ('200', '08:00', '19:00', 'Hanstavägen', 'Stockholm', '16362');

INSERT INTO "facility" ("spots", "opening_hour", "closing_hour", "street", "city", "zip")
VALUES ('250', '07:00', '20:00', 'Regeringsgatan', 'Stockholm', '10571');

-- room
INSERT INTO "room" ("room_number", "facility_id")
VALUES ('201', '1');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('202', '1');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('203', '1');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('201', '2');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('202', '2');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('203', '2');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('301', '2');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('302', '2');

INSERT INTO "room" ("room_number", "facility_id")
VALUES ('303', '2');

-- lesson ... beginner, intermediate and advanced
INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('beginner', '2020-09-10', '10:00', '90', '1');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2020-11-10', '13:00', '60', '2');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2022-04-10', '12:00', '60', '2');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('advanced', '2020-12-11', '12:00', '60', '7');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2020-12-20', '19:00', '60', '1');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2021-01-15', '12:00', '60', '1');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2021-01-23', '13:00', '60', '2');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2021-01-29', '15:00', '60', '2');
--9,10
INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2021-01-02', '15:00', '60', '2');

INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2021-01-03', '15:00', '60', '2');
--11, no students attending
INSERT INTO "lesson" ("level", "date", "time", "duration", "room_id")
VALUES ('intermediate', '2021-01-03', '12:00', '60', '2');

-- group_lesson
INSERT INTO "group_lesson" ("lesson_id", "max_students", "min_students", "instrument_name")
VALUES ('2', '5', '2', 'piano');

INSERT INTO "group_lesson" ("lesson_id", "max_students", "min_students", "instrument_name")
VALUES ('3', '10', '2', 'guitar');

INSERT INTO "group_lesson" ("lesson_id", "max_students", "min_students", "instrument_name")
VALUES ('6', '10', '2', 'guitar');

INSERT INTO "group_lesson" ("lesson_id", "max_students", "min_students", "instrument_name")
VALUES ('7', '10', '2', 'trumpet');

INSERT INTO "group_lesson" ("lesson_id", "max_students", "min_students", "instrument_name")
VALUES ('8', '10', '2', 'piano');

-- ensemble
INSERT INTO "ensemble" ("lesson_id", "max_students", "min_students", "genre")
VALUES ('4', '20', '5', 'classical');

INSERT INTO "ensemble" ("lesson_id", "max_students", "min_students", "genre")
VALUES ('9', '20', '5', 'classical');

INSERT INTO "ensemble" ("lesson_id", "max_students", "min_students", "genre")
VALUES ('10', '20', '5', 'jazz');

INSERT INTO "ensemble" ("lesson_id", "max_students", "min_students", "genre")
VALUES ('11', '20', '5', 'pop');

-- individual_lesson
INSERT INTO "individual_lesson" ("lesson_id", "instrument_name")
VALUES ('1', 'piano');

-- student_lesson
INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('1', '1');

INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('2', '2');

INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('3', '2');

INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('4', '4');


--ensemble
INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('1', '9');

INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('2', '10');

INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('3', '10');

INSERT INTO "student_lesson" ("student_id", "lesson_id")
VALUES ('4', '10');

-- instructor_lesson
INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '1');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '2');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '3');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('2', '3');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('2', '4');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('2', '5');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('2', '6');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '7');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '8');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '9');

INSERT INTO "instructor_lesson" ("instructor_id", "lesson_id")
VALUES ('1', '10');