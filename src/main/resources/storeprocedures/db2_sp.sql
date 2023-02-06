/*  MYSQL */
create procedure SP_ADD_NEW_USER(
    IN cif_id varchar(255),
    IN more_info varchar(255),
    OUT last_id bigint
)
begin
insert into User (cif_id, more_info) values (cif_id, more_info);
set last_id = LAST_INSERT_ID();
end;

create procedure SP_GET_USER_BY_ID(
    IN user_id varchar(255)
)
begin
select * from User where id = user_id;
end;

create procedure SP_GET_ALL_USERS()
begin
select * from User;
end;

/*  DB2 */
create procedure SP_INSERT_NEW_CARD_INFO(
    IN cif_id varchar(255),
    IN cust_name varchar(255),
    IN card_number varchar(255),
    IN card_type varchar(255),
    IN uuid varchar(255),
    IN created_date varchar(255),
    IN modified_date varchar(255),
    OUT last_id bigint
)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN
INSERT INTO CARD_INFORMATION(CIF_ID, CUST_NAME, CARD_NUMBER, CARD_TYPE, UUID, CREATED_DATE, MODIFIED_DATE)
VALUES (cif_id, cust_name, card_number, card_type, uuid, created_date, modified_date);
SET last_id = IDENTITY_VAL_LOCAL();
end;

create procedure SP_GET_CARD_INFO_BY_ID(IN card_id varchar(255))
    dynamic result sets 1
    language sql
    reads sql data
begin
    DECLARE cur CURSOR WITH RETURN FOR
select * from CARD_INFORMATION where id = card_id;
open cur;
end;

