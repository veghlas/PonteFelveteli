package com.pontefelveteli.repository;

import com.pontefelveteli.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    @Query("select a from AppUser a where a.name=:name")
    Optional<AppUser> findByName(String name);
}
