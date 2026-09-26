package com.medata.catalog;

import com.medata.catalog.model.LabTest;
import com.medata.catalog.model.TestCategory;
import java.math.BigDecimal;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<TestCategory> categories = createSampleData();
    printCategories(categories);
  }

  private static List<TestCategory> createSampleData() {
    TestCategory hematology =
        TestCategory.builder().name("Hematology").requiresFasting(false).build();
    TestCategory biochemistry =
        TestCategory.builder().name("Biochemistry").requiresFasting(true).build();
    TestCategory hormones = TestCategory.builder().name("Hormones").requiresFasting(false).build();

    hematology.addLabTest(
        LabTest.builder()
            .name("Hemoglobin")
            .unit("g/dL")
            .referenceMin(12.0)
            .referenceMax(17.5)
            .price(new BigDecimal("12.00"))
            .build());
    hematology.addLabTest(
        LabTest.builder()
            .name("White blood cells")
            .unit("10^9/L")
            .referenceMin(4.0)
            .referenceMax(10.0)
            .price(new BigDecimal("15.00"))
            .build());
    biochemistry.addLabTest(
        LabTest.builder()
            .name("Glucose")
            .unit("mg/dL")
            .referenceMin(70.0)
            .referenceMax(99.0)
            .price(new BigDecimal("10.00"))
            .build());
    biochemistry.addLabTest(
        LabTest.builder()
            .name("Total cholesterol")
            .unit("mg/dL")
            .referenceMin(115.0)
            .referenceMax(190.0)
            .price(new BigDecimal("18.00"))
            .build());
    hormones.addLabTest(
        LabTest.builder()
            .name("TSH")
            .unit("mIU/L")
            .referenceMin(0.27)
            .referenceMax(4.2)
            .price(new BigDecimal("30.00"))
            .build());
    hormones.addLabTest(
        LabTest.builder()
            .name("Free thyroxine")
            .unit("ng/dL")
            .referenceMin(0.93)
            .referenceMax(1.7)
            .price(new BigDecimal("35.00"))
            .build());

    return List.of(hematology, biochemistry, hormones);
  }

  private static void printCategories(List<TestCategory> categories) {
    System.out.println("== All categories with tests ==");
    categories.forEach(
        category -> {
          System.out.println(category);
          category.getLabTests().forEach(labTest -> System.out.println("  " + labTest));
        });
  }
}
