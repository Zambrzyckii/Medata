import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Api } from '../api';

@Component({
  selector: 'app-test-edit-page',
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <h2>Edit test</h2>
    <form [formGroup]="form" (ngSubmit)="save()">
      <p>
        <label>Name <input formControlName="name" /></label>
      </p>
      <p>
        <label>Unit <input formControlName="unit" /></label>
      </p>
      <p>
        <label
          >Reference min <input type="number" step="0.01" formControlName="referenceMin"
        /></label>
      </p>
      <p>
        <label
          >Reference max <input type="number" step="0.01" formControlName="referenceMax"
        /></label>
      </p>
      <p>
        <label>Price <input type="number" step="0.01" formControlName="price" /></label>
      </p>
      <button type="submit" [disabled]="form.invalid">Save</button>
      <a [routerLink]="['/categories', categoryId]">Cancel</a>
    </form>
  `,
})
export class TestEditPage {
  private readonly api = inject(Api);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  readonly categoryId = this.route.snapshot.paramMap.get('categoryId')!;
  private readonly testId = this.route.snapshot.paramMap.get('testId')!;

  readonly form = inject(NonNullableFormBuilder).group({
    name: ['', Validators.required],
    unit: ['', Validators.required],
    referenceMin: [0, Validators.required],
    referenceMax: [0, Validators.required],
    price: [0, [Validators.required, Validators.min(0)]],
  });

  constructor() {
    this.api.getTest(this.testId).subscribe((test) =>
      this.form.patchValue({
        name: test.name,
        unit: test.unit,
        referenceMin: test.referenceMin,
        referenceMax: test.referenceMax,
        price: test.price,
      }),
    );
  }

  save(): void {
    if (this.form.invalid) return;
    this.api
      .updateTest(this.testId, this.form.getRawValue())
      .subscribe(() => this.router.navigate(['/categories', this.categoryId]));
  }
}
