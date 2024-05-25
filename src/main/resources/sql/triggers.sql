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

DELIMITER ;
