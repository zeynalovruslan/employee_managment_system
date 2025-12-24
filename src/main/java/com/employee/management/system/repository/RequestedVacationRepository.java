package com.employee.management.system.repository;

import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestedVacationRepository extends JpaRepository<RequestedVacation, Long> {

    List<RequestedVacation> findRequestedVacationByEmployeeIdAndStatus(
            Long employeeId,
            RequestVacationStatusEnum vacationStatus);

    Optional<RequestedVacation> findRequestedVacationByIdAndStatus(Long id, RequestVacationStatusEnum statusEnum);

    List<RequestedVacation> findRequestedVacationByEmployeeId(Long employeeId);
}
