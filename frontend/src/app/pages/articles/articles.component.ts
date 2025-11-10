import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ArticlesService, PageResponse, ArticleResponse } from '../../services/articles.service';
import { PaginationComponent } from '../../shared/pagination/pagination.component';

@Component({
  standalone: true,
  selector: 'app-articles',
  imports: [CommonModule, RouterLink, PaginationComponent],
  template: `
  <h2>Articles</h2>

  <div *ngIf="error" class="error">{{ error }}</div>

  <div *ngFor="let a of resp?.content" class="card">
    <h3><a [routerLink]="['/articles', a.id]">{{ a.title }}</a></h3>
    <p>{{ (a.body || '').slice(0,160) }}{{ (a.body?.length||0) > 160 ? '…' : '' }}</p>
    <p class="muted">by {{ a.author }}</p>
  </div>

  <app-pagination
    [page]="page"
    [totalPages]="resp?.totalPages || 1"
    (pageChange)="onPage($event)">
  </app-pagination>
  `,
  styles: [`.card{background:#fff;padding:12px;margin:12px 0;border-radius:6px}
            .muted{opacity:.7;font-size:12px}
            .error{color:#c00}`]
})
export class ArticlesComponent implements OnInit {
  page = 0;
  resp?: PageResponse<ArticleResponse>;
  error = '';

  constructor(private api: ArticlesService) {}

  ngOnInit(){ this.load(); }

  load(){
    this.error = '';
    this.api.list(this.page, 5).subscribe({
      next: r => this.resp = r,
      error: e => this.error = e?.error?.message || e.message || 'Error'
    });
  }

  onPage(p: number){ this.page = p; this.load(); }
}