CREATE VIEW MutualFollows AS
SELECT u1.Username AS 'Follower 1', u2.Username AS 'Follower 2'
FROM Users u1, Users u2, Follows f1, Follows f2
WHERE f1.FollowedUsername = u1.Username AND f1.FollowerUsername = u2.Username
  AND f2.FollowedUsername = u2.Username AND f2.FollowerUsername = u1.Username
  AND u1.Username < u2.Username;

-- View for top 5 most followed users
CREATE VIEW top_5_most_followed AS
SELECT u.*,
       (SELECT COUNT(*) FROM Follows f WHERE f.FollowedUsername = u.Username) AS NumFollowers
FROM Users u
ORDER BY NumFollowers DESC
LIMIT 5;

-- View for latest 15 posts
CREATE VIEW latest_15_posts AS
SELECT p.*
FROM Posts p
ORDER BY PostDate DESC
LIMIT 15;

-- View for top 15 most liked posts
CREATE VIEW top_15_liked_posts AS
SELECT p.*,
       (SELECT COUNT(*) FROM Likes l WHERE l.PostID = p.PostID) AS NumLikes
FROM Posts p
ORDER BY NumLikes DESC
LIMIT 15;

-- View for user's post count, followers, and followings
CREATE VIEW user_statistics AS
SELECT u.Username,
       (SELECT COUNT(*) FROM Posts p WHERE p.Username = u.Username) AS PostsCount,
       (SELECT COUNT(*) FROM Follows f WHERE f.FollowedUsername = u.Username) AS NumFollowers,
       (SELECT COUNT(*) FROM Follows f WHERE f.FollowerUsername = u.Username) AS NumFollowing
FROM Users u;