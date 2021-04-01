import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<any> {
    let dto = {username: username, password: password};
    let save = new HttpHeaders();
    save = save.set("Authorization", `Basic ${btoa(username + ":" + password)}`);
    return this.http.post<any>('http://localhost:8080/login', dto, {"headers": save, observe: 'response'});
  }

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('userId');
    localStorage.removeItem('trainerId');
    localStorage.removeItem('userRoles');
  }

  public isLoggedIn(): boolean {
    let token = localStorage.getItem('access_token');
    if (token === null) {
      return false;
    }
    const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
    let isExpired = (Math.floor((new Date).getTime() / 1000)) >= expiry;
    return !isExpired;
  }
}
