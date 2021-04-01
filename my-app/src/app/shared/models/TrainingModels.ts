import {DayAdd, DayIdModel} from "./DayModels";
import {TrainingsListModel} from "./LinkModels";

export class ReadyTrainingStartDateModel {
  startDate: string;
}

export class TrainingBasicDto {
  id: number;
  title: string;
  trainingType: string;
  difficulty: string;
}

export class TrainingDto {
  id: number;
  fromDate: string;
  toDate: string;
  title: string;
  description: string;
  trainingType: string;
  statusType: string;
  difficulty: string;
}

export class Trainings {
  "trainings": [TrainingDto]
  "totalPages": number
  "_links": TrainingsListModel
}

export class TrainingAdd {
  title: string;
  description: string;
  fromDate: string;
  toDate: string;
  trainingType: string;
  difficultyType: string;
  days: [DayAdd];
}

export class TrainingFullInfo {
  training: TrainingDto;
  days: DayAdd[]
}


export class TrainingWithLinksInfo {
  training: TrainingDto;
  days: [DayIdModel]
}

export interface Trainings {
  trainings: [TrainingDto];
}

export interface TrainingEnums {
  "trainingTypes": string[],
  "exerciseTypes": string[],
  "exerciseTargets": string[],
  "workoutTypes": string[],
  "durationTypes": string[],
  "difficultyTypes": string[]
}
