package com.employee.management.system.repository;

import com.employee.management.system.entity.DayOffDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayOffDayRepository extends JpaRepository<DayOffDay, Long> {

   DayOffDay findByYearAndMonth(int year, int month);

  List <DayOffDay> findHolidayByYearAndMonth(int year, int month);


}
