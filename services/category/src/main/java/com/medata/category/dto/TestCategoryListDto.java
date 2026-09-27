package com.medata.category.dto;

import com.medata.category.model.TestCategory;
import java.util.List;
import java.util.UUID;

public record TestCategoryListDto(List<Entry> categories) {
  public record Entry(UUID id, String name) {}

  public static TestCategoryListDto fromEntities(List<TestCategory> categories) {
    return new TestCategoryListDto(
        categories.stream()
            .map(category -> new Entry(category.getId(), category.getName()))
            .toList());
  }
}
