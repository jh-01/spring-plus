package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.global.config.QueryDSLConfig;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.user.enums.UserRole.USER;

@Import(QueryDSLConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QUserRepositoryImpl qUserRepository;

    @Test
    @Order(1)
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
//
//    @Test
//    @Order(2)
//    public void 기본_아이디_검색(){
//        long targetID = 12;
//
//        long start = System.currentTimeMillis();
//        Optional<User> user = userRepository.findById(targetID);
//        long end = System.currentTimeMillis();
//
//        System.out.println("기본_아이디_검색 소요 시간(ms): " + (end - start));
//    }
//
//    @Test
//    @Order(3)
//    public void 기본_이메일_검색(){
//        String targetEmail = "user12@gmail.com";
//
//        long start = System.currentTimeMillis();
//        Optional<User> user = userRepository.findByEmail(targetEmail);
//        long end = System.currentTimeMillis();
//
//        System.out.println("기본_이메일_검색 소요 시간(ms): " + (end - start));
//    }

//    @Test
//    public void 기본_닉네임_검색(){
//        String targetNickname = "user99999";
//
//        long start = System.currentTimeMillis();
//        Optional<User> user = userRepository.findByNickname(targetNickname);
//        long end = System.currentTimeMillis();
//
//        System.out.println("기본_닉네임_검색 소요 시간(ms): " + (end - start));
//    }

//    @Test
//    public void querydsl_닉네임_단건_검색() {
//        String targetNickname = "user99999";
//
//        long start = System.currentTimeMillis();
//        User user = qUserRepository.findUserByName(targetNickname);
//        long end = System.currentTimeMillis();
//
//        System.out.println("querydsl_닉네임_단건_검색 소요 시간(ms): " + (end - start));
//    }
//
//    @Test
//    public void querydsl_닉네임_단건_검색_id_반환() {
//        String targetNickname = "user99999";
//
//        long start = System.currentTimeMillis();
//        Long id = qUserRepository.findIdByName(targetNickname);
//        long end = System.currentTimeMillis();
//
//        System.out.println("querydsl_닉네임_단건_검색_id_반환 소요 시간(ms): " + (end - start));
//    }
//
//    @Test
//    public void querydsl_닉네임_포함_검색() {
//        String targetNickname = "user99999";
//
//        long start = System.currentTimeMillis();
//        List<User> users = qUserRepository.findAllByName(targetNickname);
//        long end = System.currentTimeMillis();
//
//        System.out.println("querydsl_닉네임_포함_검색 소요 시간(ms): " + (end - start));
//    }
//
//    @Test
//    public void querydsl_userResponse_검색() {
//        String targetNickname = "user99999";
//
//        long start = System.currentTimeMillis();
//        UserResponse response = qUserRepository.findUserResponseByName(targetNickname);
//        long end = System.currentTimeMillis();
//
//        System.out.println("querydsl_userResponse_검색 소요 시간(ms): " + (end - start));
//    }

    @Test
    @Order(2)
    void 닉네임_검색_성능_테스트() {
        System.out.println("닉네임 검색 속도 테스트 (반복 10회):");
        long total = 0;

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            Optional<User> result = userRepository.findByNickname("user99999");
            long end = System.nanoTime();

            long elapsed = (end - start) / 1_000_000;
            total += elapsed;
            System.out.printf("[%d회차] 소요 시간: %dms → 존재 여부: %s%n", i + 1, elapsed, result.isPresent());
        }

        long average = total / 10;
        System.out.printf("평균 검색 시간: %dms%n", average);
    }
}