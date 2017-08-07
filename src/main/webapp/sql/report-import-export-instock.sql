SELECT rownum id,
  shop_name,
  goods_group_name,
  goods_code,
  goods_name,
  goods_status,
  NVL(old_balance,0) old_balance,
  NVL(import_amount,0) import_amount,
  NVL(export_amount,0) export_amount,
  NVL(new_balance,0) new_balance
FROM
  (SELECT shop_id,
    goods_id,
    goods_status,
    SUM(QUANTITY) AS new_balance
  FROM REPORT_SHOP_GOODS_STOCK
  WHERE TRUNC(REPORT_CREATED_DATE) = TRUNC(:TO_DATE) 
  and stock_status = 1
  and shop_id = :SHOP_ID
  GROUP BY shop_id,
    goods_id,
    goods_status
  ORDER BY shop_id,
    goods_id,
    goods_status
  )
full JOIN
  (SELECT shop_id,
    goods_id,
    goods_status,
    SUM(QUANTITY) AS old_balance
  FROM REPORT_SHOP_GOODS_STOCK
  WHERE TRUNC(REPORT_CREATED_DATE) = TRUNC(:FROM_DATE) 
  and stock_status = 1
  and shop_id = :SHOP_ID
  GROUP BY shop_id,
    goods_id,
    goods_status
  ORDER BY shop_id,
    goods_id,
    goods_status
  ) USING (shop_id, goods_id,goods_status)
  
full JOIN
  (SELECT --*
    t.TO_SHOP_ID shop_id,
    td.GOODS_ID,
    std.goods_status,
    SUM(td.QUANTITY) import_amount
  FROM TRANSACTION_ACTION_DETAIL td
  JOIN TRANSACTION_ACTION t
  ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID
  JOIN
    (SELECT transaction_action_id,
      goods_id,
      goods_status
    FROM STOCK_TRANSACTION_DETAIL stdd
    JOIN STOCK_TRANSACTION stt
    ON stdd.STOCK_TRANSACTION_ID       = stt.STOCK_TRANSACTION_ID
    ) std ON std.transaction_action_id = t.transaction_action_id
  AND std.goods_id                     = td.goods_id
  JOIN sm_owner.staff staff
  ON staff.STAFF_ID = t.CREATE_STAFF_ID
  AND staff.SHOP_ID = t.CREATE_SHOP_ID
  JOIN sm_owner.shop shop
  ON shop.SHOP_ID = t.TO_SHOP_ID
  WHERE lower(t.TRANSACTION_ACTION_CODE) LIKE '%' ESCAPE '/'
  AND ( ( t.TRANSACTION_TYPE = 1
  AND t.STATUS              IN (2, 6) )
  OR ( t.TRANSACTION_TYPE    > 1
  AND t.STATUS              IN (5, 10,14) ) )
  AND t.TO_SHOP_ID           = :SHOP_ID
  AND td.CREATE_DATETIME >= :FROM_DATE
  AND td.CREATE_DATETIME  < :TO_DATE
  GROUP BY t.TO_SHOP_ID,
    td.GOODS_ID,
    std.goods_status
  ) USING (shop_id, goods_id, goods_status)
full JOIN
  (SELECT t.FROM_SHOP_ID shop_id,
    td.GOODS_ID,
    std.goods_status ,
    SUM(td.QUANTITY) export_amount
  FROM TRANSACTION_ACTION_DETAIL td
  JOIN TRANSACTION_ACTION t
  ON td.TRANSACTION_ACTION_ID = t.TRANSACTION_ACTION_ID
  JOIN
    (SELECT transaction_action_id,
      goods_id,
      goods_status
    FROM STOCK_TRANSACTION_DETAIL stdd
    JOIN STOCK_TRANSACTION stt
    ON stdd.STOCK_TRANSACTION_ID       = stt.STOCK_TRANSACTION_ID
    ) std ON std.transaction_action_id = t.transaction_action_id
  AND std.goods_id                     = td.goods_id
  JOIN sm_owner.staff staff
  ON staff.STAFF_ID = t.CREATE_STAFF_ID
  AND staff.SHOP_ID = t.CREATE_SHOP_ID
  JOIN sm_owner.shop shop
  ON shop.SHOP_ID = t.FROM_SHOP_ID
  WHERE lower(t.TRANSACTION_ACTION_CODE) LIKE '%' ESCAPE '/'
  AND t.FROM_SHOP_ID      = :SHOP_ID
  AND td.CREATE_DATETIME >= :FROM_DATE
  AND td.CREATE_DATETIME  < :TO_DATE
  AND(t.TRANSACTION_TYPE IN (1,2,3,4,5,6,7,8)
  AND t.STATUS           IN (4, 5, 9, 10,14) )
  GROUP BY t.FROM_SHOP_ID,
    td.GOODS_ID,
    std.goods_status
  ) USING (shop_id, goods_id, goods_status)
JOIN shop USING (shop_id)
JOIN
  (SELECT * FROM GOODS_GROUP JOIN GOODS USING (GOODS_GROUP_ID)
  ) USING (goods_id)