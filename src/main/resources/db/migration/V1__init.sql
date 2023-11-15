CREATE TABLE bank_account
(
    id              VARCHAR(36) PRIMARY KEY,
    name            VARCHAR(16)    NOT NULL,
    pincode         VARCHAR(4)     NOT NULL,
    money           decimal(20, 2) NOT NULL default 0.00
);

CREATE TABLE money_transfer
(
    id             VARCHAR(36) PRIMARY KEY,
    data_time      TIMESTAMP      NOT NULL,
    money          decimal(20, 2) NOT NULL,
    operation_type VARCHAR(10)    NOT NULL,
    account_from VARCHAR(36) NOT NULL references bank_account (id),
    account_to   VARCHAR(36) NOT NULL references bank_account (id)
);

CREATE TABLE internal_operation
(
    id             VARCHAR(36) PRIMARY KEY,
    data_time      TIMESTAMP      NOT NULL,
    money          decimal(20, 2) NOT NULL,
    operation_type VARCHAR(10)    NOT NULL,
    bank_account_id VARCHAR(36) NOT NULL references bank_account (id)
);
