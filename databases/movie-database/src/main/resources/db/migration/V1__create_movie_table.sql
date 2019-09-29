CREATE TABLE movie
   (
     id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
     director VARCHAR(255),
     genre VARCHAR(255),
     rating INT NOT NULL,
     title VARCHAR(255),
     year INT NOT NULL
   );
