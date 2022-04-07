package com.bt.dev.kodemy.users.repositories;

import com.bt.dev.kodemy.users.model.AddressInfos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressInfosRepository extends JpaRepository<AddressInfos, Long> {
}
