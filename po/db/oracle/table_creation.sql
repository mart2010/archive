/* Please connect as the schema owner before running   *
 * this script.  For example connect as po_test and    *
 * run it, and do the same connecting as po_prod       *
 */

/*
drop table po_lineitem;
drop table po_user_lab;
drop table po_hist_status;
drop table po_user_role;
drop table po_item_supplied;
drop table po_supplier;
drop table po_purchase_order;
drop table po_item;
drop table po_product;
drop table po_category;
drop table po_user;
drop table po_lab;

drop sequence hibernate_sequence;
*/




create table po_user (
   id number(10,0) not null,
   version number(10,0) not null,
   password varchar2(30) not null,
   email varchar2(80) not null,
   firstName varchar2(50) not null,
   lastName varchar2(50) not null,
   phoneNumber varchar2(50),
   enabled number(1,0),
   createdDate date,
   createdBy varchar2(50),
   updatedBy varchar2(50),
   updatedDate date,
   name varchar2(50) not null unique,
   description varchar2(100),
   primary key (id)
);
create table po_lab (
   id number(10,0) not null,
   name varchar2(50) not null unique,
   description varchar2(100),
   primary key (id)
);
create table po_item (
   id number(10,0) not null,
   version number(10,0) not null,
   name varchar2(100) not null,
   description varchar2(100),
   itemNumber varchar2(50),
   distributor varchar2(50),
   format varchar2(50),
   manufacturer varchar2(50),
   product_id number(10,0) not null,
   listprice_amt Decimal(10,2),
   listprice_currency varchar2(3),
   createdBy varchar2(50),
   createdDate date,
   updatedBy varchar2(50),
   updatedDate date,
   primary key (id)
);
create table po_item_supplied (
   item_supplied_id number(10,0) not null,
   bidInfo varchar2(100),
   bidYear varchar2(4),
   catalogNumber varchar2(30),
   supplier_id number(10,0) not null,
   item_id number(10,0) not null,
   createdDate date,
   primary key (item_supplied_id)
);
create table po_supplier (
   id number(10,0) not null,
   createdDate date,
   createdBy varchar2(50),
   salesrep_contact varchar2(100),
   salesrep_name varchar2(50),
   name varchar2(50) not null unique,
   description varchar2(100),
   primary key (id)
);
create table po_user_role (
   user_id number(10,0) not null,
   role varchar2(255)
);
create table po_category (
   id number(10,0) not null,
   createdBy varchar2(50),
   createdDate date,
   name varchar2(50) not null unique,
   description varchar2(100),
   primary key (id)
);
create table po_hist_status (
   po_id number(10,0) not null,
   status_code varchar2(30),
   createdBy varchar2(50),
   statusChangeDate date,
   status_index number(10,0) not null,
   primary key (po_id, status_index)
);
create table po_purchase_order (
   po_id number(10,0) not null,
   version number(10,0) not null,
   confirmNumber varchar2(30),
   estimated_CAD_tot Decimal(10,2),
   effective_CAD_tot Decimal(10,2),
   effective_CAD_tax Decimal(10,2),
   orderFor varchar2(30),
   lab_id number(10,0) not null,
   user_id number(10,0) not null,
   project varchar2(100),
   payment_type varchar2(50),
   current_status varchar2(30),
   current_status_date date,
   openDate date,
   est_reception_date date,
   primary key (po_id)
);
create table po_product (
   id number(10,0) not null,
   category_id number(10,0) not null,
   createdBy varchar2(50),
   createdDate date,
   name varchar2(50) not null unique,
   description varchar2(100),
   primary key (id)
);
create table po_user_lab (
   user_id number(10,0) not null,
   lab_id number(10,0) not null,
   primary key (user_id, lab_id)
);
create table po_lineitem (
   po_id number(10,0) not null,
   item_id number(10,0) not null,
   quantity number(10,0) not null,
   listprice_amt Decimal(10,2),
   listprice_currency varchar2(3),
   supplier_id number(10,0) not null,
   linenum number(10,0) not null,
   primary key (po_id, linenum)
);
alter table po_item add constraint fk_item_product foreign key (product_id) references po_product;
alter table po_item_supplied add constraint fk_itemsupplied_item foreign key (item_id) references po_item;
alter table po_item_supplied add constraint fk_itemsupplied_supplier foreign key (supplier_id) references po_supplier;
alter table po_user_role add constraint fk_role_user foreign key (user_id) references po_user;
alter table po_hist_status add constraint fk_hist_po foreign key (po_id) references po_purchase_order;
alter table po_purchase_order add constraint fk_purchase_order_lab foreign key (lab_id) references po_lab;
alter table po_purchase_order add constraint fk_purchase_order_user foreign key (user_id) references po_user;
alter table po_product add constraint fk_product_category foreign key (category_id) references po_category;
alter table po_user_lab add constraint fk_lab_user foreign key (user_id) references po_user;
alter table po_user_lab add constraint fk_lab_lab foreign key (lab_id) references po_lab;
alter table po_lineitem add constraint fk_lineitem_po foreign key (po_id) references po_purchase_order;
alter table po_lineitem add constraint fk_lineitem_item foreign key (item_id) references po_item;
alter table po_lineitem add constraint fk_lineitem_supplier foreign key (supplier_id) references po_supplier;
create sequence hibernate_sequence;
