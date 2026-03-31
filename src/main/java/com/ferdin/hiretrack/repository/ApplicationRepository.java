package com.ferdin.hiretrack.repository;

import com.ferdin.hiretrack.entity.Application;
import com.ferdin.hiretrack.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByUserId(Long userId, Pageable pageable);
    Page<Application> findByUserIdAndStatus (Long userId, ApplicationStatus status, Pageable pageable);
}