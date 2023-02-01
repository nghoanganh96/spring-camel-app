package com.sacombank.db2demo.service;

import com.google.gson.Gson;
import com.sacombank.db2demo.entity.card.CardInformation;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.repository.card.CardInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardInfoService {
    private final CardInformationRepository cardInfoRepository;
    private final Gson gson;

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

    @Transactional
    public String saveCardInfo(String body) {
        CardInfoRequest request = gson.fromJson(body, CardInfoRequest.class);

        String uuid = UUID.randomUUID().toString();
        var cardInfoEntity = CardInformation.builder()
                .uuid(uuid)
                .cifId(request.getCifId())
                .cardNumber(request.getCardNumber())
                .cardType(request.getCardType())
                .custName(request.getCustName())
                .build();

        var response = cardInfoRepository.save(cardInfoEntity);

        return gson.toJson(response);
    }

    @Transactional
    public String getOneCardInfo(String body) {
        Long idRequest = gson.fromJson(body, Long.class);
        var response = cardInfoRepository.findById(idRequest).orElse(null);

        return gson.toJson(response);
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
