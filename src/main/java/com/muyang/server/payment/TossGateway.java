package com.muyang.server.payment;

import com.muyang.server.payment.Payment;
import com.muyang.server.payment.PaymentProvider;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

/**
 * 토스페이먼츠 — 프론트 결제위젯으로 결제창을 띄우고,
 * 성공 리다이렉트에서 받은 paymentKey를 서버가 최종 승인(confirm).
 * 위젯 안에서 카드·카카오페이·네이버페이 등 선택 가능.
 */
@Component
public class TossGateway implements PaymentGateway {

    private final RestClient client;

    public TossGateway(
            @Value("${payment.toss.base-url}") String baseUrl,
            @Value("${payment.toss.secret-key}") String secretKey) {
        String basic = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Basic " + basic)
                .build();
    }

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.TOSS;
    }

    @Override
    public String ready(Payment payment, String orderName) {
        // 토스는 프론트 위젯이 결제창을 띄우므로 서버 준비 호출 없음
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String confirm(Payment payment, String paymentKey, Integer amount) {
        if (paymentKey == null || amount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "paymentKey와 amount가 필요합니다");
        }
        if (amount != payment.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 금액이 주문 금액과 다릅니다");
        }
        try {
            Map<String, Object> res = client.post()
                    .uri("/v1/payments/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "paymentKey", paymentKey,
                            "orderId", "muyang-" + payment.getOrderId(),
                            "amount", amount))
                    .retrieve()
                    .body(Map.class);
            return (String) res.get("paymentKey");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "토스 승인 실패: " + e.getMessage());
        }
    }
    @Override
    public void cancel(Payment payment, String reason){
        try{
            client.post()
                    .uri("/v1/payments/{paymentKey}/cancel", payment.getPgKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("cancelReason", reason))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "토스 결제 취소 실패:" + e.getMessage());
        }
    }
}