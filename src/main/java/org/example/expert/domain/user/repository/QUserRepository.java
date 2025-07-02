package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QUserRepository {
    public User findUserByName(String name);

    Long findIdByName(String name);

    public List<User> findAllByName(String name);
    public UserResponse findUserResponseByName(String email);
}

