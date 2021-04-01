import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {
  ReadyTrainingStartDateModel,
  TrainingAdd,
  TrainingBasicDto,
  TrainingDto, TrainingEnums,
  TrainingFullInfo, TrainingWithLinksInfo
} from "../../shared/models/TrainingModels";
import {RespondToTrainerRequestDto, TrainerDto, TrainerFullInfoDto} from "../../shared/models/TrainerModels";
import {SuggestionModel, UserBasicPicDto, UserProfileViewM0del, UserTrainer} from "../../shared/models/UserModels";
import {RequestAddDto, RequestViewModel} from "../../shared/models/RequesterModels";

@Injectable({
  providedIn: 'root'
})
export class TrainerService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/trainers/';
  }

  getAllUsersByTrainerId(id: number, page: number, sortBy: string, orderBy: string): Observable<UserTrainer> {
    return this.http.get<UserTrainer>(this.baseUrl + id + '/users?page=' + page + '&size=6&sortBy=' + sortBy + "&orderBy=" + orderBy, this.headers);
  }

  getAllUsersByTrainerIdCustomSize(id: number, page: number, sortBy: string, orderBy: string, size: number): Observable<UserTrainer> {
    return this.http.get<UserTrainer>(this.baseUrl + id + '/users?page=' + page + '&size=' + size + '&sortBy=' + sortBy + "&orderBy=" + orderBy, this.headers);
  }

  getAllUsersRequestsByTrainerId(id: number): Observable<RequestViewModel[]> {
    return this.http.get<RequestViewModel[]>(this.baseUrl + id + '/usersRequests', this.headers);
  }

  respondToRequest(id: number, requestDto: RequestAddDto) {
    return this.http.patch(this.baseUrl + id + '/request', requestDto, this.headers);
  }

  addRequest(id: number, request: RequestAddDto) {
    return this.http.post(this.baseUrl + id + '/request', request, this.headers);
  }

  getByUsername(username: string) {
    return this.http.get<TrainerFullInfoDto>(this.baseUrl + "username/" + username, this.headers);
  }

  getSuggestions(suggestionModel: SuggestionModel): Observable<UserBasicPicDto[]> {
    return this.http.post<UserBasicPicDto[]>(this.baseUrl + "getSearchSuggestions", suggestionModel, this.headers);
  }

  getInfoByTrainerIdAndUserId(trainerId: number, userId: number) {
    return this.http.get<UserProfileViewM0del>(this.baseUrl + trainerId + '/users/' + userId, this.headers);
  }

  getAllUsersByTrainerHref(href: string) {
    return this.http.get<UserTrainer>(href, this.headers);
  }

  addTrainingToUser(training: TrainingAdd, trainerId: number, userId: number) {
    return this.http.post<TrainingDto>(this.baseUrl + trainerId + '/trainings/' + userId + '/add', training, this.headers);
  }

  editTraining(training: TrainingFullInfo, trainerId: number, userId: number, trainingId: number) {
    return this.http.put<TrainingDto>(this.baseUrl + trainerId + '/trainings/' + userId + '/edit/' + trainingId, training, this.headers);
  }

  getAllReadyTrainings(trainerId: number) {
    return this.http.get<TrainingBasicDto[]>(this.baseUrl + trainerId + '/readyTrainings', this.headers);
  }

  getReadyTrainingForEdit(trainerId: number, trainingId: number) {
    return this.http.get<TrainingFullInfo>(this.baseUrl + trainerId + '/readyTrainings/' + trainingId, this.headers);
  }

  addReadyTrainingToUser(trainerId: number, userId: number, trainingId: number, model: ReadyTrainingStartDateModel) {
    return this.http.post<TrainingDto>(this.baseUrl + trainerId + '/users/' + userId + '/readyTrainings/' + trainingId, model, this.headers);
  }

  addReadyTraining(training: TrainingAdd, currentTrainerId: number) {
    return this.http.post<TrainingDto>(this.baseUrl + currentTrainerId + '/readyTrainings', training, this.headers);
  }

  editReadyTraining(training: TrainingFullInfo, currentTrainerId: number, trainingId: number) {
    return this.http.put<TrainingDto>(this.baseUrl + currentTrainerId + '/readyTrainings/' + trainingId, training, this.headers);
  }

  getTrainingWithLinksById(currentTrainerId: number, userId: number, trainingId: number): Observable<TrainingWithLinksInfo> {
    return this.http.get<TrainingWithLinksInfo>(this.baseUrl + currentTrainerId + '/users/' + userId + '/trainings/' + trainingId + '/withDayLinks', this.headers);
  }

  getFullTrainingToEdit(currentTrainerId: number, userId: number, trainingId: number) {
    return this.http.get<TrainingFullInfo>(this.baseUrl + currentTrainerId + '/users/' + userId + '/trainings/' + trainingId + '/fullInfo', this.headers);
  }
}
