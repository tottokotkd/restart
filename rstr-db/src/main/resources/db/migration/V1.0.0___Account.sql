CREATE TABLE accounts (
  account_id INT PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE twitter_accounts (
  twitter_id INT PRIMARY KEY ,
  account_id INT NOT NULL REFERENCES accounts(account_id)
);
