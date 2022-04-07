package com.bt.dev.kodemy.users.repositories;

import com.bt.dev.kodemy.users.model.ContactInfos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInfosRepository extends JpaRepository<ContactInfos, Long> {
}
