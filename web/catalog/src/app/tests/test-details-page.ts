import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Api } from '../api';
import { LabTest } from '../models';

@Component({
  selector: 'app-test-details-page',
  imports: [RouterLink],
  template: `
    <h2>Test details</h2>
    @if (test(); as test) {
      <p>Name: {{ test.name }}</p>
      <p>Unit: {{ test.unit }}</p>
      <p>Reference range: {{ test.referenceMin }} – {{ test.referenceMax }}</p>
      <p>Price: {{ test.price }}</p>
      <p>Category: {{ test.category }}</p>
    }
    <p>
      <a [routerLink]="['/categories', categoryId, 'tests', testId, 'edit']">Edit</a>
      <a [routerLink]="['/categories', categoryId]">Back to category</a>
    </p>
  `,
})
export class TestDetailsPage {
  private readonly api = inject(Api);
  private readonly route = inject(ActivatedRoute);
  readonly categoryId = this.route.snapshot.paramMap.get('categoryId')!;
  readonly testId = this.route.snapshot.paramMap.get('testId')!;
  readonly test = signal<LabTest | undefined>(undefined);

  constructor() {
    this.api.getTest(this.testId).subscribe((test) => this.test.set(test));
  }
}
