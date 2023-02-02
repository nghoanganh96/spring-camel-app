package com.sacombank.db2demo.repository.user;

import com.sacombank.db2demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Procedure(procedureName = "AddNewUser")
    long spInsertNewUser(String cifId, String moreInfo);

    @Procedure(procedureName = "GetUserById")
    Optional<User> spGetUserById(long id);

    @Procedure(procedureName = "GetAllUser")
    List<User> spGetAllUser();
}
