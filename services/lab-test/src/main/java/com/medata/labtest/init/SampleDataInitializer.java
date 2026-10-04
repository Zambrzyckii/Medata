package com.medata.labtest.init;

import com.medata.labtest.model.LabTest;
import com.medata.labtest.model.TestCategory;
import com.medata.labtest.service.LabTestService;
import com.medata.labtest.service.TestCategoryService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class SampleDataInitializer implements CommandLineRunner {

  public static final UUID HEMATOLOGY_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  public static final UUID BIOCHEMISTRY_ID =
      UUID.fromString("22222222-2222-2222-2222-222222222222");
  public static final UUID HORMONES_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

  private final TestCategoryService testCategoryService;
  private final LabTestService labTestService;

  @Override
  public void run(String... args) {
    TestCategory hematology =
        testCategoryService.save(
            TestCategory.builder().id(HEMATOLOGY_ID).name("Hematology").build());
    TestCategory biochemistry =
        testCategoryService.save(
            TestCategory.builder().id(BIOCHEMISTRY_ID).name("Biochemistry").build());
    TestCategory hormones =
        testCategoryService.save(TestCategory.builder().id(HORMONES_ID).name("Hormones").build());

    saveTest(hematology, "Hemoglobin", "g/dL", 12.0, 17.5, "12.00");
    saveTest(hematology, "White blood cells", "10^9/L", 4.0, 10.0, "15.00");
    saveTest(biochemistry, "Glucose", "mg/dL", 70.0, 99.0, "10.00");
    saveTest(biochemistry, "Total cholesterol", "mg/dL", 115.0, 190.0, "18.00");
    saveTest(hormones, "TSH", "mIU/L", 0.27, 4.2, "30.00");
    saveTest(hormones, "Free thyroxine", "ng/dL", 0.93, 1.7, "35.00");
  }

  private void saveTest(
      TestCategory category,
      String name,
      String unit,
      double referenceMin,
      double referenceMax,
      String price) {
    LabTest labTest =
        LabTest.builder()
            .name(name)
            .unit(unit)
            .referenceMin(referenceMin)
            .referenceMax(referenceMax)
            .price(new BigDecimal(price))
            .category(category)
            .build();
    labTestService.save(labTest);
  }
}
