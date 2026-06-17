package com.muyang.server.common;

import com.muyang.server.cart.Cart;
import com.muyang.server.cart.CartItem;
import com.muyang.server.catalog.Category;
import com.muyang.server.catalog.ImageType;
import com.muyang.server.catalog.Sku;
import com.muyang.server.catalog.SkuAttribute;
import com.muyang.server.catalog.SkuDetail;
import com.muyang.server.catalog.SkuImage;
import com.muyang.server.catalog.Promo;
import com.muyang.server.review.Review;
import com.muyang.server.wishlist.Wishlist;
import com.muyang.server.wishlist.WishlistItem;
import com.muyang.server.auth.User;
import com.muyang.server.catalog.SkuRepository;
import com.muyang.server.catalog.CategoryRepository;
import com.muyang.server.catalog.PromoRepository;
import com.muyang.server.review.ReviewRepository;
import com.muyang.server.cart.CartRepository;
import com.muyang.server.wishlist.WishlistRepository;
import com.muyang.server.auth.UserRepository;
import com.muyang.server.coupon.CouponRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.muyang.server.coupon.Coupon;
import com.muyang.server.coupon.DiscountType;
import com.muyang.server.pet.Pet;
import com.muyang.server.pet.PetRepository;
/** 인메모리 H2에 데모 데이터 적재 (고양이 장난감 중심 카탈로그) */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final SkuRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PromoRepository promoRepository;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CouponRepository couponRepository;
    private final PetRepository petRepository;
    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }
        productRepository.saveAll(List.of(
                product("p1", "깃털 낚싯대 장난감", "장난감", 5900, null, "BEST", 4.8, 1320, "깃털 낚싯대",
                        "사냥 본능을 깨우는 깃털 낚싯대. 보호자와 함께하는 놀이로 운동량과 스트레스를 한 번에 해결해요.", 1),
                product("p2", "전동 회전 깃털 놀이기", "자동완구", 23900, 28000, "신상", 4.6, 210, "전동 깃털",
                        "불규칙하게 움직이는 전동 깃털로 혼자서도 신나게. 타이머 자동 종료로 과흥분도 방지해요.", 2),
                product("p3", "캣닢 쿠션 인형 3종 세트", "캣닢", 8900, null, "인기", 4.9, 870, "캣닢 인형",
                        "북미산 프리미엄 캣닢을 가득 채운 발차기 인형 세트. 텐션 폭발 보장!", 3),
                product("p4", "마따따비 스틱", "캣닢", 4500, null, null, 4.7, 540, "마따따비",
                        "씹고 비비며 즐기는 천연 마따따비 스틱. 입맛 까다로운 냥이도 좋아해요.", 4),
                product("p5", "굴리는 노즈워크 트릿볼", "장난감", 12900, null, null, 4.7, 320, "트릿볼",
                        "간식을 넣고 굴리면 조금씩 나오는 노즈워크 볼. 식탐과 지루함을 동시에 잡아요.", 5),
                product("p6", "사각 골판지 스크래처", "스크래처", 15900, 18000, "BEST", 4.8, 1100, "스크래처",
                        "고밀도 골판지로 오래가는 스크래처. 가구 긁기를 줄여주는 필수템.", 6),
                product("p7", "터널 & 방울공 놀이터", "장난감", 19900, null, null, 4.6, 260, "터널 놀이터",
                        "바스락 터널과 방울공이 결합된 종합 놀이터. 숨고 점프하며 혼자서도 잘 놀아요.", 7),
                product("p8", "오토 레이저 포인터", "자동완구", 27000, null, "신상", 4.5, 95, "레이저 포인터",
                        "랜덤 패턴으로 움직이는 자동 레이저. 바쁜 집사를 대신해 사냥 놀이를 책임져요.", 8),
                product("p9", "동결건조 참치 트릿", "간식", 11200, null, "인기", 4.9, 980, "동결건조 참치",
                        "참치살 100% 동결건조. 기호성 최고, 놀이 보상이나 토퍼로 딱 좋아요.", 9),
                product("p10", "곡물프리 두부 모래", "위생용품", 13900, null, null, 4.8, 430, "두부 모래",
                        "먼지 적은 두부 모래. 강한 응고력과 자연 탈취로 화장실을 쾌적하게.", 10)));
        // 카테고리 (간식 제외 — 화면 카테고리 줄에서 빠짐)
        categoryRepository.saveAll(List.of(
                Category.builder().key("장난감").label("장난감").build(),
                Category.builder().key("자동완구").label("자동완구").build(),
                Category.builder().key("캣닢").label("캣닢").build(),
                Category.builder().key("스크래처").label("스크래처").build(),
                Category.builder().key("위생용품").label("위생").build()));
        promoRepository.saveAll(List.of(
                Promo.builder().title("신규회원 첫 구매").big("15% 할인").sub("가입하고 바로 받는 웰컴 쿠폰").theme("navy").build(),
                Promo.builder().title("3만원 이상 구매 시").big("무료배송").sub("오후 2시 이전 주문 당일출고").theme("cream").build(),
                Promo.builder().title("우리 냥이 취향저격").big("인기 장난감 TOP 10").sub("집사들이 다시 찾는 베스트 모음").theme("navy").build()));
        reviewRepository.saveAll(List.of(
                Review.builder().name("나비집사").pet("코숏 · 2살").rating(5).text("낚싯대 흔드니까 눈 돌아가요. 덕분에 운동량이 확 늘었어요!").build(),
                Review.builder().name("치즈마망").pet("치즈태비 · 3살").rating(5).text("스크래처 받고 소파를 안 긁어요. 진작 살걸 후회 중입니다.").build(),
                Review.builder().name("회색이네").pet("러시안블루 · 5살").rating(4).text("캣닢 인형에 텐션 폭발… 한참을 끌어안고 발차기해요 ㅎㅎ").build()));
        // 데모 계정 — demo@muyang.com / 1234
        User demo = userRepository.save(User.builder()
                .email("demo@muyang.com")
                .password(passwordEncoder.encode("1234"))
                .name("김무양")
                .role("USER")
                .grade("VIP")
                .onboarded(false)
                .createdAt(Instant.now())
                .build());
        // demo 유저의 반려동물
        petRepository.saveAll(List.of(
                Pet.builder().userId(demo.getId()).name("나비").species("코숏").age(2).build(),
                Pet.builder().userId(demo.getId()).name("치즈").species("치즈태비").age(3).build()));
        // demo 유저의 장바구니 — Cart(집계 루트) 1개 + CartItem p1×1·p6×2
        cartRepository.save(Cart.builder()
                .userId(demo.getId())
                .items(new ArrayList<>(List.of(
                        CartItem.builder().productId("p1").qty(1).build(),
                        CartItem.builder().productId("p6").qty(2).build())))
                .build());
        // demo 유저의 찜 — Wishlist 1개 + WishlistItem p3·p7
        wishlistRepository.save(Wishlist.builder()
                .userId(demo.getId())
                .items(new ArrayList<>(List.of(
                        WishlistItem.builder().productId("p3").build(),
                        WishlistItem.builder().productId("p7").build())))
                .build());
        couponRepository.saveAll(List.of(
                Coupon.builder().code("WELCOME15").name("신규회원 15% 할인")
                        .discountType(DiscountType.RATE).discountValue(15).minOrder(0).active(true).build(),
                Coupon.builder().code("SHIP3000").name("배송비 3000원 할인")
                        .discountType(DiscountType.AMOUNT).discountValue(3000).minOrder(10000).active(true).build()));
    }
    private Sku product(String id, String name, String cat, int price, Integer was, String tag,
                        double rating, int reviews, String ph, String desc, int sortOrder) {
        String code = id.toUpperCase();
        return Sku.builder()
                .id(id).name(name).cat(cat).pet("고양이")
                .price(price).was(was).tag(tag)
                .rating(rating).reviews(reviews)
                .ph(ph).desc(desc).sortOrder(sortOrder)
                .detail(SkuDetail.builder()
                        .description(name + " — " + desc + " 우리 아이의 하루를 더 건강하고 편안하게.")
                        .extraDescription("직사광선을 피해 서늘하고 건조한 곳에 보관하세요. 손상 시 즉시 교체해 주세요.")
                        .videoUrl("https://videos.muyang.example.com/" + id + ".mp4")
                        .build())
                .images(List.of(
                        image("https://placehold.co/600x600/1F2A52/F6E7A0?text=" + code, ImageType.MAIN, 0, name),
                        image("https://placehold.co/600x600/F6E7A0/1F2A52?text=" + code + "-2", ImageType.GALLERY, 0, name),
                        image("https://placehold.co/600x600/FBF3CD/1F2A52?text=" + code + "-3", ImageType.GALLERY, 1, name),
                        image("https://placehold.co/800x1200/FFFDF6/1F2A52?text=" + code + "-detail", ImageType.DETAIL, 0, name)))
                .attributes(List.of(
                        attribute("카테고리", cat, 0),
                        attribute("대상", "고양이", 1),
                        attribute("원산지", "대한민국", 2),
                        attribute("배송", "3만원 이상 무료배송", 3)))
                .build();
    }
    private SkuImage image(String url, ImageType type, int sortOrder, String alt) {
        return SkuImage.builder().url(url).type(type).sortOrder(sortOrder).alt(alt).build();
    }
    private SkuAttribute attribute(String name, String value, int sortOrder) {
        return SkuAttribute.builder().name(name).value(value).sortOrder(sortOrder).build();
    }
}