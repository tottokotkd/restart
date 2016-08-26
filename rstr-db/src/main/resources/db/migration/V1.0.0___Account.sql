CREATE TABLE accounts (
  account_id INTEGER PRIMARY KEY  ,
  name TEXT NOT NULL
);

CREATE TABLE twitter_accounts (
  twitter_id INTEGER PRIMARY KEY ,
  account_id INTEGER NOT NULL REFERENCES accounts(account_id)
);
