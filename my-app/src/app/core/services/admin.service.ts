import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {RespondToTrainerRequestDto, TrainerDto} from "../../shared/models/TrainerModels";
import {LogModel} from "../../shared/models/UserModels";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/admins/';
  }

  getNotApprovedTrainers() {
    return this.http.get<TrainerDto[]>(this.baseUrl + "trainerRequests", this.headers);
  }

  respondToRequestToBecomeTrainer(id: number, request: RespondToTrainerRequestDto) {
    return this.http.patch(this.baseUrl + id + '/respondToRequest', request, this.headers);
  }

  getLog() {
    return this.http.get<LogModel[]>(this.baseUrl + "log", this.headers);
  }
}
