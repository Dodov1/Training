import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Statistic} from "../../shared/models/Statistic";
import {
  ImageDto,
  SuggestionModel,
  UserAdd,
  UserBasicPicDto,
  UserDto,
  UserProfileViewM0del
} from "../../shared/models/UserModels";
import {MyDay} from "../../shared/models/DayModels";
import {Rating, RequestToBecomeTrainer, TrainerEnums, TrainerUser} from "../../shared/models/TrainerModels";
import {RequestAddDto, RequestViewModel} from "../../shared/models/RequesterModels";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};
  headersWithResponse = {};

  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/users/';
  }

  getInfoByUserId(id: number): Observable<UserDto> {
    return this.http.get<UserDto>(this.baseUrl + id, this.headers);
  }

  getTrainingsStatisticsForUserId(id: number): Observable<Statistic> {
    return this.http.get<Statistic>(this.baseUrl + id + '/trainings/statistics', this.headers);
  }

  registerNewUser(user: UserAdd): Observable<UserAdd> {
    return this.http.post<UserAdd>(this.baseUrl + "add", user);
  }

  getTodayForUserId(id: number): Observable<MyDay> {
    return this.http.get<MyDay>(this.baseUrl + id + '/today', this.headers);
  }

  getAllTrainersByUserId(userId: number, page: number, sortBy: string, orderBy: string): Observable<TrainerUser> {
    return this.http.get<TrainerUser>(this.baseUrl + userId + "/trainers?page=" + page + "&size=6&sortBy=" + sortBy + "&orderBy=" + orderBy, this.headers);
  }

  getAllTrainerRequestsByUserId(currentUserId: number): Observable<RequestViewModel[]> {
    return this.http.get<RequestViewModel[]>(this.baseUrl + currentUserId + "/trainerRequests", this.headers);
  }

  respondToRequest(currentUserId: number, requestDto: RequestAddDto) {
    return this.http.patch(this.baseUrl + currentUserId + "/request", requestDto, this.headers);
  }

  addRequest(currentUserId: number, request: RequestAddDto) {
    return this.http.post(this.baseUrl + currentUserId + "/request", request, this.headers);
  }

  getByHref(href: string) {
    return this.http.get<TrainerUser>(href, this.headers);
  }

  updateUserPhoto(uploadImageData, currentUserId: number) {
    return this.http.post(this.baseUrl + currentUserId + "/pictures/upload", uploadImageData, {
        "headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))
      }
    )
  }

  getUserPhoto(currentUserId: number) {
    return this.http.get<ImageDto>(this.baseUrl + currentUserId + "/pictures/profileImage", this.headers)
  }

  getAllTrainersByHref(href: string) {
    return this.http.get<TrainerUser>(href, this.headers)
  }

  checkIfUserExistsWithUsername(username: string) {
    return this.http.get<TrainerUser>(this.baseUrl + 'checkUsername/' + username)
  }

  checkIfUserExistsWithEmail(email: string) {
    return this.http.get<TrainerUser>(this.baseUrl + 'checkEmail/' + email)
  }

  getSuggestions(suggestionModel: SuggestionModel): Observable<UserBasicPicDto[]> {
    return this.http.post<UserBasicPicDto[]>(this.baseUrl + "getSearchSuggestions", suggestionModel, this.headers);
  }

  sendRequestToBecomeTrainer(currentUserId: number, requestModel: RequestToBecomeTrainer) {
    return this.http.post<any>(this.baseUrl + currentUserId + "/requestToBecomeTrainer", requestModel, this.headers);
  }

  getInfoByUserIdAndTrainerId(userId: number, trainerId: number): Observable<UserProfileViewM0del> {
    return this.http.get<UserProfileViewM0del>(this.baseUrl + userId + '/trainers/' + trainerId, this.headers);
  }

  submitRating(currentUserId: number, trainerId: number, rating: Rating): Observable<Rating> {
    return this.http.post<Rating>(this.baseUrl + currentUserId + "/trainers/" + trainerId + '/rating', rating, this.headers);
  }

  getTrainerPhoto(id: number) {
    return this.http.get<ImageDto>(this.baseUrl + id + "/trainerProfileImage", this.headers)
  }

  getTrainerEnums() {
    return this.http.get<TrainerEnums>(this.baseUrl + "enums", this.headers)
  }
}
