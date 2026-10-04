import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Api } from '../api';

@Component({
  selector: 'app-category-edit-page',
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <h2>Edit category</h2>
    <form [formGroup]="form" (ngSubmit)="save()">
      <p>
        <label>Name <input formControlName="name" /></label>
      </p>
      <p>
        <label><input type="checkbox" formControlName="requiresFasting" /> Requires fasting</label>
      </p>
      <button type="submit" [disabled]="form.invalid">Save</button>
      <a routerLink="/categories">Cancel</a>
    </form>
  `,
})
export class CategoryEditPage {
  private readonly api = inject(Api);
  private readonly router = inject(Router);
  private readonly categoryId = inject(ActivatedRoute).snapshot.paramMap.get('categoryId')!;

  readonly form = inject(NonNullableFormBuilder).group({
    name: ['', Validators.required],
    requiresFasting: [false],
  });

  constructor() {
    this.api
      .getCategory(this.categoryId)
      .subscribe((category) =>
        this.form.patchValue({ name: category.name, requiresFasting: category.requiresFasting }),
      );
  }

  save(): void {
    if (this.form.invalid) return;
    this.api
      .updateCategory(this.categoryId, this.form.getRawValue())
      .subscribe(() => this.router.navigate(['/categories']));
  }
}
