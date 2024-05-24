DELIMITER $$
CREATE TRIGGER after_post_insert
AFTER INSERT ON Posts
FOR EACH ROW
BEGIN
    UPDATE Users
    SET PostsCount = PostsCount + 1
    WHERE Username = NEW.Username;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER after_comment_insert
AFTER INSERT ON Comments
FOR EACH ROW
BEGIN
    UPDATE Posts
    SET NumComments = NumComments + 1
    WHERE PostID = NEW.PostID;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER after_like_insert
AFTER INSERT ON Likes
FOR EACH ROW
BEGIN
    UPDATE Posts
    SET NumLikes = NumLikes + 1
    WHERE PostID = NEW.PostID;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER before_follow_insert
BEFORE INSERT ON Follows
FOR EACH ROW
BEGIN
    SET NEW.FollowDate = CURRENT_TIMESTAMP;
END$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER after_follow_insert
AFTER INSERT ON Follows
FOR EACH ROW
BEGIN
    -- Update the number of followers for the followed user
    UPDATE Users
    SET NumFollowers = NumFollowers + 1
    WHERE Username = NEW.FollowedUsername;

    -- Update the number of following for the follower user
    UPDATE Users
    SET NumFollowing = NumFollowing + 1
    WHERE Username = NEW.FollowerUsername;
END$$

CREATE TRIGGER after_follow_delete
AFTER DELETE ON Follows
FOR EACH ROW
BEGIN
    -- Update the number of followers for the unfollowed user
    UPDATE Users
    SET NumFollowers = NumFollowers - 1
    WHERE Username = OLD.FollowedUsername;

    -- Update the number of following for the unfollower user
    UPDATE Users
    SET NumFollowing = NumFollowing - 1
    WHERE Username = OLD.FollowerUsername;
END$$

DELIMITER ;
