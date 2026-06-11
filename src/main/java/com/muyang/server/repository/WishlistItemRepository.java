package com.muyang.server.repository;

import com.muyang.server.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {
}
