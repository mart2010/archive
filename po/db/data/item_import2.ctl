load data
infile 'inventaire2006labosékaly2.csv' 
into table import_data2
replace
 fields terminated by "," optionally enclosed by '"' 
TRAILING NULLCOLS
( catname, name , format, sup1itemNb, sup2itemNb, sup2, sup1, price )
