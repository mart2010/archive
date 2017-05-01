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



Insert into po_user(id,version,enabled,name,firstname,lastname,email,password,createdBy,createdDate)
Values (hibernate_sequence.NEXTVAL,0,1,'jfregeau','Josée', 'Frégeau','josee.fregeau.chum@ssss.gouv.qc.ca','josee','mouellet',SYSDATE);

Insert into po_user_role(user_id, role)
Values(hibernate_sequence.CURRVAL,'PO_User');

Insert into po_user_role(user_id, role)
Values(hibernate_sequence.CURRVAL,'PO_Admin');


Insert into po_user(id,version,enabled,name,firstname,lastname,email,password,createdBy,createdDate)
Values (hibernate_sequence.NEXTVAL,0,1,'mouellet','Martin', 'Ouellet','martin.ouellet@myrealbox.com','mouellet','mouellet',SYSDATE);

Insert into po_user_role(user_id, role)
Values(hibernate_sequence.CURRVAL,'PO_User');

Insert into po_user_role(user_id, role)
Values(hibernate_sequence.CURRVAL,'PO_Admin');



Insert into po_lab(id,name,description) 
Values (hibernate_sequence.NEXTVAL,'General','General');


Insert into po_user_lab(lab_id,user_id)
Select hibernate_sequence.CURRVAL,id
from po_user
Where name = 'jfregeau';

Insert into po_user_lab(lab_id,user_id)
Select hibernate_sequence.CURRVAL,id
from po_user
Where name = 'mouellet';


commit;

