package com.muyang.server.admin.service;

import com.muyang.server.order.Order;
import com.muyang.server.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AdminOrderService {
    private final OrderRepository orderRepository;
    public List<Order>list(){
        return orderRepository.findAllByOrderByIdDesc();
    }
    @Transactional
    public void updateStatus(Long id, String status){
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }
}
