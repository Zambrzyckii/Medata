package com.medata.catalog.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
public class LabTest implements Serializable, Comparable<LabTest> {
  @Serial private static final long serialVersionUID = 1L;
  private String name;
  private String unit;
  private double referenceMin;
  private double referenceMax;
  private BigDecimal price;
  private TestCategory category;

  @Override
  public int compareTo(LabTest other) {
    return name.compareTo(other.name);
  }
}
