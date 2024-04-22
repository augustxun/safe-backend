CREATE TABLE customer
(
    id        bigint auto_increment comment 'id' primary key,
    lastName  VARCHAR(30)       NOT NULL,
    firstName VARCHAR(30)       NOT NULL,
    zipcode   VARCHAR(15)       NOT NULL,
    unit      VARCHAR(15),
    street    VARCHAR(30)       NOT NULL,
    city      VARCHAR(20)       NOT NULL,
    isDelete  tinyint default 0 not null comment '是否删除',
    state     VARCHAR(20)       NOT NULL,
    userId    bigint
) comment '客户';
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userPhone    varchar(30)                            null comment '用户手机号',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    customerId   BIGINT                                 NULL COMMENT 'customer id'
) comment '用户' collate = utf8mb4_unicode_ci;

ALTER TABLE user
    ADD CONSTRAINT user__un UNIQUE (userAccount);

ALTER TABLE customer
    ADD CONSTRAINT customer_user_fk FOREIGN KEY (userId) REFERENCES user (id);


ALTER TABLE user
    ADD CONSTRAINT user_customer_fk FOREIGN KEY (customerId) REFERENCES customer (id);

CREATE TABLE account
(
    acctNo     bigint auto_increment comment 'Account Number' PRIMARY KEY,
    acctName   VARCHAR(30)                        NOT NULL,
    zipcode    VARCHAR(15)                        NOT NULL,
    unit       VARCHAR(15),
    street     VARCHAR(30)                        NOT NULL,
    city       VARCHAR(30)                        NOT NULL,
    state      VARCHAR(30)                        NOT NULL,
    dateOpened DATETIME DEFAULT CURRENT_TIMESTAMP not null comment '创建时间',
    type       VARCHAR(1)                         NOT NULL,
    isDelete   tinyint  default 0                 not null comment '是否删除',
    userId     BIGINT                             NOT NULL
) comment '账户' collate = utf8mb4_unicode_ci;
ALTER TABLE account
    ADD CONSTRAINT account__un UNIQUE (acctName);
ALTER TABLE account
    ADD CONSTRAINT account_user_fk FOREIGN KEY (userId) REFERENCES user (id);


CREATE TABLE checking
(
    acctNo     bigint auto_increment comment 'Account Number' PRIMARY KEY,
    balance    DECIMAL(12, 2) default 0 NOT NULL comment '存款',
    serviceFee DECIMAL(8, 2)            NOT NULL,
    customerId BIGINT                   NOT NULL,
    isDelete   tinyint        default 0 not null comment '是否删除'
) comment '账户' collate = utf8mb4_unicode_ci;
ALTER TABLE checking
    ADD CONSTRAINT checking_account_fk FOREIGN KEY (acctNo) REFERENCES account (acctNo);
ALTER TABLE checking
    ADD CONSTRAINT checking_customer_fk FOREIGN KEY (customerId) REFERENCES customer (id);

CREATE TABLE savings
(
    acctNo       bigint auto_increment comment 'id' primary key,
    balance      DECIMAL(12, 2) default 0 NOT NULL comment '存款',
    isDelete     tinyint        default 0 not null comment '是否删除',
    interestRate DECIMAL(5, 2)            NOT NULL,
    customerId   BIGINT                   NOT NULL
);
ALTER TABLE savings
    ADD CONSTRAINT savings_account_fk FOREIGN KEY (acctNo) REFERENCES account (acctNo);
ALTER TABLE savings
    ADD CONSTRAINT savings_customer_fk FOREIGN KEY (customerId) REFERENCES customer (id);

CREATE TABLE loan
(
    acctNo     bigint auto_increment comment 'id' primary key,
    rate       DECIMAL(5, 2)     NOT NULL,
    amount     DECIMAL(10, 2)    NOT NULL,
    months     INTEGER           NOT NULL,
    payment    DECIMAL(7, 2)     NOT NULL,
    loanType   VARCHAR(1)        NOT NULL,
    customerId BIGINT            NOT NULL,
    isDelete   tinyint default 0 not null comment '是否删除'
);
ALTER TABLE loan
    ADD CONSTRAINT loan_account_fk FOREIGN KEY (acctNo) REFERENCES account (acctNo);
ALTER TABLE loan
    ADD CONSTRAINT loan_customer_fk FOREIGN KEY (customerId) REFERENCES customer (id);
ALTER TABLE loan
    ADD CONSTRAINT ch_inh_loan CHECK ( loan.loanType IN ('H', 'P', 'S') );

CREATE TABLE insure_com
(
    id       bigint auto_increment comment 'id' primary key,
    name     VARCHAR(30)       NOT NULL,
    zipcode  VARCHAR(15)       NOT NULL,
    unit     VARCHAR(15),
    street   VARCHAR(30)       NOT NULL,
    city     VARCHAR(20)       NOT NULL,
    state    VARCHAR(20)       NOT NULL,
    isDelete tinyint default 0 not null comment '是否删除'
);

CREATE TABLE home
(
    acctNo        bigint auto_increment comment 'id' primary key,
    builtYear     DECIMAL(4)        NOT NULL,
    insureAcctNo  DECIMAL(8)        NOT NULL,
    yearlyPremium DECIMAL(7, 2)     NOT NULL,
    InsureComId   BIGINT            NOT NULL,
    isDelete      tinyint default 0 not null comment '是否删除'
);
ALTER TABLE home
    ADD CONSTRAINT home_loan_fk FOREIGN KEY (acctNo) REFERENCES loan (acctNo);
ALTER TABLE home
    ADD CONSTRAINT home_insure_com_fk FOREIGN KEY (InsureComId) REFERENCES insure_com (id);

CREATE TABLE personal
(
    acctNo      bigint auto_increment comment 'id' primary key,
    income      DECIMAL(10, 2)    NOT NULL,
    creditScore DECIMAL(5, 2)     NOT NULL,
    purpose     TEXT              NOT NULL,
    isDelete    tinyint default 0 not null comment '是否删除'
);
ALTER TABLE personal
    ADD CONSTRAINT check_credit_score CHECK (creditscore >= 0 AND creditscore <= 100);
ALTER TABLE personal
    ADD CONSTRAINT personal_loan_fk FOREIGN KEY (acctNo) REFERENCES loan (acctNo);

CREATE TABLE student
(
    acctNo         bigint auto_increment comment 'id' primary key,
    universityName VARCHAR(30)       NOT NULL,
    stuId          VARCHAR(30)       NOT NULL,
    gradMonth      DECIMAL(2)        NOT NULL,
    gradYear       DECIMAL(4)        NOT NULL,
    isDelete       tinyint default 0 not null comment '是否删除'
);
ALTER TABLE student
    ADD CONSTRAINT student_loan_fk FOREIGN KEY (acctNo) REFERENCES loan (acctNo);



