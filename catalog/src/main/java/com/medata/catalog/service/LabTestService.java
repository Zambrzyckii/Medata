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
    return labTestRepository.save(labTest);
  }

  public void deleteById(UUID id) {
    labTestRepository.deleteById(id);
  }
}
