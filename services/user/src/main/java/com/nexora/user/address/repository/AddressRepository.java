package com.nexora.user.address.repository;

import com.nexora.user.address.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {

    Optional<Address> findByUid(String uid);
}
