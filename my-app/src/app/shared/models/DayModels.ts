import {Workout, WorkoutAdd, WorkoutFullInfo} from "./WorkoutModels";
import {DayLinks} from "./LinkModels";

export interface Day {
  "calories": number,
  "status": string,
  "date": string,
  "workouts": [
    Workout
  ]
}


export class DayDto {
  id: number;
  calories: number;
  status: string;
  date: string;
}

export interface Days {
  "days": [Day]
}


export interface MyDay {
  "localDate": string
  workoutsCount: number
  workoutWithLinks: number
  workoutsExercisesCount: number
  "workouts": [Workout]
}


export class DayAdd {
  id: number
  dayOfMonth: string
  date: string
  active: boolean
  workouts: [WorkoutAdd]
}

export class DayActive {
  date: string
  active: boolean
  workoutsCount: number;
  index: number;
  _links: DayLinks;
}

export class DayIdModel {
  id: number;
  date: string
  workoutsCount: number
  _links: DayLinks
}

export class DayInfoModel {
  date: string
  link: string
  workouts: WorkoutFullInfo[];
}

export class DayFullModel {
  "id": number
  "calories": number
  "status": string
  "date": string
  "workoutsCount": number
  "workoutsLinkCount": number
  "totalExercises": number
  "workouts": [WorkoutFullInfo]
}
