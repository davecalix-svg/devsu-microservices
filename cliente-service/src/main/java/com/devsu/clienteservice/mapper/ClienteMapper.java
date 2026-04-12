package com.devsu.clienteservice.mapper;

import com.devsu.clienteservice.dto.ClienteRequestDTO;
import com.devsu.clienteservice.dto.ClienteResponseDTO;
import com.devsu.clienteservice.entity.Cliente;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mappings({
            @Mapping(target = "clienteId", ignore = true),
            @Mapping(source = "nombre", target = "nombre"),
            @Mapping(source = "genero", target = "genero"),
            @Mapping(source = "edad", target = "edad"),
            @Mapping(source = "identificacion", target = "identificacion"),
            @Mapping(source = "direccion", target = "direccion"),
            @Mapping(source = "telefono", target = "telefono"),
            @Mapping(source = "contrasena", target = "contrasena"),
            @Mapping(source = "estado", target = "estado")
    })
    Cliente toEntity(ClienteRequestDTO dto);


    @Mappings({
            @Mapping(source = "clienteId", target = "clienteId"),
            @Mapping(source = "nombre", target = "nombre"),
            @Mapping(source = "identificacion", target = "identificacion"),
            @Mapping(source = "estado", target = "estado")
    })
    ClienteResponseDTO toDTO(Cliente entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(source = "nombre", target = "nombre"),
            @Mapping(source = "genero", target = "genero"),
            @Mapping(source = "edad", target = "edad"),
            @Mapping(source = "identificacion", target = "identificacion"),
            @Mapping(source = "direccion", target = "direccion"),
            @Mapping(source = "telefono", target = "telefono"),
            @Mapping(source = "contrasena", target = "contrasena"),
            @Mapping(source = "estado", target = "estado")
    })
    void updateFromDto(ClienteRequestDTO dto, @MappingTarget Cliente entity);
}
