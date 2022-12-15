DROP database IF EXISTS testdb;

CREATE database testdb;

DROP TABLE files;
DROP TABLE folders;

CREATE TABLE folders (
	id SERIAL PRIMARY KEY,
	name VARCHAR(20) UNIQUE
);

CREATE TABLE files (
	id SERIAL PRIMARY KEY,
	name VARCHAR(20),
	size INTEGER,
	folder_id INTEGER REFERENCES folders ON DELETE CASCADE
);

INSERT INTO folders VALUES (DEFAULT, 'folder1');
INSERT INTO folders VALUES (DEFAULT, 'folder2');
INSERT INTO folders VALUES (DEFAULT, 'folder3');
INSERT INTO files VALUES (DEFAULT, 'file1', 10, 1);
INSERT INTO files VALUES (DEFAULT, 'file2', 20, 2);
INSERT INTO files VALUES (DEFAULT, 'file3', 30, 2);
INSERT INTO files VALUES (DEFAULT, 'file4', 40, 3);
INSERT INTO files VALUES (DEFAULT, 'file5', 50, 3);
INSERT INTO files VALUES (DEFAULT, 'file6', 60, 3);

SELECT * FROM folders;
SELECT * FROM files;
