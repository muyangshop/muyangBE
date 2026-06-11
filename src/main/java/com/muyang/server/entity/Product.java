package com.muyang.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    private String name;

    /** 카테고리 키 (예: 장난감, 자동완구) */
    private String cat;

    /** 대상 반려동물 (예: 고양이) */
    private String pet;

    private int price;

    /** 할인 전 가격 — 없으면 null */
    private Integer was;

    /** 뱃지 (BEST·신상·인기) — 없으면 null */
    private String tag;

    private double rating;

    /** 리뷰 수 */
    private int reviews;

    /** 이미지 플레이스홀더 라벨 */
    private String ph;

    @Column(name = "description", length = 500)
    private String desc;

    /** 목록 정렬용 (인기 상품 = 앞 4개) */
    @JsonIgnore
    private int sortOrder;
}
