DELIMITER $$

-- Create a procedure to update follower/following counts
CREATE PROCEDURE UpdateFollowCounts(
    IN followerUsername VARCHAR(255),
    IN followedUsername VARCHAR(255),
    IN operation VARCHAR(10) -- 'INCREMENT' or 'DECREMENT'
)
BEGIN
    IF operation = 'INCREMENT' THEN
        -- Increment the number of followers for the followed user
        UPDATE Users
        SET NumFollowers = NumFollowers + 1
        WHERE Username = followedUsername;

        -- Increment the number of following for the follower user
        UPDATE Users
        SET NumFollowing = NumFollowing + 1
        WHERE Username = followerUsername;
    ELSEIF operation = 'DECREMENT' THEN
        -- Decrement the number of followers for the followed user
        UPDATE Users
        SET NumFollowers = NumFollowers - 1
        WHERE Username = followedUsername;

        -- Decrement the number of following for the follower user
        UPDATE Users
        SET NumFollowing = NumFollowing - 1
        WHERE Username = followerUsername;
    END IF;
END$$

-- Create a function to get the number of posts a user has liked
CREATE FUNCTION GetNumLikedPosts(userUsername VARCHAR(255))
    RETURNS INT
    READS SQL DATA
BEGIN
    DECLARE numLikedPosts INT DEFAULT 0;

    SELECT COUNT(*)
    INTO numLikedPosts
    FROM Likes
    WHERE Username = userUsername;

    RETURN numLikedPosts;
END$$

-- Trigger after inserting a follow to update follow counts and call the procedure
CREATE TRIGGER after_follow_insert
    AFTER INSERT ON Follows
    FOR EACH ROW
BEGIN
    -- Call the procedure to update follow counts
    CALL UpdateFollowCounts(NEW.FollowerUsername, NEW.FollowedUsername, 'INCREMENT');
END$$

-- Trigger after deleting a follow to update follow counts and call the procedure
CREATE TRIGGER after_follow_delete
    AFTER DELETE ON Follows
    FOR EACH ROW
BEGIN
    -- Call the procedure to update follow counts
    CALL UpdateFollowCounts(OLD.FollowerUsername, OLD.FollowedUsername, 'DECREMENT');
END$$

-- Create a procedure to update post count
CREATE PROCEDURE UpdatePostCount(
    IN userUsername VARCHAR(255),
    IN operation VARCHAR(10) -- 'INCREMENT' or 'DECREMENT'
)
BEGIN
    IF operation = 'INCREMENT' THEN
        -- Increment the number of posts for the user
        UPDATE Users
        SET PostsCount = PostsCount + 1
        WHERE Username = userUsername;
    ELSEIF operation = 'DECREMENT' THEN
        -- Decrement the number of posts for the user
        UPDATE Users
        SET PostsCount = PostsCount - 1
        WHERE Username = userUsername;
    END IF;
END$$

-- Trigger after inserting a post to update post count and call the procedure
CREATE TRIGGER after_post_insert
    AFTER INSERT ON Posts
    FOR EACH ROW
BEGIN
    -- Call the procedure to update post count
    CALL UpdatePostCount(NEW.Username, 'INCREMENT');
END$$

-- Trigger after deleting a post to update post count and call the procedure
CREATE TRIGGER after_post_delete
    AFTER DELETE ON Posts
    FOR EACH ROW
BEGIN
    -- Call the procedure to update post count
    CALL UpdatePostCount(OLD.Username, 'DECREMENT');
END$$

-- Create a procedure to update the number of likes for a post
CREATE PROCEDURE UpdateNumLikes(
    IN post_id INT,
    IN operation VARCHAR(10) -- 'INCREMENT' or 'DECREMENT'
)
BEGIN
    IF operation = 'INCREMENT' THEN
        -- Increment the number of likes for the post
        UPDATE Posts
        SET NumLikes = NumLikes + 1
        WHERE PostID = post_id;
    ELSEIF operation = 'DECREMENT' THEN
        -- Decrement the number of likes for the post
        UPDATE Posts
        SET NumLikes = NumLikes - 1
        WHERE PostID = post_id;
    END IF;
END$$

-- Create a procedure to update the number of comments for a post
CREATE PROCEDURE UpdateNumComments(
    IN post_id INT,
    IN operation VARCHAR(10) -- 'INCREMENT' or 'DECREMENT'
)
BEGIN
    IF operation = 'INCREMENT' THEN
        -- Increment the number of comments for the post
        UPDATE Posts
        SET NumComments = NumComments + 1
        WHERE PostID = post_id;
    ELSEIF operation = 'DECREMENT' THEN
        -- Decrement the number of comments for the post
        UPDATE Posts
        SET NumComments = NumComments - 1
        WHERE PostID = post_id;
    END IF;
END$$

-- Trigger after inserting a like to update the number of likes and call the procedure
CREATE TRIGGER after_like_insert
    AFTER INSERT ON Likes
    FOR EACH ROW
BEGIN
    -- Call the procedure to update the number of likes
    CALL UpdateNumLikes(NEW.PostID, 'INCREMENT');
END$$

-- Trigger after deleting a like to update the number of likes and call the procedure
CREATE TRIGGER after_like_delete
    AFTER DELETE ON Likes
    FOR EACH ROW
BEGIN
    -- Call the procedure to update the number of likes
    CALL UpdateNumLikes(OLD.PostID, 'DECREMENT');
END$$

-- Trigger after inserting a comment to update the number of comments and call the procedure
CREATE TRIGGER after_comment_insert
    AFTER INSERT ON Comments
    FOR EACH ROW
BEGIN
    -- Call the procedure to update the number of comments
    CALL UpdateNumComments(NEW.PostID, 'INCREMENT');
END$$

-- Trigger after deleting a comment to update the number of comments and call the procedure
CREATE TRIGGER after_comment_delete
    AFTER DELETE ON Comments
    FOR EACH ROW
BEGIN
    -- Call the procedure to update the number of comments
    CALL UpdateNumComments(OLD.PostID, 'DECREMENT');
END$$

DELIMITER ;
