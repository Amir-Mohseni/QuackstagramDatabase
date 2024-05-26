# View for top 5 most followed users
CREATE VIEW top_5_most_followed AS
SELECT u.*
FROM Users u
ORDER BY NumFollowers DESC
LIMIT 5;

# View for latest 15 posts
CREATE VIEW latest_15_posts AS
SELECT p.*
FROM Posts p
ORDER BY PostDate DESC
LIMIT 15;

# View for top 15 most liked posts
CREATE VIEW top_15_liked_posts AS
SELECT p.*
FROM Posts p
ORDER BY NumLikes DESC
LIMIT 15;