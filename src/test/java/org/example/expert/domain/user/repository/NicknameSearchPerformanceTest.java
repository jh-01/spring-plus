package org.example.expert.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DataJpaTest
@Import(NicknameSearchPerformanceTest.TestConfig.class)  // JPAQueryFactory 빈 등록을 위해 Import
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NicknameSearchPerformanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager em;

    static final String TARGET_NICKNAME = "user99999";

    @BeforeAll
    static void banner() {
        System.out.println("닉네임 검색 성능 테스트 시작 (인덱스 유무 비교)");
    }

    @Test
    @Order(1)
    @Transactional
    @Rollback(false)
    void 대용량_유저_생성() {
        System.out.println("유저 100,000명 생성 중...");

        for (int i = 0; i < 100_000; i++) {
            User user = new User(
                    "user" + i + "@gmail.com",
                    "password!",
                    UserRole.USER,
                    "user" + i
            );
            userRepository.save(user);

            if (i % 1000 == 0) {
                em.flush();
                em.clear();
            }
        }

        System.out.println("생성 완료");
    }

    @Test
    @Order(2)
    void 인덱스_유무_확인() {
        System.out.println("🔍 현재 인덱스 상태 확인:");

        List<Map<String,Object>> result = jdbcTemplate.queryForList("SHOW INDEX FROM users WHERE Column_name = 'nickname'");

        if (result.isEmpty()) {
            System.out.println("인덱스 없음");
        } else {
            for (Map<String, Object> row : result) {
                System.out.printf("인덱스 이름: %s, 컬럼: %s, 유니크: %s%n",
                        row.get("Key_name"),
                        row.get("Column_name"),
                        ((Integer)row.get("Non_unique") == 0 ? "예" : "아니오"));
            }
        }
    }


    @Test
    @Order(3)
    void 인덱스_사용_여부_EXPLAIN_분석() {
        System.out.println("🔬 EXPLAIN 결과 확인:");

        List<Map<String,Object>> result = jdbcTemplate.queryForList("EXPLAIN SELECT * FROM users WHERE nickname = ?", TARGET_NICKNAME);

        if (result.isEmpty()) {
            System.out.println("EXPLAIN 결과 없음");
        } else {
            for (Map<String, Object> row : result) {
                System.out.printf("type=%s, key=%s, rows=%s%n",
                        row.get("type"),
                        row.get("key"),
                        row.get("rows"));
            }
        }
    }


    @Test
    @Order(4)
    void 닉네임_검색_성능_테스트() {
        System.out.println("닉네임 검색 속도 테스트 (반복 10회):");
        long total = 0;

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            Optional<User> result = userRepository.findByNickname(TARGET_NICKNAME);
            long end = System.nanoTime();

            long elapsed = (end - start) / 1_000_000;
            total += elapsed;
            System.out.printf("[%d회차] 소요 시간: %dms → 존재 여부: %s%n", i + 1, elapsed, result.isPresent());
        }

        long average = total / 10;
        System.out.printf("평균 검색 시간: %dms%n", average);
    }


    // QueryDSL JPAQueryFactory 빈을 등록하는 TestConfig
    @TestConfiguration
    static class TestConfig {

        @PersistenceContext
        private EntityManager em;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(em);
        }
    }
}
