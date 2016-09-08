CREATE TABLE rstr_data.resources (
  account_id INTEGER PRIMARY KEY REFERENCES rstr_account.accounts(account_id),
  money INTEGER NOT NULL,
  cc INTEGER NOT NULL
);