package com.muyang.server.catalog;

import com.muyang.server.catalog.Promo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoRepository extends JpaRepository<Promo, Long> {

    List<Promo> findAllByOrderByIdAsc();
}
