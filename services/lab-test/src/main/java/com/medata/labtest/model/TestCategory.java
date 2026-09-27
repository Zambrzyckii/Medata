package com.medata.labtest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "test_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "labTests")
@EqualsAndHashCode(exclude = "labTests")
public class TestCategory implements Comparable<TestCategory> {

  @Id @Builder.Default private UUID id = UUID.randomUUID();

  private String name;
  private boolean requiresFasting;

  @OneToMany(
      mappedBy = "category",
      fetch = FetchType.LAZY,
      cascade = CascadeType.REMOVE,
      orphanRemoval = true)
  @Builder.Default
  private List<LabTest> labTests = new ArrayList<>();

  public void addLabTest(LabTest labTest) {
    labTests.add(labTest);
    labTest.setCategory(this);
  }

  @Override
  public int compareTo(TestCategory other) {
    return name.compareTo(other.name);
  }
}
