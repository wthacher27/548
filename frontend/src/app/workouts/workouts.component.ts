import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WorkoutService } from '../services/workout.service';

@Component({
  selector: 'app-workouts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Workouts</h2>

      <form (ngSubmit)="save()">
        <h3>{{ editId ? 'Edit Workout' : 'New Workout' }}</h3>
        <div class="form-row">
          <label>User ID</label>
          <input type="number" [(ngModel)]="form.user" name="user" required />
        </div>
        <div class="form-row">
          <label>Type</label>
          <select [(ngModel)]="form.type" name="type" required>
            <option value="">-- Select --</option>
            <option>Strength</option>
            <option>Cardio</option>
            <option>HIIT</option>
            <option>Yoga</option>
            <option>Other</option>
          </select>
        </div>
        <div class="form-row">
          <label>Date</label>
          <input type="date" [(ngModel)]="form.date" name="date" required />
        </div>
        <div class="form-row">
          <label>Duration (min)</label>
          <input type="number" [(ngModel)]="form.duration" name="duration" />
        </div>
        <div class="form-row">
          <label>Summary</label>
          <input [(ngModel)]="form.summary" name="summary" />
        </div>
        <div class="form-actions">
          <button type="submit">{{ editId ? 'Update' : 'Create' }}</button>
          <button type="button" *ngIf="editId" (click)="cancelEdit()">Cancel</button>
        </div>
      </form>

      <table>
        <thead>
          <tr>
            <th>ID</th><th>User ID</th><th>Type</th><th>Date</th><th>Duration</th><th>Summary</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let w of workouts">
            <td>{{ w.id }}</td>
            <td>{{ w.user }}</td>
            <td>{{ w.type }}</td>
            <td>{{ w.date }}</td>
            <td>{{ w.duration }} min</td>
            <td>{{ w.summary }}</td>
            <td>
              <button (click)="edit(w)">Edit</button>
              <button class="danger" (click)="delete(w.id)">Delete</button>
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
    .form-row input, .form-row select { flex: 1; padding: 4px 8px; }
    .form-actions { margin-top: 12px; display: flex; gap: 8px; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background: #2196F3; color: white; }
    tr:nth-child(even) { background: #f9f9f9; }
    button { padding: 4px 10px; cursor: pointer; }
    button.danger { background: #e53935; color: white; border: none; }
  `]
})
export class WorkoutsComponent implements OnInit {
  workouts: any[] = [];
  editId: number | null = null;
  form: any = {};

  constructor(private workoutService: WorkoutService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.workoutService.getAll().subscribe(data => this.workouts = data);
  }

  edit(workout: any) {
    this.editId = workout.id;
    this.form = { ...workout };
  }

  cancelEdit() {
    this.editId = null;
    this.form = {};
  }

  save() {
    if (this.editId) {
      this.workoutService.update(this.editId, this.form).subscribe(() => {
        this.load();
        this.cancelEdit();
      });
    } else {
      this.workoutService.create(this.form).subscribe(() => {
        this.load();
        this.form = {};
      });
    }
  }

  delete(id: number) {
    if (confirm('Delete this workout?')) {
      this.workoutService.delete(id).subscribe(() => this.load());
    }
  }
}
