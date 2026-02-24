import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ExerciseService {
  private baseUrl = 'http://localhost:8080/api/exercises';

  constructor(private http: HttpClient) {}

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  getByWorkout(workoutId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/workout/${workoutId}`);
  }

  create(exercise: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, exercise);
  }

  update(id: number, exercise: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, exercise);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
