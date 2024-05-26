package org.dacs.quackstagramdatabase.database;

import lombok.Getter;

@Getter
public class QueryConfig {
    public static String moreThanXFollowersQuery = "SELECT * FROM Users WHERE NumFollowers > ?;";
    public static String userNumberOfPosts = "SELECT PostsCount FROM Users WHERE Username = ?;";
    public static String commentsOnUserXPost = "SELECT * FROM Comments WHERE PostID = ?;";
    public static String topXMostLikedPosts = "SELECT * FROM Posts ORDER BY NumLikes DESC LIMIT ?;";
    public static String numberOfPostsLikedByUser = "SELECT Username, COUNT(PostID) AS NumberOfLikedPosts FROM Likes GROUP BY Username;";
    public static String usersWithoutPosts = "SELECT * FROM Users WHERE PostsCount = 0;";
    public static String usersWhoFollowEachOther = "SELECT f1.FollowerUsername AS User1, f1.FollowedUsername AS User2 FROM Follows f1 JOIN Follows f2 ON f1.FollowerUsername = f2.FollowedUsername AND f1.FollowedUsername = f2.FollowerUsername;";
    public static String userWithHighestPosts = "SELECT * FROM Users ORDER BY PostsCount DESC LIMIT 1;";
    public static String topXUsersWithMostFollowers = "SELECT * FROM Users ORDER BY NumFollowers DESC LIMIT ?;";
    public static String postsLikedByAllUsers = "SELECT PostID FROM Likes GROUP BY PostID HAVING COUNT(DISTINCT Username) = (SELECT COUNT(*) FROM Users);";
    public static String mostActiveUser = "SELECT Username FROM Users ORDER BY (SELECT COUNT(*) FROM Likes WHERE Likes.Username = Users.Username) + (SELECT COUNT(*) FROM Comments WHERE Comments.Username = Users.Username) + PostsCount DESC LIMIT 1;";
    public static String averageLikesPerUserPost = "SELECT u.Username, COALESCE(AVG(p.NumLikes), 0) AS AverageLikesPerPost FROM Users u LEFT JOIN Posts p ON u.Username = p.Username GROUP BY u.Username;";
    public static String postsWithMoreCommentsThanLikes = "SELECT * FROM Posts WHERE NumComments > NumLikes;";
    public static String usersWhoLikedAllPostsOfUserX = "SELECT U.Username FROM Users U WHERE NOT EXISTS (SELECT P.PostID FROM Posts P WHERE P.Username = ? AND NOT EXISTS (SELECT 1 FROM Likes L WHERE L.Username = U.Username AND L.PostID = P.PostID));";
    public static String mostPopularPostOfEachUser = "SELECT P.Username, P.PostID, P.NumLikes FROM Posts P JOIN (SELECT Username, MAX(NumLikes) AS MaxLikes FROM Posts GROUP BY Username) AS MaxLikesPerUser ON P.Username = MaxLikesPerUser.Username AND P.NumLikes = MaxLikesPerUser.MaxLikes;";
    public static String userWithHighestFollowersFollowingRatio = "SELECT Username, MAX(COALESCE(NumFollowers / NULLIF(NumFollowing, 0), 0)) AS MaxRatio FROM Users;";
    public static String monthWithHighestPosts = "SELECT EXTRACT(YEAR_MONTH FROM PostDate) AS YearMonth, COUNT(*) AS NumberOfPosts FROM Posts GROUP BY YearMonth ORDER BY NumberOfPosts DESC LIMIT 1;";
    public static String usersNotInteractingWithUserXPosts = "SELECT U.Username FROM Users U WHERE NOT EXISTS (SELECT P.PostID FROM Posts P WHERE P.Username = ? AND NOT EXISTS (SELECT 1 FROM Likes L WHERE L.Username = U.Username AND L.PostID = P.PostID) AND NOT EXISTS (SELECT 1 FROM Comments C WHERE C.Username = U.Username AND C.PostID = P.PostID));";
    public static String userWithGreatestIncreaseInFollowersLastXDays = "SELECT u.Username, u.NumFollowers - COALESCE(f.NumFollowersBefore, 0) AS FollowersIncrease FROM Users u LEFT JOIN (SELECT f1.FollowedUsername, COUNT(*) AS NumFollowersBefore FROM Follows f1 WHERE f1.FollowDate > DATE_SUB(CURDATE(), INTERVAL ? DAY) GROUP BY f1.FollowedUsername) f ON u.Username = f.FollowedUsername ORDER BY FollowersIncrease DESC LIMIT 1;";
    public static String usersFollowedByMoreThanXPercent = "SELECT U.Username FROM Users U WHERE NumFollowers > (SELECT 0.01 * ? * COUNT(*) FROM Users);";
}