import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Category, CategoryList, CategoryPayload, LabTest, TestList, TestPayload } from './models';

@Injectable({
  providedIn: 'root',
})
export class Api {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api';

  getCategories(): Observable<CategoryList> {
    return this.http.get<CategoryList>(`${this.baseUrl}/categories`);
  }

  getCategory(id: string): Observable<Category> {
    return this.http.get<Category>(`${this.baseUrl}/categories/${id}`);
  }

  createCategory(payload: CategoryPayload): Observable<Category> {
    return this.http.post<Category>(`${this.baseUrl}/categories`, payload);
  }

  updateCategory(id: string, payload: CategoryPayload): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/categories/${id}`, payload);
  }

  deleteCategory(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/categories/${id}`);
  }

  getCategoryTests(categoryId: string): Observable<TestList> {
    return this.http.get<TestList>(`${this.baseUrl}/categories/${categoryId}/tests`);
  }

  createTest(categoryId: string, payload: TestPayload): Observable<LabTest> {
    return this.http.post<LabTest>(`${this.baseUrl}/categories/${categoryId}/tests`, payload);
  }

  getTest(id: string): Observable<LabTest> {
    return this.http.get<LabTest>(`${this.baseUrl}/tests/${id}`);
  }

  updateTest(id: string, payload: TestPayload): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/tests/${id}`, payload);
  }

  deleteTest(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tests/${id}`);
  }
}
