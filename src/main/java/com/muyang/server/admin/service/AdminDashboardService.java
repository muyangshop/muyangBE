package com.muyang.server.admin.service;

import com.muyang.server.auth.UserRepository;
import com.muyang.server.catalog.SkuRepository;
import com.muyang.server.order.Order;
import com.muyang.server.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {
    private final SkuRepository skuRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    public long productCount(){return skuRepository.count();}
    public long orderCount(){return orderRepository.count();}
    public long userCount(){return userRepository.count();}
    public List<Order>recentOrders(int limit){
        return orderRepository.findAllByOrderByIdDesc().stream().limit(limit).toList();
    }
}
