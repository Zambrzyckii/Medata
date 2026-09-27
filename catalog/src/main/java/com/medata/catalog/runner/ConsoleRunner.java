package com.medata.catalog.runner;

import com.medata.catalog.model.LabTest;
import com.medata.catalog.model.TestCategory;
import com.medata.catalog.service.LabTestService;
import com.medata.catalog.service.TestCategoryService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {

  private final TestCategoryService testCategoryService;
  private final LabTestService labTestService;

  @Override
  public void run(String... args) {
    Scanner scanner = new Scanner(System.in);
    printHelp();
    boolean running = true;
    while (running) {
      System.out.print("> ");
      String command = scanner.nextLine().trim();
      switch (command) {
        case "help" -> printHelp();
        case "categories" -> listCategories();
        case "tests" -> listTests();
        case "add" -> addTest(scanner);
        case "delete" -> deleteTest(scanner);
        case "stop" -> running = false;
        default -> System.out.println("Unknown command, type 'help'");
      }
    }
    System.out.println("Bye");
  }

  private void printHelp() {
    System.out.println(
        """
      Available commands:
        help       - print this list
        categories - list all categories
        tests      - list all lab tests
        add        - add a new lab test to a category
        delete     - delete a lab test
        stop       - stop the application
      """);
  }

  private void listCategories() {
    testCategoryService.findAll().stream().sorted().forEach(System.out::println);
  }

  private void listTests() {
    labTestService.findAll().stream().sorted().forEach(System.out::println);
  }

  private void addTest(Scanner scanner) {
    List<TestCategory> categories = testCategoryService.findAll();
    for (int i = 0; i < categories.size(); i++) {
      System.out.println(i + ": " + categories.get(i).getName());
    }
    System.out.print("Category number: ");
    try {
      int index = Integer.parseInt(scanner.nextLine().trim());
      if (index < 0 || index >= categories.size()) {
        System.out.println("No such category");
        return;
      }
      TestCategory category = categories.get(index);
      System.out.print("Name: ");
      String name = scanner.nextLine().trim();
      System.out.print("Unit: ");
      String unit = scanner.nextLine().trim();
      System.out.print("Reference min: ");
      double referenceMin = Double.parseDouble(scanner.nextLine().trim());
      System.out.print("Reference max: ");
      double referenceMax = Double.parseDouble(scanner.nextLine().trim());
      System.out.print("Price: ");
      BigDecimal price = new BigDecimal(scanner.nextLine().trim());
      LabTest labTest =
          LabTest.builder()
              .name(name)
              .unit(unit)
              .referenceMin(referenceMin)
              .referenceMax(referenceMax)
              .price(price)
              .category(category)
              .build();
      labTestService.save(labTest);
      System.out.println("Saved: " + labTest);
    } catch (NumberFormatException e) {
      System.out.println("Invalid number, aborting");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  private void deleteTest(Scanner scanner) {
    List<LabTest> tests = labTestService.findAll();
    for (int i = 0; i < tests.size(); i++) {
      System.out.println(i + ": " + tests.get(i).getName());
    }
    System.out.print("Test number: ");
    try {
      int index = Integer.parseInt(scanner.nextLine().trim());
      if (index < 0 || index >= tests.size()) {
        System.out.println("No such test");
        return;
      }
      labTestService.deleteById(tests.get(index).getId());
      System.out.println("Deleted");
    } catch (NumberFormatException e) {
      System.out.println("Invalid number, aborting");
    }
  }
}
