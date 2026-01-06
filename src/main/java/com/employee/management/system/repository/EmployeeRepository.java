package com.employee.management.system.repository;

import com.employee.management.system.entity.Employee;
import com.employee.management.system.enums.EmployeeStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

   Optional< List<Employee>> findEmployeeByStatus(EmployeeStatusEnum status);

    Optional<Employee> findEmployeeByIdAndStatus(Long id, EmployeeStatusEnum status);

    Optional<Employee> findEmployeeById(Long id);

    Optional<List<Employee>> findEmployeeByDepartmentIdAndStatus(Long departmentId, EmployeeStatusEnum status);



}
