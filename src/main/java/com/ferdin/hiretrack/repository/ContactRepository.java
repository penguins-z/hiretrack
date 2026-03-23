package com.ferdin.hiretrack.repository;

import com.ferdin.hiretrack.entity.Application;
import com.ferdin.hiretrack.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    public List<Contact> findByApplicationId(Long applicationId);
}
