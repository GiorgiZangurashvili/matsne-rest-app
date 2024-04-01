package dev.omedia.boot.mapper;

import dev.omedia.boot.domain.Unit;
import dev.omedia.boot.dto.UnitDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnitMapper {

    // dtoToEntity - სრული სახელები დაარქვი ხოლმე
    public static Unit toEntity(UnitDTO dto) {
        Unit entity = new Unit();

        entity.setId(dto.getId());
        entity.setPredecessorId(dto.getPredecessorId());
        entity.setContent(dto.getContent());
        entity.setEffectiveStartDate(dto.getEffectiveStartDate());
        entity.setParentId(dto.getParentId());

        /// კონსტანტად გქონდეს ხოლმე მსგავსი მნიშვნელობები. private static final String PRESENT = "Present";
        if (dto.getEffectiveEndDate().equals("Present")) {
            entity.setEffectiveEndDate(null);
        } else {
            entity.setEffectiveEndDate(LocalDate.parse(dto.getEffectiveEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        entity.setType(dto.getType());
        if (!Objects.isNull(dto.getChildren())) {
            entity.setChildren(dto.getChildren()
                    .stream()
                    .map(UnitMapper::toEntity)
                    .collect(Collectors.toList()));
        } else {
            entity.setChildren(new ArrayList<>());
        }

        return entity;
    }

    public static UnitDTO toDTO(Unit entity) {
        UnitDTO dto = new UnitDTO();

        dto.setId(entity.getId());
        dto.setPredecessorId(entity.getPredecessorId());
        dto.setContent(entity.getContent());
        dto.setEffectiveStartDate(entity.getEffectiveStartDate());
        dto.setParentId(entity.getParentId());

        if (!Objects.isNull(entity.getEffectiveEndDate())) {
            dto.setEffectiveEndDate(entity.getEffectiveEndDate().toString());
        } else {
            // აქაც იგივე
            dto.setEffectiveEndDate("Present");
        }
        dto.setType(entity.getType());

        if (!Objects.isNull(entity.getChildren())) {
            dto.setChildren(entity.getChildren()
                    .stream()
                    .map(UnitMapper::toDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setChildren(new ArrayList<>());
        }

        return dto;
    }
}
