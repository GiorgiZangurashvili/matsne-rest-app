package dev.omedia.boot.service;

import dev.omedia.boot.domain.Type;
import dev.omedia.boot.domain.Unit;
import dev.omedia.boot.domain.UpdateType;
import dev.omedia.boot.dto.UnitDTO;
import dev.omedia.boot.exception.EntityNotFoundException;
import dev.omedia.boot.mapper.UnitMapper;
import dev.omedia.boot.repository.UnitRepository;
import dev.omedia.boot.validation.UnitValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository repository;

    public Collection<UnitDTO> findAll() {
        Collection<UnitDTO> all = new ArrayList<>();

        for (var unit : repository.findAll()) {
            unit.setChildren(new ArrayList<>());
            all.add(UnitMapper.toDTO(unit));
        }

        return all;
    }

    public Optional<UnitDTO> findById(long id) {
        Unit unit = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Unit with id = " + id + " was not found"));
        if (unit.getType() != Type.ARTICLE) {
            unit.setChildren(new ArrayList<>());
            return Optional.of(UnitMapper.toDTO(unit));
        } else {
            //fetch all children with full depth
            return Optional.of(getArticleWithFullDepth(unit, new HashSet<>()));
        }
    }

    private UnitDTO getArticleWithFullDepth(Unit article, HashSet<Long> set) {
        Collection<Unit> children = article.getChildren();
        article.setChildren(null);
        UnitDTO result = UnitMapper.toDTO(article);
        set.add(article.getId());

        List<UnitDTO> allChildren = new ArrayList<>();
        for (Unit child : children) {
            if (set.contains(child.getId()))
                continue;
            UnitDTO childDTO = getArticleWithFullDepth(child, set);
            allChildren.add(childDTO);
        }
        result.setChildren(allChildren);
        return result;
    }

    public UnitDTO save(UnitDTO dto) {
        if (dto.getParentId() != null) {
            Unit parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent with id = " + dto.getParentId() + " was not found"));
            UnitValidator.validateUnitOnSave(dto, parent);
        } else {
            UnitValidator.validateUnitOnSave(dto);
        }
        dto.setPredecessorId(0);
        Unit entity = UnitMapper.toEntity(dto);
        Unit save = repository.save(entity);
        return UnitMapper.toDTO(save);
    }

    public UnitDTO update(long id, String updateType, UnitDTO dto) {
        Unit entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Unit with id = " + id + " was not found"));
        UnitValidator.validateOnUpdate(dto, updateType);
        entity.setId(id);
        entity.setEffectiveEndDate(dto.getEffectiveStartDate());
        repository.save(entity);

        Unit updatedEntity = new Unit();
        updatedEntity.setEffectiveStartDate(dto.getEffectiveStartDate());
        updatedEntity.setEffectiveEndDate(LocalDate.parse(dto.getEffectiveEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        updatedEntity.setPredecessorId(id);
        updatedEntity.setType(entity.getType());
        updatedEntity.setParentId(null);
        updatedEntity.setChildren(new ArrayList<>());
        updatedEntity.setContent(dto.getContent());
        return UnitMapper.toDTO(repository.save(updatedEntity));
    }

    public void delete(long id) {
        Unit unit = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Unit with id = " + id + " was not found"));
        updateEffectiveEndDate(unit, new HashSet<>());
    }

    private void updateEffectiveEndDate(Unit unit, HashSet<Long> set) {
        unit.setEffectiveEndDate(LocalDate.now());

        for (var child : unit.getChildren()) {
            if (set.contains(child.getId()))
                continue;
            updateEffectiveEndDate(child, set);
        }

        repository.save(unit);
    }
}
