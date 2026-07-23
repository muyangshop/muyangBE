package com.muyang.server.admin.service;
import com.muyang.server.catalog.Promo;
import com.muyang.server.catalog.PromoRepository;
import com.muyang.server.common.storage.FileStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBannerService {
    private final PromoRepository promoRepository;
    private final FileStorageService fileStorage;

    public List<Promo> list(){
        return promoRepository.findAll();
    }
    @Transactional
    public void uploadImage(Long id, MultipartFile file){
        Promo promo = promoRepository.findById(id).orElseThrow();
        promo.setImageUrl(fileStorage.store(file, "banners"));
        promoRepository.save(promo);
    }
}
