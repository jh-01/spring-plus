package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.example.expert.global.config.QueryDSLConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.user.enums.UserRole.USER;

@Import(QueryDSLConfig.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 대용량_유저_생성() {
        List<User> users = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000000; i++) {
            User user = new User("user" + i + "@gmail.com", "asdf1234!", USER, "user" + i);
            users.add(user);

            if (users.size() >= 1000) {
                userRepository.saveAll(users);
                users.clear();
            }
        }
        if (!users.isEmpty()) {
            userRepository.saveAll(users);
        }

        long end = System.currentTimeMillis();
        System.out.println("소요 시간(ms): " + (end - start));
    }

    @Test
    public void 기본_아이디_검색(){
        long targetID = 12;

        long start = System.currentTimeMillis();
        Optional<User> user = userRepository.findById(targetID);
        long end = System.currentTimeMillis();

        System.out.println("기본_아이디_검색 소요 시간(ms): " + (end - start));
    }

    @Test
    public void 기본_닉네임_검색(){
        String targetNickname = "user12";

        long start = System.currentTimeMillis();
        Optional<User> user = userRepository.findByNickname(targetNickname);
        long end = System.currentTimeMillis();

        System.out.println("기본_닉네임_검색 소요 시간(ms): " + (end - start));
    }

    @Test
    public void 기본_이메일_검색(){
        String targetEmail = "user12@gmail.com";

        long start = System.currentTimeMillis();
        Optional<User> user = userRepository.findByEmail(targetEmail);
        long end = System.currentTimeMillis();

        System.out.println("기본_이메일_검색 소요 시간(ms): " + (end - start));
    }
}