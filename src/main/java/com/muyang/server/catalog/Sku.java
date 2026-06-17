package com.muyang.server.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skus")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sku {

    @Id
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

    /** 목록 카드용 짧은 요약 설명 */
    @Column(name = "summary", length = 500)
    private String desc;

    @JsonIgnore
    private int sortOrder;

    // ── 상세 콘텐츠 (목록 응답엔 안 실리도록 @JsonIgnore — 상세 DTO로 노출) ──

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "detail_id")
    private SkuDetail detail;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "sku_id")
    private List<SkuImage> images;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "sku_id")
    private List<SkuAttribute> attributes;
}