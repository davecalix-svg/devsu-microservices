package com.devsu.cuentaservice.mapper;

import com.devsu.cuentaservice.dto.CuentaRequestDTO;
import com.devsu.cuentaservice.dto.CuentaResponseDTO;
import com.devsu.cuentaservice.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    @Mapping(target = "id", ignore = true)
    Cuenta toEntity(CuentaRequestDTO dto);
    CuentaResponseDTO toResponseDTO(Cuenta cuenta);
}
