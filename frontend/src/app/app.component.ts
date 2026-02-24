import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav *ngIf="auth.isLoggedIn()">
      <span class="brand">Fitness Tracker</span>
      <a routerLink="/users" routerLinkActive="active">Users</a>
      <a routerLink="/workouts" routerLinkActive="active">Workouts</a>
      <a routerLink="/exercises" routerLinkActive="active">Exercises</a>
      <a routerLink="/goals" routerLinkActive="active">Goals</a>
      <a routerLink="/injuries" routerLinkActive="active">Injuries</a>
      <span class="spacer"></span>
      <span class="user-name">{{ auth.getUser()?.name }}</span>
      <button class="logout" (click)="logout()">Logout</button>
    </nav>
    <router-outlet></router-outlet>
  `,
  styles: [`
    nav {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 12px 20px;
      background: #333;
      color: white;
    }
    .brand {
      font-weight: bold;
      font-size: 1.1em;
      margin-right: 16px;
    }
    nav a {
      color: #ccc;
      text-decoration: none;
      padding: 4px 8px;
      border-radius: 4px;
    }
    nav a.active, nav a:hover {
      color: white;
      background: #555;
    }
    .spacer { flex: 1; }
    .user-name {
      color: #aaa;
      font-size: 0.9em;
    }
    .logout {
      background: #e53935;
      color: white;
      border: none;
      padding: 5px 12px;
      border-radius: 4px;
      cursor: pointer;
    }
    .logout:hover { background: #c62828; }
  `]
})
export class AppComponent {
  constructor(public auth: AuthService, private router: Router) {}

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
