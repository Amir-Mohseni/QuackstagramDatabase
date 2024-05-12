create table if not exists Users
(
    Username       varchar(255)  not null
    primary key,
    Password       varchar(255)  not null,
    ProfilePicture binary(16)    not null,
    Bio            text          null,
    NumFollowers   int default 0 null,
    NumFollowing   int default 0 null,
    PostsCount     int default 0 null
    );

create table if not exists Posts
(
    PostID      binary(16)                          not null
    primary key,
    Username    varchar(255)                        null,
    Caption     text                                null,
    MediaURL    binary(16)                          null,
    PostDate    timestamp default CURRENT_TIMESTAMP null,
    NumLikes    int       default 0                 null,
    NumComments int       default 0                 null,
    constraint Posts_ibfk_1
    foreign key (Username) references Users (Username)
    );

create table if not exists Comments
(
    CommentID   binary(16)                          not null
    primary key,
    PostID      binary(16)                          null,
    Username    varchar(255)                        null,
    CommentText text                                null,
    CommentDate timestamp default CURRENT_TIMESTAMP null,
    constraint Comments_ibfk_1
    foreign key (PostID) references Posts (PostID),
    constraint Comments_ibfk_2
    foreign key (Username) references Users (Username)
    );

create index PostID
    on Comments (PostID);

create index Username
    on Comments (Username);

create index Username
    on Posts (Username);

