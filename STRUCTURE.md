# MuYang 백엔드 구조 (도메인 패키지)

레이어별(entity/repository/service/…)이 아니라 **기능(도메인)별 패키지**로 구성돼 있어요.
한 기능을 보려면 그 도메인 폴더 하나만 열면 됩니다.

```
com.muyang.server/
├─ MuyangServerApplication.java   진입점
├─ catalog/    상품·카탈로그
├─ cart/       장바구니
├─ wishlist/   찜
├─ order/      주문
├─ payment/    결제 (PG 연동)
├─ auth/       회원·인증
├─ address/    배송지
├─ review/     리뷰
├─ point/      적립금
├─ coupon/     쿠폰
├─ common/     공통(시드·예외처리·헬스체크)
└─ security/   보안(JWT·Security 설정)
```

각 도메인 폴더 안에는 그 기능의 **Controller·Service·Repository·Entity·DTO가 한데** 모여 있습니다.

## 도메인별 구성 + 주요 API

### catalog — 상품
`Sku · SkuDetail · SkuImage · SkuAttribute · ImageType · Category · Promo`,
`SkuRepository · CategoryRepository · PromoRepository`,
`ProductService · CatalogService`, `ProductController · CatalogController`, `SkuDetailView`
- `GET /api/products` (검색·필터) · `/products/popular` · `/products/{id}` · `/products/{id}/detail`
- `GET /api/categories · /promos · /reviews`

### cart — 장바구니 (Cart 1:N CartItem)
`Cart · CartItem · CartRepository · CartService · CartController` + dto(`CartLine·CartView·AddCartRequest·QtyRequest`)
- `GET/POST /api/cart` · `PATCH/DELETE /api/cart/{productId}`

### wishlist — 찜 (Wishlist 1:N WishlistItem)
- `GET /api/wishlist` · `POST /api/wishlist/{productId}/toggle`

### order — 주문
`Order · OrderItem · OrderRepository · OrderService · OrderController`
- `GET /api/orders` · `/orders/{id}` · `POST /api/orders` (주문생성) · `/orders/{id}/cancel`

### payment — 결제
`Payment · PaymentProvider · PaymentStatus` + `PaymentGateway`(인터페이스) + `Toss/KakaoPay/NaverPay Gateway`
- `POST /api/payments/ready · /confirm` · `/payments/{id}/cancel` · `GET /payments/{id}`

### auth — 회원·인증
`User · UserRepository · AuthService · AuthController` + dto(`AuthResponse·LoginRequest·RegisterRequest·UserView`)
- `POST /api/auth/register · /login · /logout` · `GET /me` · `POST /onboarding/complete`

### address — 배송지
- `GET/POST /api/addresses` · `PUT/DELETE /api/addresses/{id}` · `POST /{id}/default`

### review — 리뷰
- `GET /api/products/{id}/reviews` · `POST /api/products/{id}/reviews` (로그인 필요)

### point — 적립금 (원장 방식)
`PointTransaction`(±내역) → 잔액 = 합계. 주문 시 1% 자동 적립.
- `GET /api/points` (잔액 + 내역)

### coupon — 쿠폰 (Coupon 마스터 + UserCoupon 발급분)
- `GET /api/coupons` (보유) · `POST /api/coupons/claim` (코드로 발급)

### common — 공통
`DataSeeder`(데모 데이터) · `GlobalExceptionHandler`(에러 응답 통일) · `HealthController`

### security — 보안
`JwtService`(토큰 발급/검증) · `JwtAuthFilter`(요청 인증) · `SecurityConfig`(권한·CORS)

## 새 기능 추가 레시피
1. 새 도메인이면 `com.muyang.server/<도메인>/` 폴더 생성
2. 그 안에 `Entity → Repository → Service → Controller (+ dto)` 추가
3. 컴포넌트 스캔 베이스가 `com.muyang.server`라 별도 등록 없이 자동 인식

## 참고
- 패키지 간 참조는 명시적 import로 연결됩니다. 같은 도메인 안은 import 없이 사용.
- 데이터는 인메모리 H2 (재시작 시 초기화, `DataSeeder`가 데모 데이터 적재).
