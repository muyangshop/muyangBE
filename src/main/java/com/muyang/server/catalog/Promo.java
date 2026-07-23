package com.muyang.server.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "promos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String title;

    /** 큰 글씨 (예: 15% 할인) */
    private String big;

    /** 보조 문구 */
    private String sub;

    /** 배너 테마: navy | cream */
    private String theme;

    /** 배너 이미지 */
    private String imageUrl;
}
