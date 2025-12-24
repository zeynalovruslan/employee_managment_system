package com.employee.management.system.repository;

import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.EmployeeInvoice;
import com.employee.management.system.entity.RequestedVacation;
import com.employee.management.system.enums.RequestVacationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeInvoiceRepository extends JpaRepository<EmployeeInvoice, Long> {

    boolean existsByEmployeeIdAndYearAndMonth(Long employeeId, int year, int month);

    @Query("""
                SELECT COUNT(v) > 0
                FROM RequestedVacation v
                WHERE v.employee.id = :employeeId
                  AND v.status IN (:statuses)
                  AND v.startDay <= :endDay
                  AND v.endDay >= :startDay
            """)
    boolean existsOverlappingVacation(
            @Param("employeeId") Long employeeId,
            @Param("statuses") List<RequestVacationStatusEnum> statuses,
            @Param("startDay") LocalDate startDay,
            @Param("endDay") LocalDate endDay
    );


    List<RequestedVacation> findByEmployeeId(Long employeeId);


}
