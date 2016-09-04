CREATE TABLE rstr_account.accounts (
  account_id INTEGER PRIMARY KEY  ,
  name TEXT NOT NULL
);

CREATE TABLE rstr_account.twitter_accounts (
  twitter_id INTEGER PRIMARY KEY ,
  account_id INTEGER NOT NULL REFERENCES rstr_account.accounts(account_id)
);
