package com.nexora.user.address.repository;

import com.nexora.user.address.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUidAndUserUid(String uid, String userUid);

    Page<Address> findByUserUid(String userUid, Pageable pageable);
}
