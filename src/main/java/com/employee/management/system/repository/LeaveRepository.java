package com.employee.management.system.repository;

import com.employee.management.system.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Optional<Leave> findById(Long id);

//    @Query("""
//    SELECT COUNT(l) > 0
//    FROM Leave l
//    WHERE l.employee.id = :employeeId
//      AND l.requestStatus IN ('PENDING','APPROVED')
//      AND l.startAt < :endAt
//      AND l.endAt > :startAt
//""")
//    boolean existsOverlap(Long employeeId,
//                          LocalDateTime startAt,
//                          LocalDateTime endAt);        {{{ lazim olarsa eger query ile de yoxlaya bilerik}}}


}
