package com.muyang.server.repository;

import com.muyang.server.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findAllByOrderBySortOrderAsc();

    List<Product> findTop4ByOrderBySortOrderAsc();
}
