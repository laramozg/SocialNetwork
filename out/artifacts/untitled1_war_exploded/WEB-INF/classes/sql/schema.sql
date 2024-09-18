drop table if exists posts;
drop table if exists profile_games;
drop table if exists games;
drop table if exists profiles;
drop table if exists users;

CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY,
                                  username VARCHAR(50) NOT NULL,
                                  password VARCHAR(100) NOT NULL
    );


CREATE TABLE IF NOT EXISTS profiles (id SERIAL PRIMARY KEY,
                                     first_name VARCHAR(255),
                                     last_name VARCHAR(255),
                                     email VARCHAR(255),
                                     user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS games (id SERIAL PRIMARY KEY,
                                  title VARCHAR(255),
                                  genre VARCHAR(255)
    );
CREATE TABLE IF NOT EXISTS profile_games (profile_id INTEGER REFERENCES profiles(id) ON DELETE CASCADE,
                                          game_id INTEGER REFERENCES games(id) ON DELETE CASCADE,
                                          PRIMARY KEY (profile_id, game_id)
    );

CREATE TABLE IF NOT EXISTS posts (id SERIAL PRIMARY KEY,
                                  profile_id INTEGER REFERENCES profiles(id) ON DELETE CASCADE,
                                  content TEXT,
                                  created_at TIMESTAMP
    );