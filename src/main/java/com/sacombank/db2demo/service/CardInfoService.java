package com.sacombank.db2demo.service;

import com.sacombank.db2demo.entity.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.repository.CardInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardInfoService {
    private final CardInformationRepository cardInfoRepository;

    public CardInformation getById(Long id) {
        return cardInfoRepository.findById(id).orElse(null);
    }

    public CardInformation save(CardInfoRequest request) {
        String uuid = UUID.randomUUID().toString();
        var cardInfoEntity = CardInformation.builder()
                .uuid(uuid)
                .cifId(request.getCifId())
                .cardNumber(request.getCardNumber())
                .cardType(request.getCardType())
                .custName(request.getCustName())
                .build();

        return cardInfoRepository.save(cardInfoEntity);
    }

    public boolean delete(Long id) {
        try {
            cardInfoRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error when delete CardInfo at id {}", id, ex);
            return false;
        }
        return true;
    }
}
