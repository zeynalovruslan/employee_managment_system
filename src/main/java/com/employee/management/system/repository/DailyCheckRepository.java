package com.employee.management.system.repository;

import com.employee.management.system.entity.DailyCheck;
import com.employee.management.system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyCheckRepository extends JpaRepository<DailyCheck, Long> {

    boolean existsDailyCheckByEmployeeAndWorkDate(Employee employee, LocalDate workDate);


}
