import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

export interface PageResponse<T> {
  pageNumber: number; pageSize: number; totalElements: number; totalPages: number;
  content: T[];
}
export interface ArticleResponse {
  id: number; title: string; body: string; author: string;
  createdAt?: string; likes?: number; dislikes?: number;
  likesCount?: number; dislikesCount?: number; disabled?: boolean; image?: any;
}
export interface CommentResponse { id:number; text:string; username:string; createdAt:string; }

@Injectable({ providedIn: 'root' })
export class ArticlesService {
  private base = environment.apiBase; // API base URL

  constructor(private http: HttpClient) {}

  // Paged list of public (enabled) articles
  list(page = 0, size = 5) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<ArticleResponse>>(`${this.base}/article`, { params });
  }

  // Get a single article by id
  getById(id: number) {
    return this.http.get<ArticleResponse>(`${this.base}/article/${id}`);
  }

  // Create article with optional image (multipart/form-data)
  create(payload: { title: string; body: string; imageFile?: File | null }) {
    const form = new FormData();
    form.append(
      'payload',
      new Blob([JSON.stringify({ title: payload.title, body: payload.body })], { type: 'application/json' })
    );
    if (payload.imageFile) form.append('image', payload.imageFile);
    return this.http.post<ArticleResponse>(`${this.base}/article`, form);
  }

  // Add a new comment
  comment(articleId: number, text: string) {
    return this.http.post<CommentResponse>(`${this.base}/article/${articleId}/comment`, { text });
  }

  // Reactions
  like(id: number)    { return this.http.put<ArticleResponse>(`${this.base}/article/${id}/like`, {}); }
  dislike(id: number) { return this.http.put<ArticleResponse>(`${this.base}/article/${id}/dislike`, {}); }

  // Delete (owner-only server-side)
  remove(id: number) { return this.http.delete<void>(`${this.base}/article/${id}`); }

  // Image endpoint URL for <img [src]>
  imageUrl(id: number) { return `${this.base}/article/${id}/image`; }

  // List comments for an article
  listComments(id: number) {
    return this.http.get<CommentResponse[]>(`${this.base}/article/${id}/comment`);
  }
}
