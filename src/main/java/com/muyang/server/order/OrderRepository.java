package com.muyang.server.order;

import com.muyang.server.order.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByIdDesc();

    List<Order> findByUserIdOrderByIdDesc(Long userId);
}
