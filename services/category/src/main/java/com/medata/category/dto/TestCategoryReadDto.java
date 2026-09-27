package com.medata.category.dto;

import com.medata.category.model.TestCategory;
import java.util.UUID;

public record TestCategoryReadDto(UUID id, String name, boolean requiresFasting) {
  public static TestCategoryReadDto fromEntity(TestCategory category) {
    return new TestCategoryReadDto(
        category.getId(), category.getName(), category.isRequiresFasting());
  }
}
