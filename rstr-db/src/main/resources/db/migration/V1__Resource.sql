CREATE TABLE resource_money (
  account_id INTEGER REFERENCES rstr_account.accounts(account_id),
  money INTEGER NOT NULL ,
  last_update TIMESTAMPTZ NOT NULL
);