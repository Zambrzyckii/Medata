package com.medata.catalog.dto;

import java.math.BigDecimal;

public record LabTestCreateUpdateDto(
    String name, String unit, double referenceMin, double referenceMax, BigDecimal price) {}
