load data
infile 'inventairelabosékalytest.csv' 
into table import_data
replace
 fields terminated by "," optionally enclosed by '"' 
TRAILING NULLCOLS
( name , format, itemNb, distributor, price , supItemNb)
