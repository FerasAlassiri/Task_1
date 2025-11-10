import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ArticlesService, ArticleResponse, CommentResponse } from '../../services/articles.service';
import { AuthService } from '../../core/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-article-detail',
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
  <div *ngIf="error()" class="error">{{ error() }}</div>
  <ng-container *ngIf="article(); else loading">
    <div class="card">
      <h2>{{ article()?.title }}</h2>
      <p><em>by {{ article()?.author }}</em></p>

      <img *ngIf="article()?.image" [src]="imageUrl()" alt="article" class="img" />

      <p style="white-space: pre-wrap">{{ article()?.body }}</p>

      <div class="row">
        <button *ngIf="isLoggedIn()" (click)="like()">👍</button>
        <button *ngIf="isLoggedIn()" (click)="dislike()">👎</button>
        <span class="muted">
          👍 {{ likes() }} · 👎 {{ dislikes() }}
        </span>
        <button *ngIf="isOwner()" class="danger" (click)="doDelete()">🗑️ Delete</button>
      </div>
    </div>

    <div class="card">
      <h3>Comments</h3>

      <form *ngIf="isLoggedIn()" (ngSubmit)="postComment()">
        <label><strong>Add comment</strong></label>
        <textarea [(ngModel)]="text" name="text" rows="3" maxlength="100" placeholder="Up to 100 characters"></textarea>
        <button>Post</button>
      </form>
      <p *ngIf="!isLoggedIn()">Login to comment.</p>

      <ul class="comments">
        <li *ngFor="let c of comments()">
          {{ c.text }} — <em>{{ c.username }}</em>
          <span class="muted">{{ (c.createdAt || '').replace('T',' ').slice(0,16) }}</span>
        </li>
        <li *ngIf="comments().length === 0" class="muted">No comments yet.</li>
      </ul>
    </div>
  </ng-container>
  <ng-template #loading><p>Loading...</p></ng-template>
  `,
  styles: [`
    .card{background:#fff;padding:12px;margin:12px 0;border-radius:6px}
    .row{display:flex;align-items:center;gap:8px}
    .img{max-width:100%;border-radius:6px;margin:.5rem 0}
    .muted{opacity:.7}
    .danger{margin-left:auto;background:#c0392b;color:#fff;border:none;padding:6px 10px;border-radius:4px}
    textarea{width:100%;margin:.5rem 0}
    .error{color:#c00}
    .comments{margin-top:12px}
  `]
})
export class ArticleDetailComponent implements OnInit {
  id!: number;
  article = signal<ArticleResponse | null>(null);
  comments = signal<CommentResponse[]>([]);
  text = '';
  error = signal('');

  likes = computed(() => this.article()?.likes ?? this.article()?.likesCount ?? 0);
  dislikes = computed(() => this.article()?.dislikes ?? this.article()?.dislikesCount ?? 0);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private api: ArticlesService,
    private auth: AuthService
  ) {}

  isLoggedIn = () => !!this.auth.token();
  currentUsername = () => this.auth.username();
  isOwner = () => !!this.currentUsername() && this.currentUsername() === this.article()?.author;

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
  }

  imageUrl(){ return this.api.imageUrl(this.id); }

  load(){
    this.error.set('');
    this.api.getById(this.id).subscribe({
      next: a => {
        this.article.set(a);
        this.api.listComments(this.id).subscribe({
          next: cs => this.comments.set(cs),
          error: () => {}
        });
      },
      error: e => this.error.set(e?.error?.message || e.message || 'Error')
    });
  }

  like(){ this.api.like(this.id).subscribe({ next: a => this.article.set(a), error: e => this.error.set(e?.error?.message || e.message) }); }
  dislike(){ this.api.dislike(this.id).subscribe({ next: a => this.article.set(a), error: e => this.error.set(e?.error?.message || e.message) }); }

  postComment(){
    if (!this.text.trim()) return;
    if (this.text.length > 100) { this.error.set('Comment must be ≤ 100 chars'); return; }
    this.api.comment(this.id, this.text).subscribe({
      next: c => { this.comments.set([c, ...this.comments()]); this.text = ''; },
      error: e => this.error.set(e?.error?.message || e.message)
    });
  }

  doDelete(){
    if (!confirm('Delete this article?')) return;
    this.api.remove(this.id).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: e => this.error.set(e?.error?.message || e.message)
    });
  }
}