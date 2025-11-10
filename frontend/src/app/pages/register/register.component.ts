import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card">
    <h2>Register</h2>
    <div *ngIf="error" class="error">{{ error }}</div>
    <form (ngSubmit)="submit()">
      <label><strong>Username</strong></label>
      <input [(ngModel)]="username" name="username"/>

      <label><strong>Email</strong></label>
      <input type="email" [(ngModel)]="email" name="email"/>

      <label><strong>Mobile Number</strong></label>
      <input [(ngModel)]="mobileNumber" name="mobileNumber" placeholder="+9665xxxxxxx"/>

      <label><strong>Password</strong></label>
      <input type="password" [(ngModel)]="password" name="password"/>

      <button>Create Account</button>
    </form>
  </div>
  `,
  styles: [`.card{background:#fff;padding:12px;margin:12px 0;border-radius:6px}
            .error{color:#c00} input{width:100%;margin:.5rem 0}`]
})
export class RegisterComponent {
  username = ''; email = ''; mobileNumber = ''; password = ''; error = '';
  constructor(private auth: AuthService, private router: Router){}
  submit(){
    if (this.password.length < 8) { this.error = 'Password must be at least 8 chars'; return; }
    this.error = '';
    this.auth.register({ username: this.username, email: this.email, password: this.password, mobileNumber: this.mobileNumber })
      .subscribe({
        next: () => this.router.navigateByUrl('/login'),
        error: (e:any) => this.error = e?.error?.message || e.message || 'Error'
      });
  }
}