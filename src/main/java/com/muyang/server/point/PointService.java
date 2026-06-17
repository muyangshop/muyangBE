package com.muyang.server.point;

import com.muyang.server.point.PointBalance;
import com.muyang.server.point.PointTransaction;
import com.muyang.server.point.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    private final PointTransactionRepository pointRepository;
    @Transactional(readOnly = true)
    public PointBalance get(Long userId){
        if(userId == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        List<PointTransaction> history = pointRepository.findByUserIdOrderByIdDesc(userId);
        int balance = history.stream().mapToInt(PointTransaction::getAmount).sum();
        return new PointBalance(balance, history);
    }
    // 적립/차감 공통 - 다른 서비스에서 호출
    public void record(Long userId, int amount, String reason){
        if(amount == 0) return;
        pointRepository.save(PointTransaction.builder()
                .userId(userId)
                .amount(amount)
                .reason(reason)
                .createdAt(Instant.now())
                .build());
    }
}
