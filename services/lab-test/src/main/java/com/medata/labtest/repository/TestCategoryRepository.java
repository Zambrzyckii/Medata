package com.medata.labtest.repository;

import com.medata.labtest.model.TestCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCategoryRepository extends JpaRepository<TestCategory, UUID> {}
