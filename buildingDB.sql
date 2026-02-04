CREATE DATABASE IF NOT EXISTS WorkoutTracker;
USE WorkoutTracker;
-- 1. User Table
CREATE TABLE USER (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    HEIGHT_IN FLOAT,
    WEIGHT_LBS FLOAT,
    BODYFAT FLOAT,
    EXPERIENCE INT,
    AGE INT,
    BIRTHDAY DATETIME
);

-- 2. Goal Table (Linked to User)
CREATE TABLE GOAL (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERID INT,
    GOAL VARCHAR(255),
    PROGRESS FLOAT,
    FOREIGN KEY (USERID) REFERENCES USER(ID) ON DELETE CASCADE
);

-- 3. Injury Table (Linked to User)
CREATE TABLE Injury (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERID INT,
    INJURY VARCHAR(255),
    RECOVERY VARCHAR(255),
    FOREIGN KEY (USERID) REFERENCES USER(ID) ON DELETE CASCADE
);

-- 4. Workout Table (Linked to User)
CREATE TABLE Workout (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERID INT,
    TYPE VARCHAR(100),
    DATE DATETIME,
    SUMMARY VARCHAR(255),
    DURATION INT,
    FOREIGN KEY (USERID) REFERENCES USER(ID) ON DELETE CASCADE
);

-- 5. MuscleGroup Table
CREATE TABLE MuscleGroup (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) NOT NULL
);

-- 6. Exercise Table (Linked to Workout and MuscleGroup)
CREATE TABLE Exercise (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    WORKOUTID INT,
    NAME VARCHAR(255),
    REPS INT,
    WEIGHT INT,
    PR BOOLEAN DEFAULT FALSE,
    MUSCLEID INT,
    FOREIGN KEY (WORKOUTID) REFERENCES Workout(ID) ON DELETE CASCADE,
    FOREIGN KEY (MUSCLEID) REFERENCES MuscleGroup(ID) ON DELETE SET NULL
);

-- Adding 5 Users
INSERT INTO USER (NAME, HEIGHT_IN, WEIGHT_LBS, BODYFAT, EXPERIENCE, AGE, BIRTHDAY) VALUES
('Alex Rivera', 70.5, 185.2, 18.5, 3, 28, '1996-05-14 08:00:00'),
('Jordan Smith', 64.0, 135.0, 22.1, 1, 24, '2000-11-20 09:30:00'),
('Casey Chen', 72.0, 210.5, 15.0, 10, 35, '1989-02-03 10:00:00'),
('Taylor Reed', 68.0, 160.0, 19.8, 5, 31, '1993-07-12 07:15:00'),
('Morgan Blaire', 66.5, 155.4, 25.0, 2, 29, '1995-12-25 11:45:00'),
('William Thacher', 71.8, 210.4, 15.0, 3, 21, '2004-3-09 12:45:00');

-- Adding 8 Muscle Groups
INSERT INTO MuscleGroup (NAME) VALUES 
('Chest'), ('Back'), ('Legs'), ('Shoulders'), ('Biceps'), ('Triceps'), ('Abs'), ('Cardio');

-- Adding 10 Goals (2 per user)
INSERT INTO GOAL (USERID, GOAL, PROGRESS) VALUES
(1, 'Bench Press 225lbs', 0.85), (1, 'Lose 5lbs body fat', 0.40),
(2, 'Run 5k under 25 mins', 0.60), (2, 'Consistent 3 days/week', 1.0),
(3, 'Squat 405lbs', 0.90), (3, 'Increase mobility', 0.50),
(4, 'Pull-up 10 reps', 0.70), (4, 'Bodyweight dips', 0.30),
(5, 'Yoga 2x weekly', 1.0), (5, 'New PR on Deadlift', 0.20);

-- Adding 5 Injuries
INSERT INTO Injury (USERID, INJURY, RECOVERY) VALUES
(1, 'Rotator Cuff Strain', 'Physical Therapy - 4 weeks'),
(3, 'Lower Back Tightness', 'Stretching & Heat'),
(4, 'Ankle Sprain', 'Rest & Compression'),
(5, 'Tennis Elbow', 'Ice and Brace'),
(2, 'None', 'Healthy');

-- Adding 10 Workouts
INSERT INTO Workout (USERID, TYPE, DATE, SUMMARY, DURATION) VALUES
(1, 'Strength', '2026-02-01 17:00:00', 'Chest and Triceps focus', 60),
(1, 'HIIT', '2026-02-03 06:30:00', 'Morning cardio blast', 30),
(2, 'Endurance', '2026-02-01 18:00:00', 'Long distance run', 45),
(3, 'Hypertrophy', '2026-02-02 12:00:00', 'Heavy Leg Day', 90),
(3, 'Recovery', '2026-02-03 10:00:00', 'Light swimming', 40),
(4, 'Calisthenics', '2026-02-01 16:00:00', 'Park workout', 50),
(4, 'Strength', '2026-02-03 15:30:00', 'Upper Body Power', 75),
(5, 'Yoga', '2026-02-02 08:00:00', 'Flexibility session', 60),
(5, 'Strength', '2026-02-03 19:00:00', 'Back and Biceps', 65),
(2, 'Strength', '2026-02-03 07:00:00', 'Full body circuit', 45);

-- Adding 22 Exercises
INSERT INTO Exercise (WORKOUTID, NAME, REPS, WEIGHT, PR, MUSCLEID) VALUES
(1, 'Bench Press', 10, 185, 0, 1), (1, 'Dumbbell Flys', 12, 40, 0, 1), (1, 'Tricep Pushdown', 15, 60, 1, 6),
(2, 'Burpees', 20, 0, 0, 8), (2, 'Mountain Climbers', 50, 0, 0, 8),
(3, 'Outdoor Run', 1, 0, 1, 8), (3, 'Walking Lunges', 20, 0, 0, 3),
(4, 'Back Squat', 5, 315, 1, 3), (4, 'Leg Press', 10, 500, 0, 3), (4, 'Calf Raises', 20, 150, 0, 3),
(5, 'Laps', 10, 0, 0, 8),
(6, 'Pull Ups', 12, 0, 0, 2), (6, 'Dips', 15, 0, 1, 6), (6, 'Push Ups', 30, 0, 0, 1),
(7, 'Overhead Press', 8, 135, 0, 4), (7, 'Lateral Raises', 15, 20, 0, 4),
(8, 'Sun Salutation', 5, 0, 0, 7), (8, 'Plank', 1, 0, 0, 7),
(9, 'Deadlift', 5, 350, 1, 2), (9, 'Barbell Row', 10, 185, 0, 2), (9, 'Hammer Curls', 12, 35, 0, 5),
(10, 'Goblet Squat', 15, 50, 0, 3);

-- 60 rows