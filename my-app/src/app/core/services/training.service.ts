import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {
  TrainingAdd,
  TrainingDto,
  TrainingWithLinksInfo,
  Trainings,
  TrainingFullInfo,
  TrainingEnums
} from '../../shared/models/TrainingModels';
import {SuggestionModel} from "../../shared/models/UserModels";

@Injectable({
  providedIn: 'root'
})
export class TrainingService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};
  currentUserId = localStorage.getItem("userId");

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/users/';
  }

  private getUrl(userId: number): string {
    return this.baseUrl + userId + '/trainings/';
  }

  getTrainingWithLinksById(trainingId: number, userId: number): Observable<TrainingWithLinksInfo> {
    return this.http.get<TrainingWithLinksInfo>(this.getUrl(userId) + trainingId + '/withDayLinks', this.headers);
  }

  getFullTrainingById(trainingId: number, userId: number): Observable<TrainingFullInfo> {
    return this.http.get<TrainingFullInfo>(this.getUrl(userId) + trainingId + '/fullInfo', this.headers);
  }

  addTraining(training: TrainingAdd, userId: number): Observable<any> {
    return this.http.post<TrainingAdd>(this.getUrl(userId) + 'add', training, this.headers);
  }

  getAllTrainingsByUserId(userId: number, page: number, size: number, sortBy: string, orderBy: string): Observable<Trainings> {
    return this.http.get<Trainings>('http://localhost:8080/users/' + userId + "/trainings?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&orderBy=" + orderBy, this.headers);
  }

  getAllTrainingSuggestions(userId: number, suggestionModel: SuggestionModel): Observable<TrainingDto[]> {
    return this.http.post<TrainingDto[]>('http://localhost:8080/users/' + userId + "/trainings/getSearchSuggestions", suggestionModel, this.headers);
  }

  getAllTrainingsByHref(href: string) {
    return this.http.get<Trainings>(href, this.headers);
  }

  editTraining(training: TrainingFullInfo, userId: number) {
    return this.http.put(this.getUrl(userId) + training.training.id, training, this.headers);
  }

  getEnums(id: number) {
    return this.http.get<TrainingEnums>(this.baseUrl + id + '/trainings/enums', this.headers);
  }
}
