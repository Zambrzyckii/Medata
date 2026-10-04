export interface CategorySummary {
  id: string;
  name: string;
}

export interface CategoryList {
  categories: CategorySummary[];
}

export interface Category {
  id: string;
  name: string;
  requiresFasting: boolean;
}

export interface CategoryPayload {
  name: string;
  requiresFasting: boolean;
}

export interface TestSummary {
  id: string;
  name: string;
}

export interface TestList {
  tests: TestSummary[];
}

export interface LabTest {
  id: string;
  name: string;
  unit: string;
  referenceMin: number;
  referenceMax: number;
  price: number;
  category: string;
}

export interface TestPayload {
  name: string;
  unit: string;
  referenceMin: number;
  referenceMax: number;
  price: number;
}
