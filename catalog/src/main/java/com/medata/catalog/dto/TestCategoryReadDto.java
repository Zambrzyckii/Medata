package com.medata.catalog.dto;

import com.medata.catalog.model.TestCategory;
import java.util.UUID;

public record TestCategoryReadDto(UUID id, String name, boolean requiresFasting) {
  public static TestCategoryReadDto fromEntity(TestCategory category) {
    return new TestCategoryReadDto(
        category.getId(), category.getName(), category.isRequiresFasting());
  }
}
