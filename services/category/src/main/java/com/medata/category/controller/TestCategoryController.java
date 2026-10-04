package com.medata.category.controller;

import com.medata.category.dto.TestCategoryCreateUpdateDto;
import com.medata.category.dto.TestCategoryListDto;
import com.medata.category.dto.TestCategoryReadDto;
import com.medata.category.event.CategoryEventPublisher;
import com.medata.category.model.TestCategory;
import com.medata.category.service.TestCategoryService;
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
  private final CategoryEventPublisher categoryEventPublisher;

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
    categoryEventPublisher.publishCreated(saved.getId(), saved.getName());
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
              categoryEventPublisher.publishDeleted(id);
              return ResponseEntity.noContent().<Void>build();
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
