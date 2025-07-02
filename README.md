# SPRING PLUS

---
## AWS 배포
### IAM 유저 생성

### 권한 부여
<img width="1177" alt="스크린샷 2025-06-30 오전 10 27 50" src="https://github.com/user-attachments/assets/0db343c1-b19e-45da-b177-7aa1347c8f2b" />

---

### 사용자 생성 완료
<img width="1466" alt="스크린샷 2025-06-30 오전 10 28 23" src="https://github.com/user-attachments/assets/b209aab4-3774-4024-ba8f-6c510c888c78" />

---

### IAM 유저로 로그인
<img width="386" alt="스크린샷 2025-06-30 오전 10 29 41" src="https://github.com/user-attachments/assets/11ee68b7-fa60-400c-a96d-2b79e2a81427" />

---

### EC2 인스턴스 생성
<img width="984" alt="스크린샷 2025-06-30 오전 10 32 10" src="https://github.com/user-attachments/assets/b3403b73-3276-4b61-a9b9-83d0f2a885eb" />

<img width="984" alt="스크린샷 2025-06-30 오전 10 32 16" src="https://github.com/user-attachments/assets/ed6be14e-dab6-4c38-a644-7471cd740fa0" />
<img width="984" alt="스크린샷 2025-06-30 오전 10 32 55" src="https://github.com/user-attachments/assets/9eab0ded-bd94-44c4-89f2-890e19124b4e" />
<img width="984" alt="스크린샷 2025-06-30 오전 10 34 23" src="https://github.com/user-attachments/assets/e5efd59c-5f21-4f4d-9c92-9d0b9cfe5ed2" />
<img width="488" alt="스크린샷 2025-06-30 오전 10 34 35" src="https://github.com/user-attachments/assets/d9952761-ac83-42ef-8ad1-3bafdb0d1a48" />
<img width="593" alt="스크린샷 2025-06-30 오전 10 37 15" src="https://github.com/user-attachments/assets/21a0a80d-dbd3-45d5-b5c2-30f1bb61f25c" />

---
### 보안그룹 인바운드 규칙 수정
<img width="1263" alt="스크린샷 2025-06-30 오전 10 43 12" src="https://github.com/user-attachments/assets/d2a971b9-a241-46b3-8055-2b154a9fe8d3" />
<img width="1499" alt="스크린샷 2025-06-30 오전 10 45 24" src="https://github.com/user-attachments/assets/fdbce136-1560-40af-ac54-3061bb0fc500" />

--- 
### SSH 연결 및 실행
<img width="1499" alt="스크린샷 2025-06-30 오전 10 46 35" src="https://github.com/user-attachments/assets/143876ce-640c-476a-add6-a4c06112bb93" />
<img width="573" alt="스크린샷 2025-06-30 오전 10 50 00" src="https://github.com/user-attachments/assets/8943c8c1-8535-4387-a23b-20f03bc60d46" />
<img width="1004" alt="스크린샷 2025-06-30 오후 4 14 22" src="https://github.com/user-attachments/assets/9dc04029-b83d-4c22-a6c8-8ec1f18740ec" />


---
## 유저 검색 속도 비교
### 기본 쿼리

결과
```
...
2025-07-02T19:35:07.557+09:00 DEBUG 84371 --- [           main] org.hibernate.SQL                        : 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        u1_0.nickname=?
Hibernate: 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        u1_0.nickname=?
[10회차] 소요 시간: 1ms → 존재 여부: false
평균 검색 시간: 8ms
```

---
### queryDSL 사용
#### 기본 쿼리
```
@Override
public User findUserByName(String name) {
    return queryFactory.select(user)
        .from(user)
        .where(user.nickname.eq(name))
        .fetchOne();
}
```

결과
```
2025-07-02T15:39:07.324+09:00 DEBUG 77176 --- [           main] org.hibernate.SQL                        : 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        u1_0.nickname=?
Hibernate: 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        u1_0.nickname=?
querydsl_닉네임_단건_검색 소요 시간(ms): 2

```

#### IgnoreCase & FetchFirst 사용
```
    @Override
    public User findUserByName(String name) {
        return queryFactory.select(user)
                .from(user)
                .where(user.nickname.equalsIgnoreCase(name))
                .fetchFirst();
    }
```

결과
```
2025-0702T16:37:00.678+09:00 DEBUG 79053 --- [           main] org.hibernate.SQL                        : 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        lower(u1_0.nickname)=? 
    limit
        ?
Hibernate: 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        lower(u1_0.nickname)=? 
    limit
        ?
querydsl_닉네임_단건_검색 소요 시간(ms): 14

```

---
### queryDSL 필요한 정보만 가져오기
#### UserResponse Dto 활용
```
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
```

결과
```
2025-07-02T16:35:38.314+09:00 DEBUG 79053 --- [           main] org.hibernate.SQL                        : 
    select
        u1_0.id,
        u1_0.email,
        u1_0.nickname 
    from
        users u1_0 
    where
        u1_0.nickname=?
Hibernate: 
    select
        u1_0.id,
        u1_0.email,
        u1_0.nickname 
    from
        users u1_0 
    where
        u1_0.nickname=?
querydsl_userResponse_검색 소요 시간(ms): 74
```


#### id 값만 가져오기

```
    @Override
    public Long findIdByName(String name) {
        return queryFactory.select(user.id)
                .from(user)
                .where(user.nickname.equalsIgnoreCase(name))
                .fetchFirst();
    }
```

결과
```
2025-07-02T16:55:33.362+09:00 DEBUG 79609 --- [           main] org.hibernate.SQL                        : 
    select
        u1_0.id 
    from
        users u1_0 
    where
        lower(u1_0.nickname)=? 
    limit
        ?
Hibernate: 
    select
        u1_0.id 
    from
        users u1_0 
    where
        lower(u1_0.nickname)=? 
    limit
        ?
querydsl_닉네임_단건_검색_id_반환 소요 시간(ms): 51
```

---
### 캐시 사용

오류로 연결 실패...

---

### B-Tree 인덱스
유저 테이블에 인덱스 추가
```
@Table(name = "users", indexes = @Index(name = "idx_nickname", columnList = "nickname", unique = true))
```

결과
```
...
2025-07-02T19:28:37.370+09:00 DEBUG 84192 --- [           main] org.hibernate.SQL                        : 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        u1_0.nickname=?
Hibernate: 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.nickname,
        u1_0.password,
        u1_0.user_role 
    from
        users u1_0 
    where
        u1_0.nickname=?
[10회차] 소요 시간: 3ms → 존재 여부: true
평균 검색 시간: 4ms
```

---

