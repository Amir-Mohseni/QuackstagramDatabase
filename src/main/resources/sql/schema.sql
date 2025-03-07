-- Users table
CREATE TABLE IF NOT EXISTS Users (
                                     Username       VARCHAR(255)  NOT NULL PRIMARY KEY,
                                     ProfilePicture TEXT          NOT NULL,
                                     Bio            TEXT          NULL,
                                     NumFollowers   INT           DEFAULT 0 NOT NULL,
                                     NumFollowing   INT           DEFAULT 0 NOT NULL,
                                     PostsCount     INT           DEFAULT 0 NOT NULL
);

CREATE INDEX idx_NumFollowers ON Users (NumFollowers);
CREATE INDEX idx_NumFollowing ON Users (NumFollowing);
CREATE INDEX idx_PostsCount ON Users (PostsCount);

-- Posts table
CREATE TABLE IF NOT EXISTS Posts (
                                     PostID      INT              NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                     Username    VARCHAR(255)     NULL,
                                     Caption     TEXT             NULL,
                                     MediaURL    TEXT             NULL,
                                     PostDate    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NULL,
                                     NumLikes    INT              DEFAULT 0 NULL,
                                     NumComments INT              DEFAULT 0 NULL,
                                     CONSTRAINT Posts_ibfk_1 FOREIGN KEY (Username) REFERENCES Users (Username)
);

CREATE INDEX idx_Username ON Posts (Username);
CREATE INDEX idx_PostDate ON Posts (PostDate);

-- Comments table
CREATE TABLE IF NOT EXISTS Comments (
                                        CommentID   INT              NOT NULL AUTO_INCREMENT,
                                        PostID      INT              NOT NULL,
                                        Username    VARCHAR(255)     NOT NULL,
                                        CommentText TEXT             NOT NULL,
                                        CommentDate TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                        PRIMARY KEY (CommentID),
                                        CONSTRAINT Comments_ibfk_1 FOREIGN KEY (PostID) REFERENCES Posts (PostID),
                                        CONSTRAINT Comments_ibfk_2 FOREIGN KEY (Username) REFERENCES Users (Username)
);

CREATE INDEX idx_PostID ON Comments (PostID);
CREATE INDEX idx_Username ON Comments (Username);
CREATE INDEX idx_CommentDate ON Comments (CommentDate);

-- Credentials table
CREATE TABLE IF NOT EXISTS Credentials (
                                           Username VARCHAR(255) NOT NULL PRIMARY KEY,
                                           Password VARCHAR(255) NOT NULL,
                                           FOREIGN KEY (Username) REFERENCES Users (Username)
);

-- Likes table
CREATE TABLE IF NOT EXISTS Likes (
                                     PostID   INT              NOT NULL,
                                     Username VARCHAR(255)     NOT NULL,
                                     LikeDate TIMESTAMP        DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                     PRIMARY KEY (PostID, Username),
                                     CONSTRAINT Likes_ibfk_1 FOREIGN KEY (PostID) REFERENCES Posts (PostID),
                                     CONSTRAINT Likes_ibfk_2 FOREIGN KEY (Username) REFERENCES Users (Username)
);

CREATE INDEX idx_Username ON Likes (Username);
CREATE INDEX idx_LikeDate ON Likes (LikeDate);

-- Follows table
CREATE TABLE IF NOT EXISTS Follows (
                                       FollowerUsername VARCHAR(255) NOT NULL,
                                       FollowedUsername VARCHAR(255) NOT NULL,
                                       FollowDate       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       PRIMARY KEY (FollowerUsername, FollowedUsername),
                                       CONSTRAINT Follows_ibfk_1 FOREIGN KEY (FollowerUsername) REFERENCES Users (Username),
                                       CONSTRAINT Follows_ibfk_2 FOREIGN KEY (FollowedUsername) REFERENCES Users (Username),
                                       CONSTRAINT Follows_ibfk_3 CHECK (FollowerUsername != FollowedUsername)
);

CREATE INDEX idx_FollowedUsername ON Follows (FollowedUsername);
CREATE INDEX idx_FollowDate ON Follows (FollowDate);

-- Indexes
CREATE INDEX idx_Username ON Users (Username);
CREATE INDEX idx_CommentID ON Comments (CommentID);