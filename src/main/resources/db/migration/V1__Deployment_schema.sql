CREATE TABLE IF NOT EXISTS underflowuser(
  userid SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE ,
  password VARCHAR(50) ,
  email VARCHAR(100),
  banned boolean ,
  permission VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tag(
  tagid SERIAL PRIMARY KEY,
  title VARCHAR(200) UNIQUE
);

CREATE TABLE IF NOT EXISTS posttype(
  posttypeid SERIAL PRIMARY KEY,
  title VARCHAR(200) UNIQUE
);

CREATE TABLE IF NOT EXISTS post(
  postid SERIAL PRIMARY KEY,
  posttypeid INTEGER REFERENCES posttype(posttypeid) ON DELETE CASCADE ON UPDATE CASCADE,
  authorid INTEGER REFERENCES underflowuser(userid) ON DELETE CASCADE ON UPDATE CASCADE,
  parentid INTEGER REFERENCES post(postid) ON DELETE CASCADE ON UPDATE CASCADE,
  title VARCHAR(200),
  body VARCHAR(4000),
  creationdate DATE
);

CREATE TABLE IF NOT EXISTS post_tag(
  posttagid SERIAL PRIMARY KEY,
  postid INTEGER REFERENCES post(postid) ON DELETE CASCADE ON UPDATE CASCADE,
  tagid INTEGER REFERENCES tag(tagid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS vote(
  voteid SERIAL PRIMARY KEY,
  userid INTEGER REFERENCES underflowuser(userid) ON DELETE CASCADE ON UPDATE CASCADE,
  postid INTEGER REFERENCES post(postid) ON DELETE CASCADE ON UPDATE CASCADE,
  upvote boolean
);

create unique index vote_postid_userid_uindex on vote (postid, userid);
create unique index post_tag_postid_tagid_uindex on post_tag (postid, tagid);
