SELECT *
FROM safe_db.account a,
     safe_db.checking c,
     safe_db.user u
where a.acctNo = c.acctNo
  AND a.userId = u.id;

# 2.嵌套子查询
SELECT *
FROM account
WHERE userId in (SELECT id FROM user);

# 3.关联子查询
SELECT id,
       userName,
       (SELECT COUNT(*) FROM account a WHERE u.id = a.userId AND a.isDelete = 0)
FROM user u;

# 4.SET 运算符查询
SELECT c.balance
FROM checking c
UNION
SELECT s.balance
FROM savings s;

# 5.内联子查询
SELECT userName, userPhone
FROM (SELECT id, useraccount,  username,  userphone, city, state
      FROM user u,
           account a
      WHERE u.id = a.userId
        AND a.type = 'C'
        AND a.isDelete = 0) AS inline_view;



# 排序查询
SELECT * FROM savings ORDER BY balance DESC LIMIT 5;