-- Disable foreign key checks to avoid issues with truncating tables with foreign key dependencies
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate all tables
TRUNCATE TABLE Likes;
TRUNCATE TABLE Follows;
TRUNCATE TABLE Comments;
TRUNCATE TABLE Posts;
TRUNCATE TABLE Credentials;
TRUNCATE TABLE Users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
