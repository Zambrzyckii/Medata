package com.medata.catalog.repository;

import com.medata.catalog.model.TestCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCategoryRepository extends JpaRepository<TestCategory, UUID> {}
