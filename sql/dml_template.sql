INSERT INTO account (acctNo, acctName, zipcode, unit, street, city, state, dateOpened, type, userId)
VALUES (10001, 'Alice Wonderland Savings', '00001', 'Unit 101', 'Wonder St', 'New York', 'NY', '2022-01-01', 'C', 1);

INSERT INTO customer (id, lastName, firstName, zipcode, unit, street, city, state)
VALUES (20001, 'Wonderland', 'Alice', '00001', 'Unit 101', 'Wonder St', 'New York', 'NY'),
       (20002, 'Builder', 'Bob', '00002', 'Unit 202', 'Build Blvd', 'New Jersey City', 'NJ'),
       (20003, 'Chocolate', 'Charlie', '00003', 'Unit 303', 'Cocoa Ave', 'New York', 'NY'),
       (20004, 'Oz', 'Dorothy', '00004', 'Unit 404', 'Emerald St', 'Orlando', 'FL'),
       (20005, 'Paradise', 'Eve', '00005', 'Unit 505', 'Eden Rd', 'Los Angeles', 'CA'),
       (20006, 'Monster', 'Frankenstein', '00006', 'Unit 606', 'Gothic Pl', 'Miami', 'FL'),
       (20007, 'Travel', 'Gulliver', '00007', 'Unit 707', 'Lilliput Ln', 'Boston', 'MA'),
       (20008, 'Detective', 'Holmes', '00008', 'Unit 808', 'Baker St', 'Los Angeles', 'CA'),
       (20009, 'Knight', 'Ivanhoe', '00009', 'Unit 909', 'Chivalry Ct', 'Huston', 'TA'),
       (20010, 'Eyre', 'Jane', '00010', 'Unit 1010', 'Bronte Rd', 'Brooklyn', 'NY');

INSERT INTO savings (acctNo, interestRate, customerId)
VALUES (10001, 2.5, 20001),
       (10003, 1.8, 20003),
       (10005, 2.0, 20005),
       (10007, 1.5, 20007),
       (10009, 2.2, 20009);

INSERT INTO loan (acctNo, rate, amount, months, payment, type, customerId)
VALUES (10002, 4.5, 200000, 360, 1015.35, 'H', 20002),
       (10004, 3.5, 15000, 24, 657.90, 'P', 20004),
       (10006, 5.0, 50000, 60, 943.56, 'S', 20006),
       (10008, 4.0, 25000, 48, 632.15, 'S', 20008),
       (10010, 3.8, 30000, 36, 882.99, 'H', 20010);

INSERT INTO checking (acctNo, serviceFee, customerId)
VALUES (10011, 10, 20001),
       (10012, 12, 20003),
       (10013, 8, 20005),
       (10014, 9, 20007),
       (10015, 11, 20009);

INSERT INTO insure_com (id, name, zipcode, unit, street, city, state)
VALUES (3001, 'InsureCorp A', '95001', 'Suite A', 'Insure Lane A', 'New York', 'NY'),
       (3002, 'InsureCorp B', '95002', 'Suite B', 'Insure Lane B', 'Los Angeles', 'CA'),
       (3003, 'InsureCorp C', '95003', 'Suite C', 'Insure Lane C', 'Long Island', 'NY');

INSERT INTO student (acctNo, universityName, stuId, gradMonth, gradYear)
VALUES (10006, 'Tech University', 4001, 6, 2024),
       (10008, 'Science College', 4002, 5, 2023);

INSERT INTO personal (acctNo, income, creditScore, purpose)
VALUES (10004, 40000, 95, 'tourist');

INSERT INTO home (acctNo, builtYear, insureAcctNo, yearlyPremium, InsureComId)
VALUES (10002, 1998, 10002, 3001, 5000),
       (10010, 2005, 10010, 3002, 6000);


