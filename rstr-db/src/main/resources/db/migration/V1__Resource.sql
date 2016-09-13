CREATE TABLE rstr_data.resources (
  account_id INTEGER PRIMARY KEY REFERENCES rstr_account.accounts(account_id),
  money INTEGER NOT NULL,
  cc INTEGER NOT NULL
);

CREATE TABLE rstr_stamp.default_gain_logs (
  account_id INTEGER PRIMARY KEY REFERENCES rstr_account.accounts(account_id),
  last_update TIMESTAMPTZ NOT NULL
);