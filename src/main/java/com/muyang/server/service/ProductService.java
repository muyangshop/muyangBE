package com.muyang.server.service;

import com.muyang.server.entity.Product;
import com.muyang.server.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /** 카테고리·검색어 필터 목록 */
    public List<Product> search(String cat, String q) {
        return productRepository.findAllByOrderBySortOrderAsc().stream()
                .filter(p -> cat == null || cat.isBlank() || p.getCat().equals(cat))
                .filter(p -> q == null || q.isBlank()
                        || p.getName().contains(q)
                        || p.getCat().contains(q)
                        || p.getPet().contains(q))
                .toList();
    }

    /** 홈 화면 인기 상품 (앞 4개) */
    public List<Product> popular() {
        return productRepository.findTop4ByOrderBySortOrderAsc();
    }

    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Unknown product: " + id));
    }
}
