package com.employee.management.system.mapper;

import com.employee.management.system.dto.request.ReqEmployee;
import com.employee.management.system.dto.response.RespEmployee;
import com.employee.management.system.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status",
            expression = "java(com.employee.management.system.enums.EmployeeStatusEnum.CREATED)")
    Employee toEntity(ReqEmployee request);

    RespEmployee toResponse(Employee request);

    void updateEmployeeFromRequest(ReqEmployee request, @MappingTarget Employee employee);


}
