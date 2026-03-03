import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ExerciseService } from '../services/exercise.service';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-exercises',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Exercises</h2>

      <form (ngSubmit)="save()">
        <h3>{{ editId ? 'Edit Exercise' : 'New Exercise' }}</h3>
        <div class="form-row">
          <label>Workout ID</label>
          <input type="number" [(ngModel)]="form.workoutId" name="workoutId" required />
        </div>
        <div class="form-row">
          <label>Name</label>
          <input [(ngModel)]="form.name" name="name" required />
        </div>
        <div class="form-row">
          <label>Reps</label>
          <input type="number" [(ngModel)]="form.reps" name="reps" />
        </div>
        <div class="form-row">
          <label>Weight (lbs)</label>
          <input type="number" step="0.5" [(ngModel)]="form.weight" name="weight" />
        </div>
        <div class="form-row">
          <label>Muscle ID</label>
          <input type="number" [(ngModel)]="form.muscleId" name="muscleId" required />
        </div>
        <div class="form-row">
          <label>Personal Record</label>
          <input type="checkbox" [(ngModel)]="form.pr" name="pr" />
        </div>
        <div class="form-actions">
          <button type="submit">{{ editId ? 'Update' : 'Create' }}</button>
          <button type="button" *ngIf="editId" (click)="cancelEdit()">Cancel</button>
        </div>
      </form>

      <table>
        <thead>
          <tr>
            <th>ID</th><th>Workout ID</th><th>Name</th><th>Reps</th><th>Weight</th><th>Muscle ID</th><th>PR</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let e of exercises">
            <td>{{ e.id }}</td>
            <td>{{ e.workoutId }}</td>
            <td>{{ e.name }}</td>
            <td>{{ e.reps }}</td>
            <td>{{ e.weight }} lbs</td>
            <td>{{ e.muscleId }}</td>
            <td>{{ e.pr ? 'PR' : '' }}</td>
            <td>
              <button (click)="edit(e)">Edit</button>
              <button class="danger" (click)="delete(e.id)">Delete</button>
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
    th { background: #9C27B0; color: white; }
    tr:nth-child(even) { background: #f9f9f9; }
    button { padding: 4px 10px; cursor: pointer; }
    button.danger { background: #e53935; color: white; border: none; }
  `]
})
export class ExercisesComponent implements OnInit {
  exercises: any[] = [];
  editId: number | null = null;
  form: any = {};
  isAdmin: boolean = false;

  constructor(private exerciseService: ExerciseService, private userService: UserService, private auth: AuthService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    const name = this.auth.getUser()?.name;
    if (!name) return;
    this.userService.isAdmin(name).subscribe((isAdmin: boolean) => {
      this.isAdmin = isAdmin;
      this.exerciseService.getAll().subscribe(data => this.exercises = data);
    });
  }

  edit(exercise: any) {
    this.editId = exercise.id;
    this.form = { ...exercise };
  }

  cancelEdit() {
    this.editId = null;
    this.form = {};
  }

  save() {
    if (this.editId) {
      this.exerciseService.update(this.editId, this.form).subscribe(() => {
        this.load();
        this.cancelEdit();
      });
    } else {
      this.exerciseService.create(this.form).subscribe(() => {
        this.load();
        this.form = {};
      });
    }
  }

  delete(id: number) {
    if (confirm('Delete this exercise?')) {
      this.exerciseService.delete(id).subscribe(() => this.load());
    }
  }
}
