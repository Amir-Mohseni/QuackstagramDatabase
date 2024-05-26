DELIMITER $$

-- Drop triggers
DROP TRIGGER IF EXISTS after_follow_insert$$
DROP TRIGGER IF EXISTS after_follow_delete$$
DROP TRIGGER IF EXISTS after_post_insert$$
DROP TRIGGER IF EXISTS after_post_delete$$
DROP TRIGGER IF EXISTS after_like_insert$$
DROP TRIGGER IF EXISTS after_like_delete$$
DROP TRIGGER IF EXISTS after_comment_insert$$
DROP TRIGGER IF EXISTS after_comment_delete$$

-- Drop procedures
DROP PROCEDURE IF EXISTS UpdateFollowCounts$$
DROP PROCEDURE IF EXISTS UpdatePostCount$$
DROP PROCEDURE IF EXISTS UpdateNumLikes$$
DROP PROCEDURE IF EXISTS UpdateNumComments$$

-- Drop functions
DROP FUNCTION IF EXISTS GetNumLikedPosts$$

DELIMITER ;
