package com.sacombank.db2demo.repository.card;

import com.sacombank.db2demo.entity.card.CardInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardInformationRepository extends JpaRepository<CardInformation, Long> {

    @Procedure(procedureName = "SP_INSERT_NEW_CARD_INFO", outputParameterName = "last_id")
    long spSaveCardInfo(@Param("cif_id") String cifId
            , @Param("cust_name") String custName
            , @Param("card_number") String cardNumber
            , @Param("card_type") String cardType
            , @Param("uuid") String uuid
            , @Param("created_date") LocalDateTime createdDate
            , @Param("modified_date") LocalDateTime modifiedDate);

    @Procedure(procedureName = "SP_GET_CARD_INFO_BY_ID")
    Optional<CardInformation> spGetCardInfoById(@Param("card_id") long id);

    @Procedure(procedureName = "SP_GET_ALL_CARD_INFO")
    List<CardInformation> spGetAllCardInfo();
}
