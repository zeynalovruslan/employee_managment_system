package com.employee.management.system.mapper;

import com.employee.management.system.dto.response.RespEmployeeInvoice;
import com.employee.management.system.entity.EmployeeInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeInvoiceMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    RespEmployeeInvoice toResponse(EmployeeInvoice invoice);

}
