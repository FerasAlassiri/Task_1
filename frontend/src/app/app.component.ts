import { Component, computed } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet, NgIf],
  template: `
  <header class="header">
    <h1><a routerLink="/">Articles house</a></h1>
    <nav>
      <a routerLink="/">Articles</a>
      <a *ngIf="isLoggedIn()" routerLink="/new">Write</a>
      <a *ngIf="!isLoggedIn()" routerLink="/login">Login</a>
      <a *ngIf="!isLoggedIn()" routerLink="/register">Register</a>
      <button *ngIf="isLoggedIn()" (click)="logout()">
        Logout ({{ username() || '' }})
      </button>
    </nav>
  </header>
  <main class="container">
    <router-outlet />
  </main>
  `,
  styles: [`
    .header{background:#6c7a99;color:#fff;padding:12px}
    .header a{color:#fff;margin-right:12px}
    .header button{margin-left:12px}
    .container{max-width:900px;margin:24px auto;padding:0 16px}
  `]
})
export class AppComponent {
  username = computed(() => this.auth.username());
  isLoggedIn = () => !!this.auth.token();
  constructor(private auth: AuthService) {}
  logout(){ this.auth.logout(); }
}