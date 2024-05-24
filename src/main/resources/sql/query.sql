-- Query 1: Find all users who have more than X followers
SELECT *
FROM Users
WHERE NumFollowers > X;

-- Query 2: Number of posts for a User
SELECT PostsCount
FROM Users
WHERE Username = X;

-- Query 3: Find all comments made on a particular user’s post.
SELECT *
FROM Comments
WHERE PostID = X;

-- Query 4: Display the top X most liked posts.
SELECT *
FROM Posts
ORDER BY NumLikes DESC
LIMIT X;

-- Query 5: Count the number of posts each user has liked.
SELECT Username, COUNT(PostID) AS NumberOfLikedPosts
FROM Likes
GROUP BY Username;

-- Query 6: List all users who haven’t made a post yet.
SELECT *
FROM Users
WHERE PostsCount = 0;

-- Query 7: List users who follow each other.
SELECT 
    f1.FollowerUsername AS User1, 
    f1.FollowedUsername AS User2
FROM
    Follows f1
    JOIN Follows f2
ON 
    f1.FollowerUsername = f2.FollowedUsername
    AND f1.FollowedUsername = f2.FollowerUsername;

-- Query 8: Show the user with the highest number of posts.
SELECT *
FROM Users
ORDER BY PostsCount DESC
LIMIT 1;

-- Query 9: List the top X users with the most followers.
SELECT *
FROM Users
ORDER BY NumFollowers DESC
LIMIT X;

-- Query 10: Find posts that have been liked by all users.
SELECT PostID
FROM Likes
GROUP BY PostID
HAVING COUNT(DISTINCT Username) = (SELECT COUNT(*) FROM Users);

-- Query 11: Display the most active user (based on posts, comments, and likes).
SELECT Username
FROM Users
ORDER BY 
    (SELECT COUNT(*) FROM Likes WHERE Likes.Username = Users.Username) + 
    (SELECT COUNT(*) FROM Comments WHERE Comments.Username = Users.Username) + 
    PostsCount DESC
LIMIT 1;

-- Query 12: Find the average number of likes per post for each user.
SELECT 
    u.Username, 
    COALESCE(AVG(p.NumLikes), 0) AS AverageLikesPerPost
FROM 
    Users u
LEFT JOIN 
    Posts p ON u.Username = p.Username
GROUP BY 
    u.Username;

-- Query 13: Show posts that have more comments than likes.
SELECT *
FROM Posts
WHERE NumComments > NumLikes;

-- Query 14: List the users who have liked every post of a specific user.
SELECT U.Username
FROM Users U
WHERE NOT EXISTS (
    SELECT P.PostID
    FROM Posts P
    WHERE P.Username = X
    AND NOT EXISTS (
        SELECT 1
        FROM Likes L
        WHERE L.Username = U.Username
        AND L.PostID = P.PostID
    )
);

-- Query 15: Display the most popular post of each user (based on likes).
SELECT
    P.Username,
    P.PostID,
    P.NumLikes
FROM
    Posts P
JOIN (
    SELECT
        Username,
        MAX(NumLikes) AS MaxLikes
    FROM
        Posts
    GROUP BY
        Username
) AS MaxLikesPerUser ON P.Username = MaxLikesPerUser.Username AND P.NumLikes = MaxLikesPerUser.MaxLikes;

-- Query 16: Find the user(s) with the highest ratio of followers to following.
SELECT 
    Username, 
    MAX(COALESCE(NumFollowers / NULLIF(NumFollowing, 0), 0)) AS MaxRatio
FROM Users;

-- Query 17: Show the month with the highest number of posts made.
SELECT EXTRACT(YEAR_MONTH FROM PostDate) AS YearMonth, COUNT(*) AS NumberOfPosts
FROM Posts
GROUP BY YearMonth
ORDER BY NumberOfPosts DESC
LIMIT 1;

-- Query 18: Identify users who have not interacted with a specific user’s posts.
SELECT U.Username
FROM Users U
WHERE NOT EXISTS (
    SELECT P.PostID
    FROM Posts P
    WHERE P.Username = X
    AND NOT EXISTS (
        SELECT 1
        FROM Likes L
        WHERE L.Username = U.Username
        AND L.PostID = P.PostID
    )
    AND NOT EXISTS (
        SELECT 1
        FROM Comments C
        WHERE C.Username = U.Username
        AND C.PostID = P.PostID
    )
);

-- Query 19: Display the user with the greatest increase in followers in the last X days. X can be any integer value.
SELECT u.Username,
       u.NumFollowers - COALESCE(f.NumFollowersBefore, 0) AS FollowersIncrease
FROM Users u
         LEFT JOIN (
    SELECT f1.FollowedUsername,
           COUNT(*) AS NumFollowersBefore
    FROM Follows f1
    WHERE f1.FollowDate > DATE_SUB(CURDATE(), INTERVAL X DAY)
    GROUP BY f1.FollowedUsername
) f ON u.Username = f.FollowedUsername
ORDER BY FollowersIncrease DESC
LIMIT 1;




-- Query 20: Find users who are followed by more than X% of the platform users.
SELECT U.Username
FROM Users U
WHERE NumFollowers > (
    SELECT 0.01 * X * COUNT(*)
    FROM Users
);