import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
  <div class="card">
    <h2>Login</h2>
    <div *ngIf="error" class="error">{{ error }}</div>
    <form (ngSubmit)="submit()">
      <label><strong>Username</strong></label>
      <input [(ngModel)]="username" name="username"/>

      <label><strong>Password</strong></label>
      <input type="password" [(ngModel)]="password" name="password"/>

      <button>Sign In</button>
    </form>
    <p><a routerLink="/register">Register</a></p>
  </div>
  `,
  styles: [`.card{background:#fff;padding:12px;margin:12px 0;border-radius:6px}
            .error{color:#c00} input{width:100%;margin:.5rem 0}`]
})
export class LoginComponent {
  username = ''; password = ''; error = '';
  constructor(private auth: AuthService, private router: Router) {}
  async submit(){
    this.error = '';
    try { await this.auth.login(this.username, this.password); this.router.navigateByUrl('/'); }
    catch(e:any){ this.error = e?.error?.message || e.message || 'Error'; }
  }
}