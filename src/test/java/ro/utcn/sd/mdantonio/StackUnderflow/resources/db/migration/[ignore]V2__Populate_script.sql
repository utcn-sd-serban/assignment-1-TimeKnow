INSERT INTO posttype (posttypeid, title) VALUES (1, 'Question') ON CONFLICT DO NOTHING;
INSERT INTO posttype (posttypeid, title) VALUES (2,  'Answer') ON CONFLICT DO NOTHING;
INSERT INTO underflowuser (userid, username, password, email, banned, permission) VALUES (1,'superuser','superuser','superuser@gmail.com',FALSE,'ADMIN') ON CONFLICT DO NOTHING;

