package com.medata.catalog;

import com.medata.catalog.dto.LabTestDto;
import com.medata.catalog.model.LabTest;
import com.medata.catalog.model.TestCategory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) throws Exception {
    List<TestCategory> categories = createSampleData();
    printCategories(categories);
    Set<LabTest> allTests =
        categories.stream()
            .flatMap(category -> category.getLabTests().stream())
            .collect(Collectors.toSet());
    System.out.println("== All tests (unique set) ==");
    allTests.stream().forEach(System.out::println);

    System.out.println("== Tests cheaper than 20.00, sorted by name ==");
    allTests.stream()
        .filter(labTest -> labTest.getPrice().compareTo(new BigDecimal("20.00")) < 0)
        .sorted(Comparator.comparing(LabTest::getName))
        .forEach(System.out::println);

    System.out.println("== Test DTOs in natural order ==");
    List<LabTestDto> testDtos =
        allTests.stream().map(LabTestDto::fromEntity).sorted().collect(Collectors.toList());
    testDtos.stream().forEach(System.out::println);
    Path file = Path.of("categories.bin");
    saveCategories(categories, file);
    List<TestCategory> loaded = loadCategories(file);
    System.out.println("== Categories loaded from " + file + " ==");
    printCategories(loaded);
    System.out.println("== Parallel processing, pool size 1 ==");
    processCategoriesInParallel(categories, 1);
    System.out.println("== Parallel processing, pool size 3 ==");
    processCategoriesInParallel(categories, 3);
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

  private static void saveCategories(List<TestCategory> categories, Path file) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
      out.writeObject(categories);
    }
  }

  @SuppressWarnings("unchecked")
  private static List<TestCategory> loadCategories(Path file)
      throws IOException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
      return (List<TestCategory>) in.readObject();
    }
  }

  private static void processCategoriesInParallel(List<TestCategory> categories, int parallelism)
      throws Exception {
    long start = System.currentTimeMillis();
    try (ForkJoinPool pool = new ForkJoinPool(parallelism)) {
      pool.submit(() -> categories.parallelStream().forEach(Main::printTestsSlowly)).get();
    }
    System.out.println(
        "Pool size " + parallelism + " took " + (System.currentTimeMillis() - start) + " ms");
  }

  private static void printTestsSlowly(TestCategory category) {
    category
        .getLabTests()
        .forEach(
            labTest -> {
              System.out.println(Thread.currentThread().getName() + " -> " + labTest.getName());
              try {
                Thread.sleep(500);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
            });
  }
}
