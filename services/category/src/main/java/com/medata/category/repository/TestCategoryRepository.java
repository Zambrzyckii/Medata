package com.medata.category.repository;

import com.medata.category.model.TestCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCategoryRepository extends JpaRepository<TestCategory, UUID> {}
