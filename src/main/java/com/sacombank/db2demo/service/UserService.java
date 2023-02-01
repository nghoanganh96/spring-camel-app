package com.sacombank.db2demo.service;

import com.sacombank.db2demo.entity.user.User;
import com.sacombank.db2demo.model.request.UserRequest;
import com.sacombank.db2demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getOneUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(UserRequest request) {
        User entity = User.builder()
                .cifId(request.getCifId())
                .moreInfo(request.getMoreInfo())
                .build();

        return userRepository.save(entity);
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
