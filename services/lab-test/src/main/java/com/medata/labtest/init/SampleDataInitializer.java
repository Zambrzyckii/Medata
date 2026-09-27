package com.medata.labtest.init;

import com.medata.labtest.model.LabTest;
import com.medata.labtest.model.TestCategory;
import com.medata.labtest.service.LabTestService;
import com.medata.labtest.service.TestCategoryService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class SampleDataInitializer implements CommandLineRunner {

  private final TestCategoryService testCategoryService;
  private final LabTestService labTestService;

  @Override
  public void run(String... args) {
    TestCategory hematology =
        TestCategory.builder().name("Hematology").requiresFasting(false).build();
    TestCategory biochemistry =
        TestCategory.builder().name("Biochemistry").requiresFasting(true).build();
    TestCategory hormones = TestCategory.builder().name("Hormones").requiresFasting(false).build();
    testCategoryService.save(hematology);
    testCategoryService.save(biochemistry);
    testCategoryService.save(hormones);

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
            .build();
    category.addLabTest(labTest);
    labTestService.save(labTest);
  }
}
