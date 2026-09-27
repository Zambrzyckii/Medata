package com.medata.labtest.dto;

import com.medata.labtest.model.LabTest;
import java.util.List;
import java.util.UUID;

public record LabTestListDto(List<Entry> tests) {

  public record Entry(UUID id, String name) {}

  public static LabTestListDto fromEntities(List<LabTest> labTests) {
    return new LabTestListDto(
        labTests.stream().map(labTest -> new Entry(labTest.getId(), labTest.getName())).toList());
  }
}
