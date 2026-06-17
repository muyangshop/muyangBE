package com.muyang.server.payment;

import com.muyang.server.payment.Payment;
import com.muyang.server.payment.PaymentProvider;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

/**
 * 카카오페이 단독 연동 — ready(결제 페이지 URL 수신) → 사용자가 승인 →
 * 리다이렉트의 pg_token으로 approve. 테스트는 CID "TC0ONETIME".
 */
@Component
public class KakaoPayGateway implements PaymentGateway {

    private final RestClient client;
    private final String cid;

    public KakaoPayGateway(
            @Value("${payment.kakao.base-url}") String baseUrl,
            @Value("${payment.kakao.secret-key}") String secretKey,
            @Value("${payment.kakao.cid}") String cid) {
        this.cid = cid;
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "SECRET_KEY " + secretKey)
                .build();
    }

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.KAKAO;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String ready(Payment payment, String orderName) {
        try {
            Map<String, Object> res = client.post()
                    .uri("/online/v1/payment/ready")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "cid", cid,
                            "partner_order_id", String.valueOf(payment.getOrderId()),
                            "partner_user_id", String.valueOf(payment.getUserId()),
                            "item_name", orderName,
                            "quantity", 1,
                            "total_amount", payment.getAmount(),
                            "tax_free_amount", 0,
                            // 앱 딥링크/웹 콜백 — 실제 주소로 교체
                            "approval_url", "muyang://payments/kakao/success",
                            "cancel_url", "muyang://payments/kakao/cancel",
                            "fail_url", "muyang://payments/kakao/fail"))
                    .retrieve()
                    .body(Map.class);
            payment.setPgKey((String) res.get("tid")); // 승인 때 필요한 거래 ID 보관
            return (String) res.get("next_redirect_app_url"); // 모바일 앱용 (웹은 next_redirect_pc_url)
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오페이 준비 실패: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String confirm(Payment payment, String pgToken, Integer amount) {
        if (pgToken == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pg_token이 필요합니다");
        }
        try {
            Map<String, Object> res = client.post()
                    .uri("/online/v1/payment/approve")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "cid", cid,
                            "tid", payment.getPgKey(),
                            "partner_order_id", String.valueOf(payment.getOrderId()),
                            "partner_user_id", String.valueOf(payment.getUserId()),
                            "pg_token", pgToken))
                    .retrieve()
                    .body(Map.class);
            return (String) res.get("tid");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오페이 승인 실패: " + e.getMessage());
        }
    }
    @Override
    public void cancel(Payment payment, String reason){
        try{
            client.post()
                    .uri("/online/v1/payment/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "cid", cid,
                            "tid", payment.getPgKey(),
                            "cancel_amount", payment.getAmount(),
                            "cancel_tax_free_amount", 0))
                    .retrieve()
                    .toBodilessEntity();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "카카오페이 취소 실패: "+ e.getMessage());
        }
    }
}