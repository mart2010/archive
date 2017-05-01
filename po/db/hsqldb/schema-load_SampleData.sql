/**Useful clean-up code **/
/*
delete from po_user_lab;
delete from po_user_role;
delete from po_lab;
delete from po_user;
delete from po_item_supplied;
delete from po_item;
delete from po_product;
delete from po_category;
delete from po_supplier;
*/



Insert into po_user(id,version,enabled,name,firstname,lastname,email,password)
Values (1000,0,1,'jfregeau','Josée', 'Frégeau','josee.fregeau.chum@ssss.gouv.qc.ca','josee');

Insert into po_user_role(user_id, role)
Values(1000,'PO_User');

Insert into po_user_role(user_id, role)
Values(1000,'PO_Admin');



Insert into po_user(id,version,enabled,name,firstname,lastname,email,password)
Values (100,0,1,'userSample1','FNameUser1', 'LNameUser1','user1@dot.com','sample1');

Insert into po_user(id,version,enabled,name,firstname,lastname,email,password)
Values (101,0,1,'adminSample1','FNameAdmin1', 'LNameAdmin1','martin_l_ouellet@videotron.ca','sample1');

Insert into po_user(id,version,enabled,name,firstname,lastname,email,password)
Values (102,0,1,'admin','a', 'a','admin@dot.com','a');

Insert into po_user(id,version,enabled,name,firstname,lastname,email,password)
Values (103,0,1,'user','u', 'u','user@dot.com','u');


Insert into po_user_role(user_id, role)
Values(100,'PO_User');

Insert into po_user_role(user_id, role)
Values(101,'PO_Admin');

Insert into po_user_role(user_id, role)
Values(101,'PO_User');

Insert into po_user_role(user_id, role)
Values(102,'PO_Admin');

Insert into po_user_role(user_id, role)
Values(102,'PO_User');

Insert into po_user_role(user_id, role)
Values(103,'PO_User');


Insert into po_lab(id,name,description) 
Values (100,'LabSample1','Laboratory Sample1');

Insert into po_lab(id,name,description) 
Values (101,'LabSample2','Laboratory Sample2');


Insert into po_user_lab(lab_id,user_id)
Values(100,100);

Insert into po_user_lab(lab_id,user_id)
Values(100,101);

Insert into po_user_lab(lab_id,user_id)
Values(101,101);

Insert into po_user_lab(lab_id,user_id)
Values(100,102);

Insert into po_user_lab(lab_id,user_id)
Values(100,103);




Insert into 
po_category(id,name,description) 
Values (1000,'CatA-Sample','Category A');

Insert into 
po_category(id,name,description) 
Values (1001,'CatB-Sample','Category B');


Insert into 
po_product(id,name,description,category_ID) 
Values (1000,'ProdA.1-Sample','Product A.1',1000);

Insert into 
po_product(id,name,description,category_ID) 
Values (1001,'ProdA.2-Sample','Product A.2',1000);

Insert into 
po_product(id,name,description,category_ID) 
Values (1002,'ProdA.3-Sample','Product A.3',1000);

Insert into 
po_product(id,name,description,category_ID) 
Values (1003,'ProdB.1-Sample','Product B.1',1001);

Insert into 
po_product(id,name,description,category_ID) 
Values (1004,'ProdB.2-Sample','Product B.2',1001);


Insert into 
po_item(id,name,description,listprice_amt,listprice_currency, version,product_ID) 
Values (1000,'ItemA.1.1-Sample','Item A.1.1',2.00,'CAD', 0,1000);

Insert into 
po_item(id,name,description,listprice_amt,listprice_currency, version,product_ID) 
Values (1001,'ItemA.1.2-Sample','Item A.1.2',2.00,'CAD',0,1000);

Insert into 
po_item(id,name,description,listprice_amt,listprice_currency, version,product_ID) 
Values (1002,'ItemA.2.1-Sample','Item A.2.1',2.00,'CAD',0,1001);

Insert into 
po_item(id,name,description,listprice_amt,listprice_currency, version,product_ID) 
Values (1003,'ItemB.1.1-Sample','Item B.1.1',2.00,'CAD',0,1003);

Insert into 
po_item(id,name,description,listprice_amt,listprice_currency, version,product_ID) 
Values (1004,'ItemB.1.2-Sample','Item B.1.2',2.00,'CAD',0,1003);



Insert into 
po_supplier(id,name,description) 
Values (1000,'SupplierA','SupplierA');

Insert into 
po_supplier(id,name,description) 
Values (1001,'SupplierB','SupplierB');


Insert into 
po_item_supplied(item_supplied_id,supplier_id,item_id,bidinfo) 
Values (100,1000,1000,'SupplierA_ItemA.1.1_BidInfo');

Insert into 
po_item_supplied(item_supplied_id,supplier_id,item_id,bidinfo) 
Values (101,1000,1001,'SupplierA_ItemA.1.2_BidInfo');

Insert into 
po_item_supplied(item_supplied_id,supplier_id,item_id) 
Values (102,1001,1002);

Insert into 
po_item_supplied(item_supplied_id,supplier_id,item_id) 
Values (103,1001,1003);

Insert into 
po_item_supplied(item_supplied_id,supplier_id,item_id) 
Values (104,1001,1004);

