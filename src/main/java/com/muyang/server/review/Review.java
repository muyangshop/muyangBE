package com.muyang.server.review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 어떤 상품의 리뷰인지 (시드 리뷰는 null 허용) */
    private String skuId;

    /** 작성자 (시드 리뷰는 null) */
    private Long userId;

    private String name;
    private String pet;
    private int rating;

    @Column(name = "content", length = 500)
    private String text;

    private Instant createdAt;
}