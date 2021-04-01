import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {WeightAdd, WeightDto} from "../../shared/models/WeightModels";
import {Statistic, WeightStatistic} from "../../shared/models/Statistic";

@Injectable({
  providedIn: 'root'
})
export class WeightService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};
  currentUserId = localStorage.getItem("userId");

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/users/';
  }

  setNewWeightForCurrentUser(weight: WeightAdd): Observable<WeightDto> {
    return this.http.post<WeightDto>(this.baseUrl + +this.currentUserId + "/weights/add", weight, this.headers);
  }

  getStatisticsForUserId(id: number): Observable<WeightStatistic> {
    return this.http.get<WeightStatistic>(this.baseUrl + id + '/weights/statistics', this.headers);
  }

  getCurrentWeightByUserId(id: number): Observable<WeightDto>  {
    return this.http.get<WeightDto>(this.baseUrl + id + '/weights/current', this.headers);
  }
}
