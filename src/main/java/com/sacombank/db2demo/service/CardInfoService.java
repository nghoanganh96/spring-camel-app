package com.sacombank.db2demo.service;

import com.google.gson.Gson;
import com.sacombank.db2demo.entity.card.CardInformation;
import com.sacombank.db2demo.entity.user.User;
import com.sacombank.db2demo.model.ObjectResponse;
import com.sacombank.db2demo.model.request.CardInfoRequest;
import com.sacombank.db2demo.model.request.UserCardInfoRequest;
import com.sacombank.db2demo.model.request.UserRequest;
import com.sacombank.db2demo.repository.card.CardInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardInfoService {
    private final CardInformationRepository cardInfoRepository;
    private final UserService userService;
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

    public boolean delete(Long id) {
        try {
            cardInfoRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error when delete CardInfo at id {}", id, ex);
            return false;
        }
        return true;
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

    // parameter as String. Message camel auto-convert Object into String at Body in Exchange camel.
    // return String. Message Camel Activemq should be a String type.
    @Transactional
    public String addCardInfoByUser(String body) {
        if (Strings.isBlank(body)) {
            return gson.toJson(ObjectResponse.buildFailed("Message Request is null or blank"));
        }

        try {
            UserCardInfoRequest request = gson.fromJson(body, UserCardInfoRequest.class);

            // get User info from DB
            var userOR = userService.getOneUser(request.getUserId());
            if (userOR.getCode() == -1) {
                return gson.toJson(userOR);
            }
            // extract cifid from user and process the save cardinfo
            User user = (User) userOR.getData();
            String uuid = UUID.randomUUID().toString();
            var cardInfoEntity = CardInformation.builder()
                    .uuid(uuid)
                    .cifId(user.getCifId()) // extract cifid from user
                    .cardNumber(request.getCardNumber())
                    .cardType(request.getCardType())
                    .custName(request.getCustName())
                    .build();

            var response = cardInfoRepository.save(cardInfoEntity);
            return gson.toJson(ObjectResponse.buildSuccess(response));
        } catch (Exception ex) {
            log.error("Error in addCardInfoByUser: ", ex);
            return gson.toJson(ObjectResponse.buildFailed(ex.getMessage()));
        }
    }

    @Transactional
    public ObjectResponse getOneCardInfoWithSP(Long idRequest) {
        var response = cardInfoRepository.spGetCardInfoById(idRequest);

        return response.map(ObjectResponse::buildSuccess).orElseGet(() -> ObjectResponse.buildFailed("Cannot found CardInfo with id = " + idRequest));
    }

    @Transactional
    public String addCardInfoAndUserWithSP(String body) {
        if (Strings.isBlank(body)) {
            return gson.toJson(ObjectResponse.buildFailed("Message Request is null or blank"));
        }

        try {
            UserCardInfoRequest request = gson.fromJson(body, UserCardInfoRequest.class);

            // save user
            var userRequest = UserRequest.builder()
                    .cifId(request.getCifId())
                    .moreInfo(request.getMoreInfo())
                    .build();
            var userResponse = userService.saveWithStoreProc(userRequest);

            if (userResponse.getCode() == -1) {
                throw new Exception("Save User has error: %s" + userResponse.getMessage());
            }

            // extract cifid from user and process the save cardinfo
            User user = (User) userResponse.getData();
            String uuid = UUID.randomUUID().toString();

            long newId = cardInfoRepository.spSaveCardInfo(
                    user.getCifId()
                    , request.getCustName()
                    , request.getCardNumber()
                    , request.getCardType()
                    , uuid
                    , LocalDateTime.now()
                    , LocalDateTime.now());

            return gson.toJson(getOneCardInfoWithSP(newId));
        } catch (Exception ex) {
            log.error("Error in addCardInfoAndUserWithSP: ", ex);
            return gson.toJson(ObjectResponse.buildFailed(ex.getMessage()));
        }
    }
}
