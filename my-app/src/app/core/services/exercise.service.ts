import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {ExerciseDto} from "../../shared/models/ExerciseModels";
import {ChangeStatusDto} from "../../shared/models/WorkoutModels";

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {

  baseUrl: string;
  headers = {"headers": new HttpHeaders().set("Authorization", localStorage.getItem("access_token"))};


  constructor(private http: HttpClient) {
    this.baseUrl = 'http://localhost:8080/users/';
  }

  setExerciseTypeToDone(exerciseId: number, workoutId: number, userId: number, statusDto: ChangeStatusDto): Observable<ExerciseDto> {
    console.log("/users/{userId}/workouts/{workoutId}/exercises/{exerciseId}")
    return this.http.patch<ExerciseDto>('http://localhost:8080/users/' + userId + '/workouts/' + workoutId + '/exercises/' + exerciseId, statusDto, this.headers);
  }
}
