package com.medata.catalog.controller;

import com.medata.catalog.dto.TestCategoryCreateUpdateDto;
import com.medata.catalog.dto.TestCategoryListDto;
import com.medata.catalog.dto.TestCategoryReadDto;
import com.medata.catalog.model.TestCategory;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class TestCategoryController {

  private final TestCategoryService testCategoryService;

  @GetMapping
  public TestCategoryListDto getCategories() {
    return TestCategoryListDto.fromEntities(testCategoryService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TestCategoryReadDto> getCategory(@PathVariable UUID id) {
    return testCategoryService
        .findById(id)
        .map(category -> ResponseEntity.ok(TestCategoryReadDto.fromEntity(category)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<TestCategoryReadDto> createCategory(
      @RequestBody TestCategoryCreateUpdateDto dto) {
    TestCategory category =
        TestCategory.builder().name(dto.name()).requiresFasting(dto.requiresFasting()).build();
    TestCategory saved = testCategoryService.save(category);
    return ResponseEntity.created(URI.create("/api/categories/" + saved.getId()))
        .body(TestCategoryReadDto.fromEntity(saved));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateCategory(
      @PathVariable UUID id, @RequestBody TestCategoryCreateUpdateDto dto) {
    return testCategoryService
        .findById(id)
        .map(
            category -> {
              category.setName(dto.name());
              category.setRequiresFasting(dto.requiresFasting());
              testCategoryService.save(category);
              return ResponseEntity.noContent().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
    return testCategoryService
        .findById(id)
        .map(
            category -> {
              testCategoryService.deleteById(id);
              return ResponseEntity.noContent().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
