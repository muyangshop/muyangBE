package com.muyang.server.admin;

import com.muyang.server.catalog.Sku;
import lombok.Data;

/** 관리자 상품 등록/수정 폼 — Sku의 스칼라 필드만 (이미지/속성/상세는 별도) */
@Data
public class ProductForm {
    private String id;
    private String name;
    private String cat;
    private String pet;
    private int price;
    private Integer was;
    private String tag;
    private double rating;
    private int reviews;
    private String ph;
    private String desc;
    private int sortOrder;
    private String imageUrl;

    public static ProductForm of(Sku s) {
        ProductForm f = new ProductForm();
        f.id = s.getId();
        f.name = s.getName();
        f.cat = s.getCat();
        f.pet = s.getPet();
        f.price = s.getPrice();
        f.was = s.getWas();
        f.tag = s.getTag();
        f.rating = s.getRating();
        f.reviews = s.getReviews();
        f.ph = s.getPh();
        f.desc = s.getDesc();
        f.sortOrder = s.getSortOrder();
        f.imageUrl = s.getImageUrl();
        return f;
    }

    /** 폼 값을 대상 Sku에 복사 (관계 필드는 건드리지 않아 기존 이미지/속성 보존) */
    public void applyTo(Sku s) {
        s.setId(id);
        s.setName(name);
        s.setCat(cat);
        s.setPet(pet);
        s.setPrice(price);
        s.setWas(was);
        s.setTag(tag);
        s.setRating(rating);
        s.setReviews(reviews);
        s.setPh(ph);
        s.setDesc(desc);
        s.setSortOrder(sortOrder);
        s.setImageUrl(imageUrl);
    }
}
