CREATE TABLE account
(
    acctno      DECIMAL(8)  NOT NULL,
    acctname    VARCHAR(30) NOT NULL,
    azipcode    VARCHAR(15) NOT NULL,
    aunit       VARCHAR(15),
    astreet     VARCHAR(30) NOT NULL,
    acity       VARCHAR(30)  NOT NULL,
    astate      VARCHAR(30)  NOT NULL,
    adateopened DATE        NOT NULL,
    atype       VARCHAR(1)  NOT NULL
);

ALTER TABLE account
    ADD CONSTRAINT account_pk PRIMARY KEY (acctno);
ALTER TABLE account
    ADD CONSTRAINT account__un UNIQUE (acctname);


CREATE TABLE checking
(
    acctno     DECIMAL(8) NOT NULL,
    servicefee DECIMAL(8) NOT NULL,
    cid        DECIMAL(8) NOT NULL
);

ALTER TABLE checking
    ADD CONSTRAINT checking_pk PRIMARY KEY (acctno);

CREATE TABLE customer
(
    cid        DECIMAL(8)  NOT NULL,
    clastname  VARCHAR(30) NOT NULL,
    cfirstname VARCHAR(30) NOT NULL,
    czipcode   VARCHAR(15) NOT NULL,
    cunit      VARCHAR(15),
    cstreet    VARCHAR(30) NOT NULL,
    ccity      VARCHAR(20) NOT NULL,
    cstate     VARCHAR(20) NOT NULL
);

ALTER TABLE customer
    ADD CONSTRAINT customer_pk PRIMARY KEY (cid);


CREATE TABLE home
(
    acctno        DECIMAL(8)    NOT NULL COMMENT 'account number',
    builtyear     DECIMAL(4)    NOT NULL,
    insureacctno  DECIMAL(8)    NOT NULL,
    yearlypremium DECIMAL(7, 2) NOT NULL,
    iid           DECIMAL(8)
);


ALTER TABLE home
    ADD CONSTRAINT home_pk PRIMARY KEY (acctno);

CREATE TABLE insure_com
(
    iid      DECIMAL(8)  NOT NULL,
    iname    VARCHAR(30) NOT NULL,
    izipcode VARCHAR(15) NOT NULL,
    iunit    VARCHAR(15),
    istreet  VARCHAR(30) NOT NULL,
    icity    VARCHAR(20) NOT NULL,
    istate   VARCHAR(20) NOT NULL
);

ALTER TABLE insure_com
    ADD CONSTRAINT insure_com_pk PRIMARY KEY (iid);


CREATE TABLE loan
(
    acctno   DECIMAL(8)     NOT NULL,
    lrate    DECIMAL(5, 2)  NOT NULL,
    lamount  DECIMAL(10, 2) NOT NULL,
    lmonths  INTEGER        NOT NULL,
    lpayment DECIMAL(7, 2)  NOT NULL,
    ltype VARCHAR (1) NOT NULL,
    cid      DECIMAL(8)     NOT NULL
);

ALTER TABLE loan
    ADD CONSTRAINT ch_inh_loan CHECK ( ltype IN ('H', 'P', 'S') );

ALTER TABLE loan
    ADD CONSTRAINT loan_pk PRIMARY KEY (acctno);

CREATE TABLE personal
(
    acctno      DECIMAL(8)     NOT NULL,
    income      DECIMAL(10, 2) NOT NULL,
    creditscore DECIMAL(3)     NOT NULL,
    purpose TEXT NOT NULL
);



ALTER TABLE personal
    ADD CONSTRAINT personal_pk PRIMARY KEY (acctno);

CREATE TABLE savings
(
    acctno       DECIMAL(8)    NOT NULL ,
    interestrate DECIMAL(5, 2) NOT NULL,
    cid          DECIMAL(8)    NOT NULL
);



ALTER TABLE savings
    ADD CONSTRAINT savings_pk PRIMARY KEY (acctno);

CREATE TABLE student
(
    acctno    DECIMAL(8) NOT NULL,
    universityname VARCHAR (30) NOT NULL,
    stuid VARCHAR (30) NOT NULL,
    gradmonth DECIMAL(2) NOT NULL,
    gradyear  DECIMAL(4) NOT NULL
);


ALTER TABLE student
    ADD CONSTRAINT student_pk PRIMARY KEY (acctno);

ALTER TABLE checking
    ADD CONSTRAINT checking_account_fk FOREIGN KEY (acctno)
        REFERENCES account (acctno);

ALTER TABLE checking
    ADD CONSTRAINT checking_customer_fk FOREIGN KEY (cid)
        REFERENCES customer (cid);

ALTER TABLE home
    ADD CONSTRAINT home_loan_fk FOREIGN KEY (acctno)
        REFERENCES loan (acctno);

ALTER TABLE home
    ADD CONSTRAINT home_insure_com_fk FOREIGN KEY (iid)
        REFERENCES insure_com (iid);

ALTER TABLE loan
    ADD CONSTRAINT loan_account_fk FOREIGN KEY (acctno)
        REFERENCES account (acctno);

ALTER TABLE loan
    ADD CONSTRAINT loan_customer_fk FOREIGN KEY (cid)
        REFERENCES customer (cid);

ALTER TABLE personal
    ADD CONSTRAINT check_credit_score CHECK (creditscore >= 0 AND creditscore <= 100);

ALTER TABLE personal
    ADD CONSTRAINT personal_loan_fk FOREIGN KEY (acctno)
        REFERENCES loan (acctno);

ALTER TABLE savings
    ADD CONSTRAINT savings_account_fk FOREIGN KEY (acctno)
        REFERENCES account (acctno);

ALTER TABLE savings
    ADD CONSTRAINT savings_customer_fk FOREIGN KEY (cid)
        REFERENCES customer (cid);

ALTER TABLE student
    ADD CONSTRAINT student_loan_fk FOREIGN KEY (acctno)
        REFERENCES loan (acctno);