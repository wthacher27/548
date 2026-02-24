import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GoalService } from '../services/goal.service';

@Component({
  selector: 'app-goals',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Goals</h2>

      <form (ngSubmit)="save()">
        <h3>{{ editId ? 'Edit Goal' : 'New Goal' }}</h3>
        <div class="form-row">
          <label>User ID</label>
          <input type="number" [(ngModel)]="form.user" name="user" required />
        </div>
        <div class="form-row">
          <label>Goal</label>
          <input [(ngModel)]="form.goal" name="goal" required />
        </div>
        <div class="form-row">
          <label>Progress (0–1)</label>
          <input type="number" step="0.01" min="0" max="1" [(ngModel)]="form.progress" name="progress" />
        </div>
        <div class="form-actions">
          <button type="submit">{{ editId ? 'Update' : 'Create' }}</button>
          <button type="button" *ngIf="editId" (click)="cancelEdit()">Cancel</button>
        </div>
      </form>

      <table>
        <thead>
          <tr>
            <th>ID</th><th>User ID</th><th>Goal</th><th>Progress</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let g of goals">
            <td>{{ g.id }}</td>
            <td>{{ g.user }}</td>
            <td>{{ g.goal }}</td>
            <td>{{ (g.progress * 100).toFixed(0) }}%</td>
            <td>
              <button (click)="edit(g)">Edit</button>
              <button class="danger" (click)="delete(g.id)">Delete</button>
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
    th { background: #FF9800; color: white; }
    tr:nth-child(even) { background: #f9f9f9; }
    button { padding: 4px 10px; cursor: pointer; }
    button.danger { background: #e53935; color: white; border: none; }
  `]
})
export class GoalsComponent implements OnInit {
  goals: any[] = [];
  editId: number | null = null;
  form: any = {};

  constructor(private goalService: GoalService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.goalService.getAll().subscribe(data => this.goals = data);
  }

  edit(goal: any) {
    this.editId = goal.id;
    this.form = { ...goal };
  }

  cancelEdit() {
    this.editId = null;
    this.form = {};
  }

  save() {
    if (this.editId) {
      this.goalService.update(this.editId, this.form).subscribe(() => {
        this.load();
        this.cancelEdit();
      });
    } else {
      this.goalService.create(this.form).subscribe(() => {
        this.load();
        this.form = {};
      });
    }
  }

  delete(id: number) {
    if (confirm('Delete this goal?')) {
      this.goalService.delete(id).subscribe(() => this.load());
    }
  }
}
