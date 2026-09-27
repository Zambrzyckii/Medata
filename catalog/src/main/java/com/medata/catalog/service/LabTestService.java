package com.medata.catalog.service;

import com.medata.catalog.model.LabTest;
import com.medata.catalog.model.TestCategory;
import com.medata.catalog.repository.LabTestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabTestService {
  private final LabTestRepository labTestRepository;

  public List<LabTest> findAll() {
    return labTestRepository.findAll();
  }

  public Optional<LabTest> findById(UUID id) {
    return labTestRepository.findById(id);
  }

  public List<LabTest> findAllByCategory(TestCategory category) {
    return labTestRepository.findAllByCategory(category);
  }

  public LabTest save(LabTest labTest) {
    validate(labTest);
    return labTestRepository.save(labTest);
  }

  public void deleteById(UUID id) {
    labTestRepository.deleteById(id);
  }

  private void validate(LabTest labTest) {
    if (labTest.getName() == null || labTest.getName().isBlank()) {
      throw new IllegalArgumentException("Test name must not be blank");
    }
    if (labTest.getUnit() == null || labTest.getUnit().isBlank()) {
      throw new IllegalArgumentException("Unit must not be blank");
    }
    if (labTest.getReferenceMin() < 0) {
      throw new IllegalArgumentException("Reference min must not be negative");
    }
    if (labTest.getReferenceMin() > labTest.getReferenceMax()) {
      throw new IllegalArgumentException("Reference min must not be greater than reference max");
    }
    if (labTest.getPrice() == null || labTest.getPrice().signum() < 0) {
      throw new IllegalArgumentException("Price must not be negative");
    }
  }
}
