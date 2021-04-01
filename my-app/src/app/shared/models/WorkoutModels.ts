import {ExerciseAddDto, ExerciseDto} from "./ExerciseModels";

export class Workout {
  "id": number
  "name": string
  "link": string
  "type": string
  "exercises": ExerciseDto[]
}

export class WorkoutAdd {
  "id": number
  "name": string
  "exercises": ExerciseAddDto[]
  "type": string;
  "link": string;
}

export class WorkoutDto {
  "id": number
  "name": string
}

export class ChangeStatusDto {
  "statusType": string
}


export class WorkoutFullInfo {
  id: number
  name: string
  link: string;
  exercises: [ExerciseDto]
}
