package dev.omedia.boot.dto;

import dev.omedia.boot.domain.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(title = "Unit", description = "Stores unit data in database")
public class UnitDTO {
    @Schema(description = "Unit's id")
    private long id;

    @Schema(description = "Unit's predecessor")
    private long predecessorId;

    @Schema(description = "Unit's content")
    @NotBlank(message = "Unit's content should not be null or empty")
    private String content;

    @Schema(description = "Unit's effective start date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveStartDate;

    @Schema(description = "Unit's effective end date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date format must be yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String effectiveEndDate;

    @Schema(description = "Unit's type")
    private Type type;

    @Schema(description = "Unit's parent id")
    private Long parentId;

    @Schema(description = "Unit's children")
    private Collection<UnitDTO> children;
}
