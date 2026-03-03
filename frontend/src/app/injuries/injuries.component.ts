import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InjuryService } from '../services/injury.service';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-injuries',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Injuries</h2>

      <form (ngSubmit)="save()">
        <h3>{{ editId ? 'Edit Injury' : 'New Injury' }}</h3>
        <div class="form-row" *ngIf="isAdmin">
          <label>User ID</label>
          <input type="number" [(ngModel)]="form.user" name="user" required />
        </div>
        <div class="form-row">
          <label>Injury</label>
          <input [(ngModel)]="form.injury" name="injury" required />
        </div>
        <div class="form-row">
          <label>Recovery</label>
          <input [(ngModel)]="form.recovery" name="recovery" />
        </div>
        <div class="form-actions">
          <button type="submit">{{ editId ? 'Update' : 'Create' }}</button>
          <button type="button" *ngIf="editId" (click)="cancelEdit()">Cancel</button>
        </div>
      </form>

      <table>
        <thead>
          <tr>
            <th>ID</th><th>User ID</th><th>Injury</th><th>Recovery</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let i of injuries">
            <td>{{ i.id }}</td>
            <td>{{ i.user }}</td>
            <td>{{ i.injury }}</td>
            <td>{{ i.recovery }}</td>
            <td>
              <button (click)="edit(i)">Edit</button>
              <button class="danger" (click)="delete(i.id)">Delete</button>
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
    th { background: #F44336; color: white; }
    tr:nth-child(even) { background: #f9f9f9; }
    button { padding: 4px 10px; cursor: pointer; }
    button.danger { background: #e53935; color: white; border: none; }
  `]
})
export class InjuriesComponent implements OnInit {
  injuries: any[] = [];
  editId: number | null = null;
  form: any = {};
  isAdmin: boolean = false;
  currentUserId: number | null = null;

  constructor(private injuryService: InjuryService, private userService: UserService, private auth: AuthService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    const name = this.auth.getUser()?.name;
    const id = this.auth.getUser()?.id;
    if (!name) return;
    this.currentUserId = id;
    this.userService.isAdmin(name).subscribe((isAdmin: boolean) => {
      this.isAdmin = isAdmin;
      if (isAdmin) {
        this.injuryService.getAll().subscribe(data => this.injuries = data);
      } else {
        this.injuryService.getByUser(id).subscribe(data => this.injuries = data);
      }
    });
  }

  edit(injury: any) {
    this.editId = injury.id;
    this.form = { ...injury };
  }

  cancelEdit() {
    this.editId = null;
    this.form = {};
  }

  save() {
    if (this.editId) {
      this.injuryService.update(this.editId, this.form).subscribe(() => {
        this.load();
        this.cancelEdit();
      });
    } else {
      if (!this.isAdmin) this.form.user = this.currentUserId;
      this.injuryService.create(this.form).subscribe(() => {
        this.load();
        this.form = {};
      });
    }
  }

  delete(id: number) {
    if (confirm('Delete this injury?')) {
      this.injuryService.delete(id).subscribe(() => this.load());
    }
  }
}
