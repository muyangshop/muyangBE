package com.muyang.server.admin.service;

import com.muyang.server.admin.ProductForm;
import com.muyang.server.catalog.Sku;
import com.muyang.server.catalog.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductService {
    private final SkuRepository skuRepository;
    public List<Sku>list(){
        return skuRepository.findAllByOrderBySortOrderAsc();
    }
    // 상단 필드에 추가 (@RequiredArgsConstructor)
    private final com.muyang.server.common.storage.FileStorageService fileStorage;
    public ProductForm loadForm(String id) {
        Sku sku = skuRepository.findById(id).orElseThrow();
        return ProductForm.of(sku);
    }
    @Transactional
    public void save(ProductForm form){
        Sku sku = skuRepository.findById(form.getId()).orElseGet(Sku::new);
        form.applyTo(sku);
        skuRepository.save(sku);
    }
    @Transactional
    public void delete(String id){
        skuRepository.deleteById(id);
    }

    @Transactional
    public void uploadImage(String id, org.springframework.web.multipart.MultipartFile file) {
        Sku sku = skuRepository.findById(id).orElseThrow();
        sku.setImageUrl(fileStorage.store(file, "products"));
        skuRepository.save(sku);
    }

    @Transactional
    public void setDiscount(String id, int rate) {
        Sku sku = skuRepository.findById(id).orElseThrow();
        int base = sku.getWas() != null ? sku.getWas() : sku.getPrice();
        sku.setWas(base);
        sku.setPrice((int) Math.round(base * (100 - rate) / 100.0));
        skuRepository.save(sku);
    }

    @Transactional
    public void clearDiscount(String id) {
        Sku sku = skuRepository.findById(id).orElseThrow();
        if (sku.getWas() != null) {
            sku.setPrice(sku.getWas());
            sku.setWas(null);
            skuRepository.save(sku);
        }
    }
}
