package dev.omedia.boot.controller;

import dev.omedia.boot.domain.UpdateType;
import dev.omedia.boot.dto.UnitDTO;
import dev.omedia.boot.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService service;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve unit with specified id",
            description = "If GET is called on article, it retrieves unit's children until it reaches leaf, " +
                    "otherwise it only retrieves unit's fields, not children",
            responses = {@ApiResponse(responseCode = "404", description = "Unit with specified id was not found"),
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved unit with specified id")})
    public ResponseEntity<UnitDTO> findById(@PathVariable(name = "id") long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Retrieve all units from database",
            responses = {@ApiResponse(responseCode = "204", description = "Not a single unit is stored in database"),
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all unit")})
    public ResponseEntity<Collection<UnitDTO>> findALl() {
        Collection<UnitDTO> all = service.findAll();
        if (!all.isEmpty()) {
            return ResponseEntity.of(Optional.of(all));
        } else {
            // თუ ცარიელია პირდაპირ ექსეფშენის გასროლა ხო არ ჯობს? რა საჭიროა ცარიელი კოლექციის დაბრუნება?
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(all);
        }
    }

    @PostMapping
    @Operation(summary = "Save a new unit to database",
            description = "Only can add unit's fields, not it's children",
            responses = {@ApiResponse(responseCode = "400", description = "Some validations failed"),
                    @ApiResponse(responseCode = "201", description = "Successfully created a new unit (resource)"),
                    @ApiResponse(responseCode = "404", description = "Parent was not found")})
    public ResponseEntity<UnitDTO> save(@Valid @RequestBody UnitDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(dto));
    }

    @PutMapping("/{id}/{updateType}")
    @Operation(summary = "Update already existing unit",
            description = "Only can update unit's specific fields (only: effectiveStartDate, effectiveEndDate and content)," +
                    " not it's children",
            responses = {@ApiResponse(responseCode = "404", description = "Unit with specified id was not found"),
                    @ApiResponse(responseCode = "200", description = "Successfully updated unit with specified id"),
                    @ApiResponse(responseCode = "400", description = "Some validations failed")})
    public ResponseEntity<UnitDTO> update(
            @PathVariable(name = "id") long id,
            @PathVariable(name = "updateType")UpdateType type,
            @Valid @RequestBody UnitDTO dto
            ) {
        return ResponseEntity.of(
                Optional.of(service.update(id, type.toString(), dto))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Sets EffectiveToDate to current date of unit with specified id from database",
            responses = {@ApiResponse(responseCode = "404", description = "Unit with specified id was not found"),
                    @ApiResponse(responseCode = "204", description = "Unit was successfully updated")})
    public void delete(@PathVariable(name = "id") long id) {
        service.delete(id);
    }
}
