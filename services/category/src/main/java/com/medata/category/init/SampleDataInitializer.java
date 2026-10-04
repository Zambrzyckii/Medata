package com.medata.category.init;

import com.medata.category.model.TestCategory;
import com.medata.category.service.TestCategoryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class SampleDataInitializer implements CommandLineRunner {

  private final TestCategoryService testCategoryService;
  public static final UUID HEMATOLOGY_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  public static final UUID BIOCHEMISTRY_ID =
      UUID.fromString("22222222-2222-2222-2222-222222222222");
  public static final UUID HORMONES_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

  @Override
  public void run(String... args) {
    testCategoryService.save(
        TestCategory.builder().id(HEMATOLOGY_ID).name("Hematology").requiresFasting(false).build());
    testCategoryService.save(
        TestCategory.builder()
            .id(BIOCHEMISTRY_ID)
            .name("Biochemistry")
            .requiresFasting(true)
            .build());
    testCategoryService.save(
        TestCategory.builder().id(HORMONES_ID).name("Hormones").requiresFasting(false).build());
  }
}
