 --Create the temporary table to load from
create table import_data2(
		catname varchar2(100),
		name varchar2(100),
		format varchar2(50),
		sup1itemNb varchar2(50),
		sup2itemNb varchar2(50),
		sup2 varchar2(50),
		sup1 varchar2(50),
		price Decimal(10,2),
		primary key (name)
);



//here we populate the table "import_data2"
-- ......


//now we populate the other tables

--Supplier

Insert into po_supplier(id,name,createdBy,createddate) 
select hibernate_sequence.NEXTVAL, sup, 'mouellet', SYSDATE
from 
(Select sup from
	(select distinct sup1 as sup
	 from import_data2
	 Union
	 select distinct sup2 as sup
	 from import_data2)
 where sup is not null
)
/



--Category

Insert into po_category(id,name,createdBy,createddate) 
select hibernate_sequence.NEXTVAL,cat,'mouellet',SYSDATE
from 
(Select distinct catname as cat
 from import_data2
)
/

--Product
Insert into po_product(id,name,createdBy,createddate,category_id) 
Select hibernate_sequence.NEXTVAL,'General -' || c.name,'mouellet',SYSDATE,c.id
from po_category c
/

--item
insert into po_item(id,createdBy,createdDate,listprice_currency,version,product_id,name,format,listprice_amt)
select hibernate_sequence.NEXTVAL, 'mouellet', SYSDATE, 'CAD', 0, p.id, d.name, d.format, nvl(d.price,0)
from  import_data2 d, po_category c, po_product p
where d.catname = c.name
and p.category_id = c.id
/

--po_item_supplied

--sup1:
insert into po_item_supplied(item_supplied_id,item_id,createdDate,supplier_id,catalogNumber)
select hibernate_sequence.NEXTVAL, i.id , SYSDATE, s.id, d.sup1itemNb
from import_data2 d, po_supplier s, po_item i
where d.sup1 = s.name
and i.name = d.name
/


--sup2:
insert into po_item_supplied(item_supplied_id,item_id,createdDate,supplier_id,catalogNumber)
select hibernate_sequence.NEXTVAL, i.id , SYSDATE, s.id, d.sup1itemNb
from import_data2 d, po_supplier s, po_item i
where d.sup2 = s.name
and i.name = d.name
and d.sup2 is not null
/



commit;

