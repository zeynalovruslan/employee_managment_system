package com.employee.management.system.repository;

import com.employee.management.system.entity.DayOffDay;
import com.employee.management.system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DayOffDayRepository extends JpaRepository<DayOffDay, Long> {

  List <DayOffDay> findHolidayByYearAndMonth(int year, int month);

  boolean existsByYearAndMonthAndHoliday(int year, int month , int holiday);


}
