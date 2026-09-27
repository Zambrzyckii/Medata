package com.medata.labtest.service;

import com.medata.labtest.model.TestCategory;
import com.medata.labtest.repository.TestCategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCategoryService {
  private final TestCategoryRepository testCategoryrepository;

  public List<TestCategory> findAll() {
    return testCategoryrepository.findAll();
  }

  public Optional<TestCategory> findById(UUID id) {
    return testCategoryrepository.findById(id);
  }

  public TestCategory save(TestCategory category) {
    return testCategoryrepository.save(category);
  }

  public void deleteById(UUID id) {
    testCategoryrepository.deleteById(id);
  }
}
