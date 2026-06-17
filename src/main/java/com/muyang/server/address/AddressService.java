package com.muyang.server.address;

import com.muyang.server.address.AddressRequest;
import com.muyang.server.address.Address;
import com.muyang.server.address.AddressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public List<Address> list(Long userId) {
        requireUser(userId);
        return addressRepository.findByUserIdOrderByIsDefaultDescIdDesc(userId);
    }

    public Address create(Long userId, AddressRequest req) {
        requireUser(userId);
        // 첫 배송지는 자동으로 기본 배송지
        boolean makeDefault = req.isDefault() || addressRepository.countByUserId(userId) == 0;
        if (makeDefault) {
            unsetDefault(userId);
        }
        return addressRepository.save(Address.builder()
                .userId(userId)
                .label(req.label())
                .recipient(req.recipient())
                .phone(req.phone())
                .zipcode(req.zipcode())
                .address1(req.address1())
                .address2(req.address2())
                .isDefault(makeDefault)
                .build());
    }

    public Address update(Long userId, Long addressId, AddressRequest req) {
        Address addr = getOwned(userId, addressId);
        if (req.isDefault() && !addr.isDefault()) {
            unsetDefault(userId);
        }
        addr.setLabel(req.label());
        addr.setRecipient(req.recipient());
        addr.setPhone(req.phone());
        addr.setZipcode(req.zipcode());
        addr.setAddress1(req.address1());
        addr.setAddress2(req.address2());
        addr.setDefault(req.isDefault() || addr.isDefault());
        return addressRepository.save(addr);
    }

    public void delete(Long userId, Long addressId) {
        Address addr = getOwned(userId, addressId);
        addressRepository.delete(addr);
        // 기본 배송지를 지웠으면 남은 것 중 첫 번째를 기본으로 승격
        if (addr.isDefault()) {
            addressRepository.findByUserIdOrderByIsDefaultDescIdDesc(userId).stream()
                    .findFirst()
                    .ifPresent(a -> {
                        a.setDefault(true);
                        addressRepository.save(a);
                    });
        }
    }

    /** 기본 배송지 지정 */
    public Address setDefault(Long userId, Long addressId) {
        Address addr = getOwned(userId, addressId);
        unsetDefault(userId);
        addr.setDefault(true);
        return addressRepository.save(addr);
    }

    private Address getOwned(Long userId, Long addressId) {
        requireUser(userId);
        Address addr = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "배송지를 찾을 수 없습니다"));
        if (!addr.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 배송지만 관리할 수 있습니다");
        }
        return addr;
    }

    private void unsetDefault(Long userId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(a -> {
            a.setDefault(false);
            addressRepository.save(a);
        });
    }

    private void requireUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
    }
}