import { Routes } from '@angular/router';
import { CategoryCreatePage } from './categories/category-create-page';
import { CategoryDetailsPage } from './categories/category-details-page';
import { CategoryEditPage } from './categories/category-edit-page';
import { CategoryListPage } from './categories/category-list-page';
import { TestCreatePage } from './tests/test-create-page';
import { TestDetailsPage } from './tests/test-details-page';
import { TestEditPage } from './tests/test-edit-page';

export const routes: Routes = [
  { path: 'categories', component: CategoryListPage },
  { path: 'categories/new', component: CategoryCreatePage },
  { path: 'categories/:categoryId/edit', component: CategoryEditPage },
  { path: 'categories/:categoryId/tests/new', component: TestCreatePage },
  { path: 'categories/:categoryId/tests/:testId/edit', component: TestEditPage },
  { path: 'categories/:categoryId/tests/:testId', component: TestDetailsPage },
  { path: 'categories/:categoryId', component: CategoryDetailsPage },
  { path: '', pathMatch: 'full', redirectTo: 'categories' },
];
