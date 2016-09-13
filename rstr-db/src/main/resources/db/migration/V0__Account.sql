CREATE TABLE rstr_account.time_zone_ids (
  zone_id SERIAL PRIMARY KEY ,
  code TEXT NOT NULL UNIQUE
);

CREATE TABLE rstr_account.accounts (
  account_id SERIAL PRIMARY KEY  ,
  name TEXT NOT NULL,
  zone_id INT NOT NULL REFERENCES rstr_account.time_zone_ids(zone_id)
);

CREATE TABLE rstr_account.twitter_accounts (
  twitter_id INTEGER PRIMARY KEY ,
  account_id INTEGER NOT NULL REFERENCES rstr_account.accounts(account_id)
);
