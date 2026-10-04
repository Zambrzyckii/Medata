import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Api } from '../api';
import { Category, TestSummary } from '../models';

@Component({
  selector: 'app-category-details-page',
  imports: [RouterLink],
  template: `
    <h2>Category details</h2>
    @if (category(); as category) {
      <p>Name: {{ category.name }}</p>
      <p>Requires fasting: {{ category.requiresFasting ? 'yes' : 'no' }}</p>
    }
    <h3>Tests</h3>
    <p><a [routerLink]="['/categories', categoryId, 'tests', 'new']">Add test</a></p>
    <ul>
      @for (test of tests(); track test.id) {
        <li>
          <a [routerLink]="['/categories', categoryId, 'tests', test.id]">{{ test.name }}</a>
          <a [routerLink]="['/categories', categoryId, 'tests', test.id, 'edit']">edit</a>
          <button type="button" (click)="remove(test)">delete</button>
        </li>
      } @empty {
        <li>No tests in this category.</li>
      }
    </ul>
    <p><a routerLink="/categories">Back to categories</a></p>
  `,
})
export class CategoryDetailsPage {
  private readonly api = inject(Api);
  readonly categoryId = inject(ActivatedRoute).snapshot.paramMap.get('categoryId')!;
  readonly category = signal<Category | undefined>(undefined);
  readonly tests = signal<TestSummary[]>([]);

  constructor() {
    this.api.getCategory(this.categoryId).subscribe((category) => this.category.set(category));
    this.loadTests();
  }

  private loadTests(): void {
    this.api.getCategoryTests(this.categoryId).subscribe((list) => this.tests.set(list.tests));
  }

  remove(test: TestSummary): void {
    this.api.deleteTest(test.id).subscribe(() => this.loadTests());
  }
}
