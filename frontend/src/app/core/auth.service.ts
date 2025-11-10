import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

type LoginResp = { name?: string; auths?: string[]; username?: string; authorities?: string[] };

@Injectable({ providedIn: 'root' })
export class AuthService {
  private basicKey = 'basicCreds'; // stores Base64 credentials
  private userKey = 'user';        // stores user info and roles

  constructor(private http: HttpClient) {}

  // Retrieve stored Basic Auth token
  token(): string | null {
    return localStorage.getItem(this.basicKey);
  }

  // Extract username from stored data or decode from token
  username(): string | null {
    const raw = localStorage.getItem(this.userKey);
    const u = raw ? JSON.parse(raw) : null;
    if (u?.username) return u.username;
    if (u?.name) return u.name;

    const t = this.token();
    if (!t) return null;
    try { return atob(t).split(':')[0] ?? null; } catch { return null; }
  }

  // Get stored roles or authorities
  roles(): string[] {
    const raw = localStorage.getItem(this.userKey);
    const u = raw ? JSON.parse(raw) : null;
    return u?.roles ?? u?.auths ?? u?.authorities ?? [];
  }

  // Basic Auth login; stores credentials and user info
  async login(username: string, password: string): Promise<void> {
    const basic = btoa(`${username}:${password}`);
    const headers = new HttpHeaders().set('Authorization', `Basic ${basic}`);
    const data = await this.http.get<LoginResp>(`${environment.apiBase}/login`, { headers }).toPromise();
    const normalized = {
      username: data?.username ?? data?.name ?? username,
      roles: data?.auths ?? data?.authorities ?? []
    };
    localStorage.setItem(this.basicKey, basic);
    localStorage.setItem(this.userKey, JSON.stringify(normalized));
  }

  // Register new user
  register(payload: { username: string; email: string; password: string; mobileNumber: string }) {
    return this.http.post(`${environment.apiBase}/user`, payload);
  }

  // Clear stored credentials and user info
  logout() {
    localStorage.removeItem(this.basicKey);
    localStorage.removeItem(this.userKey);
  }
}
