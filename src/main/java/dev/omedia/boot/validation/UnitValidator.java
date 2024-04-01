package dev.omedia.boot.validation;

import dev.omedia.boot.domain.Type;
import dev.omedia.boot.domain.Unit;
import dev.omedia.boot.domain.UpdateType;
import dev.omedia.boot.dto.UnitDTO;
import dev.omedia.boot.exception.IllegalDateException;
import dev.omedia.boot.exception.IllegalTypeException;
import dev.omedia.boot.exception.TypeChangeException;
import dev.omedia.boot.exception.UpdateTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UnitValidator {

    public static void validateUnitOnSave(UnitDTO dto, Unit parent) {
        if (parent.getType() == Type.NORMATIVE_ACT && dto.getType() == Type.NORMATIVE_ACT) {
            throw new IllegalTypeException("NORMATIVE_ACT can not contain NORMATIVE_ACT");
        }
        if (parent.getType() == Type.BOOK && (dto.getType() == Type.NORMATIVE_ACT || dto.getType() == Type.BOOK)) {
            throw new IllegalTypeException("BOOK can not contain NORMATIVE_ACT or BOOK");
        }
        if (parent.getType() == Type.CHAPTER && (dto.getType() == Type.NORMATIVE_ACT ||
                dto.getType() == Type.BOOK || dto.getType() == Type.CHAPTER)) {
            throw new IllegalTypeException("CHAPTER can not contain NORMATIVE_ACT, BOOK or CHAPTER");
        }
        if (parent.getType() == Type.ARTICLE && (dto.getType() == Type.NORMATIVE_ACT ||
                dto.getType() == Type.BOOK || dto.getType() == Type.CHAPTER || dto.getType() == Type.ARTICLE)) {
            throw new IllegalTypeException("ARTICLE can not contain NORMATIVE_ACT, BOOK, CHAPTER or ARTICLE");
        }
        if (parent.getType() == Type.PARAGRAPH && dto.getType() != Type.PARAGRAPH) {
            throw new IllegalTypeException("PARAGRAPH can not contain anything but PARAGRAPH");
        }
        validateUnitOnSave(dto);
    }

    public static void validateUnitOnSave(UnitDTO dto) {
        LocalDate endDate = LocalDate.parse(dto.getEffectiveEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (dto.getEffectiveStartDate().isAfter(endDate)) {
            throw new IllegalDateException("EffectiveStartDate can not be after EffectiveEndDate");
        }
    }

    public static void validateOnUpdate(UnitDTO dto, String updateType) {
        if (!updateType.equalsIgnoreCase(UpdateType.SUDO.toString())
                && !updateType.equalsIgnoreCase(UpdateType.TYPO.toString())) {
            throw new UpdateTypeException("Unit can be updated only after typo or if supervisor changes it");
        }

        validateUnitOnSave(dto);
    }
}
