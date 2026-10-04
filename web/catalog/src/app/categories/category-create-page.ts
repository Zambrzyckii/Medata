import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Api } from '../api';

@Component({
  selector: 'app-category-create-page',
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <h2>Add category</h2>
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
export class CategoryCreatePage {
  private readonly api = inject(Api);
  private readonly router = inject(Router);
  readonly form = inject(NonNullableFormBuilder).group({
    name: ['', Validators.required],
    requiresFasting: [false],
  });

  save(): void {
    if (this.form.invalid) return;
    this.api
      .createCategory(this.form.getRawValue())
      .subscribe(() => this.router.navigate(['/categories']));
  }
}
