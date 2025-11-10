import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ArticlesService } from '../../services/articles.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-new-article',
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card">
    <h2>Write Article</h2>
    <div *ngIf="error" class="error">{{ error }}</div>
    <form (ngSubmit)="submit()">
      <label><strong>Title</strong></label>
      <input [(ngModel)]="title" name="title"/>

      <label><strong>Content</strong></label>
      <textarea [(ngModel)]="body" name="body" rows="10"></textarea>

      <button>Publish</button>
    </form>
  </div>
  `,
  styles: [`.card{background:#fff;padding:12px;margin:12px 0;border-radius:6px}
            .error{color:#c00} input,textarea{width:100%;margin:.5rem 0}`]
})
export class NewArticleComponent {
  title = ''; body = ''; imageFile: File | null = null; error = '';

  constructor(private api: ArticlesService, private router: Router){}

  onFile(e: Event){
    const input = e.target as HTMLInputElement;
    this.imageFile = input.files?.[0] || null;
  }

  submit(){
    this.error = '';
    this.api.create({ title: this.title, body: this.body, imageFile: this.imageFile })
      .subscribe({
        next: a => this.router.navigate(['/articles', a.id]),
        error: e => this.error = e?.error?.message || e.message || 'Error'
      });
  }
}