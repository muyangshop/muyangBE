package com.muyang.server.payment;

import com.muyang.server.payment.Payment;
import com.muyang.server.payment.PaymentProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class NaverPayGateway implements PaymentGateway{
    private final RestClient client;
    private final String chainId;

    public NaverPayGateway(
            @Value("${payment.naver.base-url}") String baseUrl,
            @Value("${payment.naver.client-id}") String clientId,
            @Value("${payment.naver.client-secret}") String clientSecret,
            @Value("${payment.naver.chain-id}") String chainId) {
        this.chainId = chainId;
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .defaultHeader("X-NaverPay-Chain-Id", chainId)
                .build();
    }
    @Override
    public PaymentProvider provider(){
        return PaymentProvider.NAVER;
    }
    @Override
    public String ready(Payment payment, String orderName){
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "네이버페이는 가맹 승인 후 사용할 수 있어요, 토스 위젯의 네이버페이를 이용해 주세요");
    }
    @Override
    public String confirm(Payment payment, String paymentId, Integer amount){
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "네이버페이는 가맹 스잉ㄴ 후 사용할 수 있어요.");
    }
    @Override
    public void cancel(Payment payment, String reason){
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "네이버페이는 가맹 승인 후 사용 가능합니다.");
    }
}
