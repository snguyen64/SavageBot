CREATE USER 'savage'@'localhost';
SET PASSWORD FOR 'savage'@'localhost' = 'savagepassword';
GRANT ALL PRIVILEGES ON *.* TO 'savage'@'localhost' WITH GRANT OPTION;
CREATE DATABASE savagebot;
USE savagebot;

CREATE TABLE Playlists (
playlist_name VARCHAR(255),
PRIMARY KEY playlist_name_key(playlist_name)
);


CREATE TABLE Songs (
song VARCHAR(255),
playlist VARCHAR(255),
linkType VARCHAR(255),
PRIMARY KEY song_key(song),
CONSTRAINT fk_playlist FOREIGN KEY(playlist)
REFERENCES Playlists (playlist_name) ON DELETE CASCADE
)
ENGINE=InnoDB;


CREATE TABLE Compliments (
compliment VARCHAR(255),
PRIMARY KEY compliment_key(compliment)
);


CREATE TABLE Insults (
insult VARCHAR(255),
PRIMARY KEY insult_key(insult)
);

CREATE TABLE Emoticons (
emote VARCHAR(255),
type VARCHAR(255),
PRIMARY KEY emote_key(emote)
);

CREATE TABLE Leaders (
name VARCHAR(255),
PRIMARY KEY name_key(name)
);

CREATE TABLE Greetings (
user VARCHAR(255),
greeting VARCHAR(255),
PRIMARY KEY user_key(user)
);

CREATE TABLE Jokes (
joke VARCHAR(255),
PRIMARY KEY joke_key(joke)
);