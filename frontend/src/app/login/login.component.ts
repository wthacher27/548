import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page">
      <div class="card">
        <h1>Fitness Tracker</h1>

        <div class="tabs">
          <button [class.active]="mode === 'login'" (click)="mode = 'login'; error = ''">Login</button>
          <button [class.active]="mode === 'register'" (click)="mode = 'register'; error = ''">Create Account</button>
        </div>

        <!-- Login Form -->
        <form *ngIf="mode === 'login'" (ngSubmit)="login()">
          <div class="field">
            <label>Name</label>
            <input [(ngModel)]="loginForm.name" name="name" placeholder="Your name" required />
          </div>
          <div class="field">
            <label>Password</label>
            <input type="password" [(ngModel)]="loginForm.password" name="password" placeholder="Password" required />
          </div>
          <p class="error" *ngIf="error">{{ error }}</p>
          <button type="submit" class="submit">Login</button>
        </form>

        <!-- Register Form -->
        <form *ngIf="mode === 'register'" (ngSubmit)="register()">
          <div class="field">
            <label>Name <span class="req">*</span></label>
            <input [(ngModel)]="regForm.name" name="name" placeholder="Full name" required />
          </div>
          <div class="field">
            <label>Password <span class="req">*</span></label>
            <input type="password" [(ngModel)]="regForm.password" name="password" placeholder="Choose a password" required />
          </div>
          <div class="field">
            <label>Age</label>
            <input type="number" [(ngModel)]="regForm.age" name="age" placeholder="Age" />
          </div>
          <div class="field">
            <label>Height (inches)</label>
            <input type="number" step="0.1" [(ngModel)]="regForm.heightIn" name="heightIn" placeholder="e.g. 70.5" />
          </div>
          <div class="field">
            <label>Weight (lbs)</label>
            <input type="number" step="0.1" [(ngModel)]="regForm.weightLbs" name="weightLbs" placeholder="e.g. 185.0" />
          </div>
          <div class="field">
            <label>Body Fat %</label>
            <input type="number" step="0.1" [(ngModel)]="regForm.bodyFat" name="bodyFat" placeholder="e.g. 18.5" />
          </div>
          <div class="field">
            <label>Experience (years)</label>
            <input type="number" [(ngModel)]="regForm.experience" name="experience" placeholder="Years training" />
          </div>
          <div class="field">
            <label>Birthday</label>
            <input type="date" [(ngModel)]="regForm.birthday" name="birthday" />
          </div>
          <p class="error" *ngIf="error">{{ error }}</p>
          <button type="submit" class="submit">Create Account</button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #1a1a2e;
    }
    .card {
      background: white;
      border-radius: 8px;
      padding: 36px 40px;
      width: 360px;
      box-shadow: 0 4px 24px rgba(0,0,0,0.3);
    }
    h1 {
      text-align: center;
      margin: 0 0 24px;
      color: #333;
      font-size: 1.5em;
    }
    .tabs {
      display: flex;
      margin-bottom: 24px;
      border-bottom: 2px solid #eee;
    }
    .tabs button {
      flex: 1;
      padding: 10px;
      border: none;
      background: none;
      cursor: pointer;
      font-size: 0.95em;
      color: #888;
      border-bottom: 2px solid transparent;
      margin-bottom: -2px;
    }
    .tabs button.active {
      color: #4CAF50;
      border-bottom-color: #4CAF50;
      font-weight: bold;
    }
    .field {
      margin-bottom: 14px;
    }
    .field label {
      display: block;
      font-size: 0.85em;
      color: #555;
      margin-bottom: 4px;
    }
    .req { color: #e53935; }
    .field input {
      width: 100%;
      padding: 8px 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 0.95em;
      box-sizing: border-box;
    }
    .field input:focus {
      outline: none;
      border-color: #4CAF50;
    }
    .submit {
      width: 100%;
      padding: 10px;
      background: #4CAF50;
      color: white;
      border: none;
      border-radius: 4px;
      font-size: 1em;
      cursor: pointer;
      margin-top: 8px;
    }
    .submit:hover { background: #43a047; }
    .error {
      color: #e53935;
      font-size: 0.85em;
      margin: 8px 0 0;
    }
  `]
})
export class LoginComponent {
  mode: 'login' | 'register' = 'login';
  error = '';

  loginForm = { name: '', password: '' };
  regForm: any = {};

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.error = '';
    this.auth.login(this.loginForm.name, this.loginForm.password).subscribe({
      next: () => this.router.navigate(['/users']),
      error: () => this.error = 'Invalid name or password.'
    });
  }

  register() {
    this.error = '';
    this.auth.register(this.regForm).subscribe({
      next: () => this.router.navigate(['/users']),
      error: () => this.error = 'Could not create account. Please try again.'
    });
  }
}
