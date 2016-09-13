-- エクステンション設定
CREATE EXTENSION btree_gist;

-- データベース管理ロール作成
CREATE ROLE rstr_admin
NOSUPERUSER
NOCREATEDB
NOCREATEROLE
LOGIN ENCRYPTED PASSWORD 'sXMYq7ez5fZZnstyXcEkLpYdhfmW37Ud'
NOREPLICATION
VALID UNTIL '2999/12/31'
NOINHERIT;

-- データベース作成
CREATE DATABASE rstr_play;
CREATE DATABASE rstr_test;

-- adminロール権限付与
GRANT ALL ON DATABASE rstr_play TO rstr_admin;
GRANT ALL ON DATABASE rstr_test TO rstr_admin;