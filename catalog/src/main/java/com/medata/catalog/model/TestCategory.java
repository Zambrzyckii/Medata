package com.medata.catalog.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = "labTests")
@EqualsAndHashCode(exclude = "labTests")
public class TestCategory implements Serializable, Comparable<TestCategory> {
  @Serial private static final long serialVersionUID = 1L;
  private String name;
  private boolean requiresFasting;

  @Builder.Default private List<LabTest> labTests = new ArrayList<>();

  @Override
  public int compareTo(TestCategory other) {
    return name.compareTo(other.name);
  }

  public void addLabTest(LabTest labTest) {
    labTests.add(labTest);
    labTest.setCategory(this);
  }
}
