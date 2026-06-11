# MuYang Server (Java · Spring Boot)

MuYang 모바일 앱의 백엔드 API. 일반적인 계층형 구조(controller → service →
repository → entity)로 구성. IntelliJ에서 이 폴더를 Open 하면 Gradle 설정이
자동으로 잡힙니다.

## 실행

```bash
./gradlew bootRun        # Windows: gradlew.bat bootRun
# → http://localhost:3000/api
```

또는 IntelliJ에서 `MuyangServerApplication` 의 main 실행.

## 구조

```
src/main/java/com/muyang/server/
├─ MuyangServerApplication.java   부트스트랩
├─ controller/    REST 엔드포인트 (Product/Catalog/Cart/Wishlist/Order/Health)
├─ service/       비즈니스 로직 (검색 필터, 장바구니 합계·무료배송, 주문 처리)
├─ repository/    Spring Data JPA 인터페이스
├─ entity/        JPA 엔티티 (Product, Category, Promo, Review,
│                 CartItem, WishlistItem, Order, OrderItem)
├─ dto/           요청/응답 레코드 (CartView, AddCartRequest 등)
└─ config/        CorsConfig, DataSeeder(데모 데이터 적재)
```

## API

| Method | Path | 설명 |
|---|---|---|
| GET | `/api/products?cat=&q=` | 상품 목록 (카테고리·검색 필터) |
| GET | `/api/products/popular` | 인기 상품 4개 |
| GET | `/api/products/{id}` | 상품 상세 |
| GET | `/api/categories` | 카테고리 |
| GET | `/api/promos` | 프로모 배너 |
| GET | `/api/reviews` | 리뷰 |
| GET | `/api/cart` | 장바구니 조회 |
| POST | `/api/cart` `{productId, qty?}` | 담기 |
| PATCH | `/api/cart/{productId}` `{qty}` | 수량 변경 (0 이하 = 삭제) |
| DELETE | `/api/cart/{productId}` | 삭제 |
| GET | `/api/wishlist` | 찜 목록 |
| POST | `/api/wishlist/{productId}/toggle` | 찜 토글 |
| GET | `/api/orders` | 주문 내역 |
| POST | `/api/orders` | 주문 생성 (장바구니 → 주문, 장바구니 비움) |
| GET | `/api/health` | 헬스 체크 |

## DB

현재 **인메모리 H2** + `DataSeeder` 로 데모 데이터를 넣습니다 (재시작 시 초기화,
단일 사용자 기준). H2 콘솔: `http://localhost:3000/h2-console`
(JDBC URL: `jdbc:h2:mem:muyang`).

실서비스 전환 시: `application.properties` 의 datasource를 MySQL/PostgreSQL로
바꾸고 `ddl-auto=validate` + 마이그레이션 도구(Flyway), 사용자 인증(Spring
Security + JWT)을 추가하세요.

> 참고: 이전 NestJS 버전 백엔드는 `../muyang-server-nest` 에 보존돼 있습니다
> (동일한 API — 필요 없으면 삭제해도 됩니다).
# muyangBE
# muyangBE
