import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DayFullModel} from "../../shared/models/DayModels";

@Injectable({
  providedIn: 'root'
})
export class DayService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};


  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/users/';
  }

  getInfoForDay(workoutsUrl: string):Observable<DayFullModel> {
    return this.http.get<DayFullModel>(workoutsUrl, this.headers);
  }
}
