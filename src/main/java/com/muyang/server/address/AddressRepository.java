package com.muyang.server.address;

import com.muyang.server.address.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserIdOrderByIsDefaultDescIdDesc(Long userId);

    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    long countByUserId(Long userId);
}