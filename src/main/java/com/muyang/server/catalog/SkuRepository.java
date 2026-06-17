package com.muyang.server.catalog;

import com.muyang.server.catalog.Sku;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, String> {

    List<Sku> findAllByOrderBySortOrderAsc();

    List<Sku> findTop4ByOrderBySortOrderAsc();
}
