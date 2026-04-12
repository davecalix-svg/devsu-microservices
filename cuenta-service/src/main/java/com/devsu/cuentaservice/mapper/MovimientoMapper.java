package com.devsu.cuentaservice.mapper;

import com.devsu.cuentaservice.dto.MovimientoResponseDTO;
import com.devsu.cuentaservice.entity.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    @Mapping(target = "tipoMovimiento", expression = "java(movimiento.getTipoMovimiento().name())")
    MovimientoResponseDTO toResponseDTO(Movimiento movimiento);
}
