package com.medata.catalog.dto;

import com.medata.catalog.model.LabTest;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record LabTestDto(
    String name,
    String unit,
    double referenceMin,
    double referenceMax,
    BigDecimal price,
    String category)
    implements Comparable<LabTestDto> {

  public static LabTestDto fromEntity(LabTest labTest) {
    return LabTestDto.builder()
        .name(labTest.getName())
        .unit(labTest.getUnit())
        .referenceMin(labTest.getReferenceMin())
        .referenceMax(labTest.getReferenceMax())
        .price(labTest.getPrice())
        .category(labTest.getCategory().getName())
        .build();
  }

  @Override
  public int compareTo(LabTestDto other) {
    return name.compareTo(other.name);
  }
}
