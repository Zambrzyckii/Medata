package com.medata.catalog.controller;

import com.medata.catalog.dto.LabTestCreateUpdateDto;
import com.medata.catalog.dto.LabTestListDto;
import com.medata.catalog.dto.LabTestReadDto;
import com.medata.catalog.model.LabTest;
import com.medata.catalog.service.LabTestService;
import com.medata.catalog.service.TestCategoryService;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LabTestController {

  private final TestCategoryService testCategoryService;
  private final LabTestService labTestService;

  @GetMapping("/api/tests")
  public LabTestListDto getAllTests() {
    return LabTestListDto.fromEntities(labTestService.findAll());
  }

  @GetMapping("/api/tests/{id}")
  public ResponseEntity<LabTestReadDto> getTest(@PathVariable UUID id) {
    return labTestService
        .findById(id)
        .map(labTest -> ResponseEntity.ok(LabTestReadDto.fromEntity(labTest)))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/api/categories/{categoryId}/tests")
  public ResponseEntity<LabTestListDto> getTestsOfCategory(@PathVariable UUID categoryId) {
    return testCategoryService
        .findById(categoryId)
        .map(
            category ->
                ResponseEntity.ok(
                    LabTestListDto.fromEntities(labTestService.findAllByCategory(category))))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/api/categories/{categoryId}/tests")
  public ResponseEntity<LabTestReadDto> createTest(
      @PathVariable UUID categoryId, @RequestBody LabTestCreateUpdateDto dto) {
    return testCategoryService
        .findById(categoryId)
        .map(
            category -> {
              LabTest labTest =
                  LabTest.builder()
                      .name(dto.name())
                      .unit(dto.unit())
                      .referenceMin(dto.referenceMin())
                      .referenceMax(dto.referenceMax())
                      .price(dto.price())
                      .category(category)
                      .build();
              LabTest saved = labTestService.save(labTest);
              return ResponseEntity.created(URI.create("/api/tests/" + saved.getId()))
                  .body(LabTestReadDto.fromEntity(saved));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/api/tests/{id}")
  public ResponseEntity<Void> updateTest(
      @PathVariable UUID id, @RequestBody LabTestCreateUpdateDto dto) {
    return labTestService
        .findById(id)
        .map(
            labTest -> {
              labTest.setName(dto.name());
              labTest.setUnit(dto.unit());
              labTest.setReferenceMin(dto.referenceMin());
              labTest.setReferenceMax(dto.referenceMax());
              labTest.setPrice(dto.price());
              labTestService.save(labTest);
              return ResponseEntity.noContent().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/api/tests/{id}")
  public ResponseEntity<Void> deleteTest(@PathVariable UUID id) {
    return labTestService
        .findById(id)
        .map(
            labTest -> {
              labTestService.deleteById(id);
              return ResponseEntity.noContent().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
