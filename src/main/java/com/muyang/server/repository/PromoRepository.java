package com.muyang.server.repository;

import com.muyang.server.entity.Promo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoRepository extends JpaRepository<Promo, Long> {

    List<Promo> findAllByOrderByIdAsc();
}
