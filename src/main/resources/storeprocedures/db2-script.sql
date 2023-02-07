
/*  DB2 */

-- Store Procedure
CREATE PROCEDURE SP_INSERT_NEW_CARD_INFO(
        IN cif_id VARCHAR(255)
    , IN cust_name VARCHAR(255)
    , IN card_number VARCHAR(255)
    , IN card_type VARCHAR(255)
    , IN uuid VARCHAR(255)
    , IN created_date TIMESTAMP
    , IN modified_date TIMESTAMP
    , OUT last_id BIGINT
)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN
    INSERT INTO
        CARD_INFORMATION(CREATED_DATE, MODIFIED_DATE, CARD_NUMBER, CARD_TYPE, CIF_ID, CUST_NAME, UUID)
    VALUES (created_date, modified_date, card_number, card_type, cif_id, cust_name, uuid);
    SET last_id = IDENTITY_VAL_LOCAL();
end;


CREATE PROCEDURE SP_GET_CARD_INFO_BY_ID ( IN card_id VARCHAR(255) )
    LANGUAGE SQL
    DYNAMIC RESULT SETS 1
    READS SQL DATA
P1: BEGIN
	-- Declare cursor
	DECLARE cursor1 CURSOR WITH RETURN for
        SELECT ID, CIF_ID, CUST_NAME, CARD_NUMBER, CARD_TYPE, UUID, CREATED_DATE, MODIFIED_DATE
        FROM CARD_INFORMATION
        WHERE ID = card_id;
    -- Cursor left open for client application
    OPEN cursor1;
END P1;


CREATE PROCEDURE SP_GET_ALL_CARD_INFO ()
    DYNAMIC RESULT SETS 1
    LANGUAGE SQL
    READS SQL DATA
P1: BEGIN
	-- Declare cursor
	DECLARE cursor1 CURSOR WITH RETURN for
        SELECT * FROM CARD_INFORMATION ;
    OPEN cursor1;

END P1;


