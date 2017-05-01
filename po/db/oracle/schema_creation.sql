/* Please connect as system before running this script *
 * The script assumes a tablespace 'user' exist        *
 * and the temporary tablespace called 'temp' exist    *
 */


-- create schema PROD

create user po_prod identified by po_prod
	default tablespace user
	temporary tablespace temp
	quota unlimited on user;

grant connect to po_prod;



-- create schema TEST

create user po_test identified by po_test
	default tablespace user
	temporary tablespace temp
	quota unlimited on user;

grant connect to po_test;

