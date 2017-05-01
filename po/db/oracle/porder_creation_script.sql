

CONNECT SYSTEM/manager

CREATE USER porder IDENTIFIED BY purchase101
default tablespace users 
temporary tablespace temp
quota unlimited on users;


--Grant to user porder
GRANT CONNECT, RESOURCE TO porder;
