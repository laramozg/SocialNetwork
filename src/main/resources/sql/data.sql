INSERT INTO users (username, password) VALUES
                                           ('user1', 'password1'),
                                           ('user2', 'password2'),
                                           ('user3', 'password3');

INSERT INTO profiles (first_name, last_name, email, user_id) VALUES
                                                                 ('John', 'Doe', 'john.doe@example.com', 1),
                                                                 ('Jane', 'Smith', 'jane.smith@example.com', 2),
                                                                 ('Alice', 'Johnson', 'alice.johnson@example.com', 3);
INSERT INTO games (title, genre) VALUES
                                     ('Chess', 'Strategy'),
                                     ('Football', 'Sports'),
                                     ('The Witcher', 'RPG');


INSERT INTO profile_games (profile_id, game_id) VALUES
                                                    (1, 1),
                                                    (1, 2),
                                                    (2, 3),
                                                    (3, 1),
                                                    (3, 2);


INSERT INTO posts (profile_id, content, created_at) VALUES
                                                        (1, 'This is my first post!', NOW()),
                                                        (2, 'Hello world!', NOW()),
                                                        (3, 'Looking forward to the weekend.', NOW());