import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Api } from '../api';

@Component({
  selector: 'app-test-create-page',
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <h2>Add test</h2>
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
export class TestCreatePage {
  private readonly api = inject(Api);
  private readonly router = inject(Router);
  readonly categoryId = inject(ActivatedRoute).snapshot.paramMap.get('categoryId')!;

  readonly form = inject(NonNullableFormBuilder).group({
    name: ['', Validators.required],
    unit: ['', Validators.required],
    referenceMin: [0, Validators.required],
    referenceMax: [0, Validators.required],
    price: [0, [Validators.required, Validators.min(0)]],
  });

  save(): void {
    if (this.form.invalid) return;
    this.api
      .createTest(this.categoryId, this.form.getRawValue())
      .subscribe(() => this.router.navigate(['/categories', this.categoryId]));
  }
}
