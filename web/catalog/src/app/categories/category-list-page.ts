import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Api } from '../api';
import { CategorySummary } from '../models';

@Component({
  selector: 'app-category-list-page',
  imports: [RouterLink],
  template: `
    <h2>Test categories</h2>
    <p><a routerLink="/categories/new">Add category</a></p>
    <ul>
      @for (category of categories(); track category.id) {
        <li>
          <a [routerLink]="['/categories', category.id]">{{ category.name }}</a>
          <a [routerLink]="['/categories', category.id, 'edit']">edit</a>
          <button type="button" (click)="remove(category)">delete</button>
        </li>
      } @empty {
        <li>No categories.</li>
      }
    </ul>
  `,
})
export class CategoryListPage {
  private readonly api = inject(Api);
  readonly categories = signal<CategorySummary[]>([]);

  constructor() {
    this.load();
  }

  private load(): void {
    this.api.getCategories().subscribe((list) => this.categories.set(list.categories));
  }

  remove(category: CategorySummary): void {
    this.api.deleteCategory(category.id).subscribe(() => this.load());
  }
}
