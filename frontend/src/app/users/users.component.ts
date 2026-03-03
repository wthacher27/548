import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Users</h2>

      <form *ngIf="isAdmin || editId" (ngSubmit)="save()" #f="ngForm">
        <h3>{{ editId ? 'Edit User' : 'New User' }}</h3>
        <div class="form-row">
          <label>Username</label>
          <input [(ngModel)]="form.name" name="name" required [disabled]="!!editId" />
        </div>
        <div class="form-row">
          <label>Age</label>
          <input type="number" [(ngModel)]="form.age" name="age" />
        </div>
        <div class="form-row">
          <label>Height (in)</label>
          <input type="number" step="0.1" [(ngModel)]="form.heightIn" name="heightIn" />
        </div>
        <div class="form-row">
          <label>Weight (lbs)</label>
          <input type="number" step="0.1" [(ngModel)]="form.weightLbs" name="weightLbs" />
        </div>
        <div class="form-row">
          <label>Body Fat %</label>
          <input type="number" step="0.1" [(ngModel)]="form.bodyFat" name="bodyFat" />
        </div>
        <div class="form-row">
          <label>Experience (yrs)</label>
          <input type="number" [(ngModel)]="form.experience" name="experience" />
        </div>
        <div class="form-row">
          <label>Birthday</label>
          <input type="date" [(ngModel)]="form.birthday" name="birthday" />
        </div>
        <div class="form-row">
          <label>Password</label>
          <input type="password" [(ngModel)]="form.password" name="password" required />
        </div>
        <div class="form-actions">
          <button type="submit">{{ editId ? 'Update' : 'Create' }}</button>
          <button type="button" *ngIf="editId" (click)="cancelEdit()">Cancel</button>
        </div>
      </form>

      <table>
        <thead>
          <tr>
            <th>ID</th><th>Username</th><th>Age</th><th>Height</th><th>Weight</th><th>Body Fat</th><th>Experience</th><th>Birthday</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let u of users">
            <td>{{ u.id }}</td>
            <td>{{ u.name }}</td>
            <td>{{ u.age }}</td>
            <td>{{ u.heightIn }}"</td>
            <td>{{ u.weightLbs }} lbs</td>
            <td>{{ u.bodyFat }}%</td>
            <td>{{ u.experience }} yrs</td>
            <td>{{ u.birthday }}</td>
            <td>
              <button (click)="edit(u)">Edit</button>
              <button class="danger" (click)="delete(u.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: [`
    .container { padding: 20px; }
    form { background: #f5f5f5; padding: 16px; border-radius: 4px; margin-bottom: 20px; max-width: 500px; }
    .form-row { display: flex; align-items: center; margin-bottom: 8px; gap: 8px; }
    .form-row label { width: 120px; font-weight: bold; }
    .form-row input { flex: 1; padding: 4px 8px; }
    .form-actions { margin-top: 12px; display: flex; gap: 8px; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background: #4CAF50; color: white; }
    tr:nth-child(even) { background: #f9f9f9; }
    button { padding: 4px 10px; cursor: pointer; }
    button.danger { background: #e53935; color: white; border: none; }
  `]
})
export class UsersComponent implements OnInit {
  users: any[] = [];
  editId: number | null = null;
  form: any = {};
  isAdmin: boolean = false;

  constructor(private userService: UserService, private auth: AuthService, private router: Router) {}

  ngOnInit() {
    this.load();
  }

  load() {
    const name = this.auth.getUser()?.name;
    const id = this.auth.getUser()?.id;
    if (!name) return;
    this.userService.isAdmin(name).subscribe((isAdmin: boolean) => {
      this.isAdmin = isAdmin;
      if (isAdmin) {
        this.userService.getAll().subscribe(data => this.users = data);
      } else {
        this.userService.getUser(id).subscribe(data => this.users = [data]);
      }
    });
  }

  edit(user: any) {
    this.editId = user.id;
    this.form = { ...user };
  }

  cancelEdit() {
    this.editId = null;
    this.form = {};
  }

  save() {
    if (this.editId) {
      this.userService.update(this.editId, this.form).subscribe({
        next: () => {
          this.load();
          this.cancelEdit();
        },
        error: (err) => {
          console.error('Update failed', err);
          alert('Update failed: ' + (err.error?.message || JSON.stringify(err.error) || err.message));
        }
      });
    } else {
      this.userService.create(this.form).subscribe({
        next: () => {
          this.load();
          this.form = {};
        },
        error: (err) => {
          console.error('Create failed', err);
          alert('Create failed: ' + (err.error || err.message));
        }
      });
    }
  }

  delete(id: number) {
    if (confirm('Delete this user?')) {
      this.userService.delete(id).subscribe(() => this.load());
      if(!this.isAdmin) {
        this.auth.logout();
        this.router.navigate(['/login']);
      }
      
    }
  }
}
