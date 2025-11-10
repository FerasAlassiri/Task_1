import { Routes } from '@angular/router';

import { ArticlesComponent } from './pages/articles/articles.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { NewArticleComponent } from './pages/new-article/new-article.component';

export const routes: Routes = [
  { path: '', component: ArticlesComponent },
  { path: 'articles/:id', component: ArticleDetailComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'new', component: NewArticleComponent },
  { path: '**', redirectTo: '' }
];