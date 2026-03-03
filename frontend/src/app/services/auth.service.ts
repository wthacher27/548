import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/users';
  private currentUser: any = null;

  constructor(private http: HttpClient) {}

  login(name: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, { name, password }).pipe(
      tap(user => this.currentUser = user)
    );
  }

  register(user: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, user).pipe(
      tap(created => this.currentUser = created)
    );
  }

  logout() {
    this.currentUser = null;
  }

  getUser(): any {
    console.log(this.currentUser);
    return this.currentUser;
  }

  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }
}
