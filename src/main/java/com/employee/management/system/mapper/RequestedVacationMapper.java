package com.employee.management.system.mapper;

import com.employee.management.system.dto.response.RespRequestedVacation;
import com.employee.management.system.entity.RequestedVacation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestedVacationMapper {


    @Mapping(source = "startDay", target = "startDay")
    @Mapping(source = "endDay", target = "endDay")
    @Mapping(source = "vacationPay", target = "vacationPay")
    @Mapping(source = "totalSalary", target = "totalSalary")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "status", expression = "java(requestedVacation.getStatus().name())")
    RespRequestedVacation toResponse(RequestedVacation requestedVacation);
}
