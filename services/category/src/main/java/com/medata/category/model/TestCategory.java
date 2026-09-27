package com.medata.category.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@ToString
@EqualsAndHashCode
public class TestCategory implements Comparable<TestCategory> {

  @Id @Builder.Default private UUID id = UUID.randomUUID();

  private String name;
  private boolean requiresFasting;

  @Override
  public int compareTo(TestCategory other) {
    return name.compareTo(other.name);
  }
}
