-- スキーマ作成
CREATE SCHEMA rstr_account;
CREATE SCHEMA rstr_data;
CREATE EXTENSION btree_gist;

-- スキーマ管理ロール
CREATE ROLE rstr_admin
NOSUPERUSER
NOCREATEDB
NOCREATEROLE
LOGIN ENCRYPTED PASSWORD 'sXMYq7ez5fZZnstyXcEkLpYdhfmW37Ud'
NOREPLICATION
VALID UNTIL '2999/12/31'
NOINHERIT;

GRANT
ALL ON SCHEMA rstr_account, rstr_data
TO rstr_admin
WITH GRANT OPTION;

-- スキーマ参照ロール
CREATE ROLE rstr_users
ADMIN rstr_admin
NOSUPERUSER
NOCREATEDB
NOCREATEROLE
NOLOGIN
NOREPLICATION
VALID UNTIL '2999/12/31'
NOINHERIT;
