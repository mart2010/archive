--Supplier

Insert into po_supplier(id,name,createdBy,createddate) 
Values (hibernate_sequence.NEXTVAL,'vwr international','mouellet',SYSDATE);



--Category
Insert into po_category(id,name,createdBy,createddate) 
Values (hibernate_sequence.NEXTVAL,'Fourniture de laboratoire','mouellet',SYSDATE);



--Product
Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'General','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';



 --Create the temporary table to load from
create table import_data(
		name varchar2(100),
		format varchar2(50),
		itemNb varchar2(50),
		distributor varchar2(50),
		price Decimal(10,2),
		supItemNb varchar2(50),
		primary key (name)
);
		
--Create a temporary sequence for handling insert into po_item_supplied
create sequence temp;

set echo off newpage 0 space 0 pagesize 0 feed off head off trimspool on 
set linesize 1000
spool c:\temp\output.dml 



--Item 
Select 
'insert into po_item(id,createdBy,createdDate,listprice_currency,version,product_id,name,format,itemNumber,distributor,listprice_amt) ' || 
'Values (hibernate_sequence.NEXTVAL, ''mouellet'', SYSDATE, ''CAD'',0,' ||
p.id || ', ' ||
'''' || i.name        || ''', ' || 
'''' || i.format      || ''', ' || 
'''' || i.itemNb      || ''', ' || 
'''' || i.distributor || ''', ' || 
i.price ||
')' || chr(10) || '/' || chr(10) ||
'insert into po_item_supplied(item_supplied_id,item_id,createdDate,supplier_id,catalogNumber) ' ||
'Values (temp.NEXTVAL,hibernate_sequence.CURRVAL, SYSDATE, ' ||
s.id || ', ' ||
'''' || i.supItemNb || ''')' || chr(10) || '/' || chr(10) || chr(10)
from po_product p, po_category c, import_data i, po_supplier s
where 
p.category_id = c.id
and p.name = 'General'
and c.name = 'Fourniture de laboratoire'
and s.name = 'vwr international'
;	

spool off

set echo on feed on head on
@@c:\temp\output.dml

drop sequence temp;


--add new Product and link the corresponding item  
Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Bottle','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%bottle %';

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Buffer','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%Buffer %';

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Dish','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%dish %' or name like '%Dish %';


Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Filtre','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%filtre %' or name like '%fitre %';

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Flask','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%flask %' or name like '%Flask%';

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Glove','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%glove %' or name like '%Glove %';

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Needle','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%needle %' or name like '%needle %'; 


Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Pipette','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%pipette %' or name like '%Pipette %' ;


Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Plate','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%plate %' or name like '%Plate %' ;


Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Rack','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%rack %' or name like '%Racks %' ;


Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Seringue','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%seringue %' or name like '%Seringue %' ;

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Tips','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%tips %' or name like '%Tips %' ;

Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'Tube','mouellet',SYSDATE,c.id
from po_category c
where c.name = 'Fourniture de laboratoire';

Update po_item set product_id = hibernate_sequence.CURRVAL 
where name like '%tube %' or name like '%tube %' ;

commit;

