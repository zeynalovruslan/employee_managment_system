package com.employee.management.system.repository;

import com.employee.management.system.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Optional<Leave> findById(Long id);

}
