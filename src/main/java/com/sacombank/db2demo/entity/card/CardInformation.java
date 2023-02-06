package com.sacombank.db2demo.entity.card;

import com.sacombank.db2demo.entity.base.AuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "card_information")
public class CardInformation extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "cif_id")
    private String cifId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "cust_name")
    private String custName;

    @Column(name = "card_type")
    private String cardType;
}
