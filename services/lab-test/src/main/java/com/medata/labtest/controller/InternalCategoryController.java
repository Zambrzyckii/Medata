package com.medata.labtest.controller;

import com.medata.labtest.dto.CategoryEventDto;
import com.medata.labtest.model.TestCategory;
import com.medata.labtest.service.TestCategoryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/categories")
@RequiredArgsConstructor
public class InternalCategoryController {
  private final TestCategoryService testCategoryService;

  @PutMapping("/{id}")
  public ResponseEntity<Void> upsertCategory(
      @PathVariable UUID id, @RequestBody CategoryEventDto event) {
    TestCategory replica =
        testCategoryService.findById(id).orElseGet(() -> TestCategory.builder().id(id).build());
    replica.setName(event.name());
    testCategoryService.save(replica);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
    testCategoryService.findById(id).ifPresent(category -> testCategoryService.deleteById(id));
    return ResponseEntity.noContent().build();
  }
}
