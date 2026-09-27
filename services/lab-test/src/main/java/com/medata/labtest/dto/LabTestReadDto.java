package com.medata.labtest.dto;

import com.medata.labtest.model.LabTest;
import java.math.BigDecimal;
import java.util.UUID;

public record LabTestReadDto(
    UUID id,
    String name,
    String unit,
    double referenceMin,
    double referenceMax,
    BigDecimal price,
    String category) {

  public static LabTestReadDto fromEntity(LabTest labTest) {
    return new LabTestReadDto(
        labTest.getId(),
        labTest.getName(),
        labTest.getUnit(),
        labTest.getReferenceMin(),
        labTest.getReferenceMax(),
        labTest.getPrice(),
        labTest.getCategory().getName());
  }
}
