package com.sacombank.db2demo.service;

import com.sacombank.db2demo.entity.user.User;
import com.sacombank.db2demo.model.ObjectResponse;
import com.sacombank.db2demo.model.request.UserRequest;
import com.sacombank.db2demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ObjectResponse getOneUser(Long id) {
        var optionalUser = userRepository.findById(id);
        return optionalUser.map(ObjectResponse::buildSuccess).orElseGet(() -> ObjectResponse.buildFailed("Cannot found User with id = " + id));
    }

    public ObjectResponse spGetOneUser(Long id) {
        var optionalUser = userRepository.spGetUserById(id);
        return optionalUser.map(ObjectResponse::buildSuccess).orElseGet(() -> ObjectResponse.buildFailed("Cannot found User with id = " + id));
    }

    public ObjectResponse save(UserRequest request) {
        User entity = User.builder()
                .cifId(request.getCifId())
                .moreInfo(request.getMoreInfo())
                .build();

        return ObjectResponse.buildSuccess(userRepository.save(entity));
    }

    @Transactional
    public ObjectResponse saveWithStoreProc(UserRequest request) {
        try {
            long insertId = userRepository.spInsertNewUser(request.getCifId(), request.getMoreInfo());
            log.info("Call Store procedure {}. Return id of new user {}", "insertNewUser", insertId);

            return this.spGetOneUser(insertId);

        } catch (Exception ex) {
            log.error("Error in saveWithStoreProc: ", ex);
            return ObjectResponse.buildFailed(ex.getMessage());
        }
    }

    public boolean delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error when delete user at id {}", id, ex);
            return false;
        }
        return true;
    }
}
