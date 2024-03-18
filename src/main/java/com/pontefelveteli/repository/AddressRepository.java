package com.pontefelveteli.repository;



import com.pontefelveteli.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("select ad from AppUser a join a.addressList ad where a.name=:name")
    List<Address> findByName(String name);
    @Query("select ad from AppUser a join a.addressList ad where a.name=:name and ad.id=:addressId")
    Optional<Address> findByNameAndId(String name, Integer addressId);
}
