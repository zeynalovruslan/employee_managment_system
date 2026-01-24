package com.employee.management.system.repository;

import com.employee.management.system.entity.Employee;
import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmployee(Employee employee);

    @Query("""
    select u from UserEntity u
    left join fetch u.roles r
    where u.username = :username
""")
    Optional<UserEntity> findByUsernameWithRoles(@Param("username") String username);


    @Query("""
select r.roleName
from UserEntity u
join u.roles r
where u.username = :username
""")
    Set<RoleNameEnum> findRoleNamesByUsername(@Param("username") String username);





}
