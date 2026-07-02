package com.muyang.server.admin;

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
}
