package org.example.expert.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.example.expert.domain.user.dto.response.QUserResponse;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class QUserRepositoryImpl implements QUserRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public User findUserByName(String name) {
        return queryFactory.select(user)
                .from(user)
                .where(user.nickname.equalsIgnoreCase(name))
                .fetchFirst();
    }

    @Override
    public Long findIdByName(String name) {
        return queryFactory.select(user.id)
                .from(user)
                .where(user.nickname.equalsIgnoreCase(name))
                .fetchFirst();
    }

    @Override
    public List<User> findAllByName(String name) {
        return queryFactory.select(user)
                .from(user)
                .where(user.nickname.contains(name))
                .fetch();
    }

    @Override
    public UserResponse findUserResponseByName(String name) {
        return queryFactory.select(
                new QUserResponse(
                        user.id,
                        user.email,
                        user.nickname
                )).from(user)
                .where(user.nickname.eq(name))
                .fetchOne();
    }
}
