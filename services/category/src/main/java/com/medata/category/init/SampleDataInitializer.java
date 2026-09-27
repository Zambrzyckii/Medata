package com.medata.category.init;

import com.medata.category.model.TestCategory;
import com.medata.category.service.TestCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class SampleDataInitializer implements CommandLineRunner {

  private final TestCategoryService testCategoryService;

  @Override
  public void run(String... args) {
    testCategoryService.save(
        TestCategory.builder().name("Hematology").requiresFasting(false).build());
    testCategoryService.save(
        TestCategory.builder().name("Biochemistry").requiresFasting(true).build());
    testCategoryService.save(
        TestCategory.builder().name("Hormones").requiresFasting(false).build());
  }
}
