package com.muyang.server.payment;

import com.muyang.server.payment.Payment;
import com.muyang.server.payment.PaymentProvider;

/** PG 연동 공통 인터페이스 — 새 PG는 이 인터페이스만 구현해 끼우면 됨 */
public interface PaymentGateway {

    PaymentProvider provider();

    /**
     * 결제 준비. 카카오/네이버처럼 PG가 결제 페이지 URL을 주는 경우 그 URL을 반환,
     * 토스 위젯처럼 프론트에서 결제창을 띄우는 경우 null 반환.
     */
    String ready(Payment payment, String orderName);

    /**
     * 최종 승인. 성공 시 PG 거래키 반환, 실패 시 예외.
     * @param pgKeyOrToken 토스=paymentKey, 카카오=pg_token
     */
    String confirm(Payment payment, String pgKeyOrToken, Integer amount);
    void cancel(Payment payment, String reason);
}