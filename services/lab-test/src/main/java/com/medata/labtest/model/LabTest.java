package com.medata.labtest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "lab_tests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
public class LabTest implements Comparable<LabTest> {

  @Id @Builder.Default private UUID id = UUID.randomUUID();

  private String name;
  private String unit;
  private double referenceMin;
  private double referenceMax;
  private BigDecimal price;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id")
  private TestCategory category;

  @Override
  public int compareTo(LabTest other) {
    return name.compareTo(other.name);
  }
}
