package com.sacombank.db2demo.repository.card;

import com.sacombank.db2demo.entity.card.CardInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInformationRepository extends JpaRepository<CardInformation, Long> {
}
