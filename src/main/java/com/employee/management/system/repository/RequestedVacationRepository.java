package com.employee.management.system.repository;

import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestedVacationRepository extends JpaRepository<RequestedVacation, Long> {

   Optional<RequestedVacation> findRequestedVacationByIdAndStatus(Long id, RequestVacationStatusEnum statusEnum);

}
