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
