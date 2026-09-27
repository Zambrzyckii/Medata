package com.medata.catalog.repository;

import com.medata.catalog.model.LabTest;
import com.medata.catalog.model.TestCategory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, UUID> {

  List<LabTest> findAllByCategory(TestCategory category);
}
