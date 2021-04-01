import {UsersViewInfo} from "./UserModels";
import {SelfLink, TrainersListModel} from "./LinkModels";

export class Trainer {
  "trainer": TrainerDto
  "users":
    UsersViewInfo[]

}

export class TrainerFullInfoDto {
  "id": number
  "firstName": string
  "username": string
  "lastName": string
  "type": string
  "phoneNumber": string;
  "fromDate": string;
  "rating": number;
  "totalUsers": number
}

export class TrainerUser {
  "trainers": [TrainerUserView]
  "totalPages": number
  "_links": TrainersListModel
}

export class TrainerDto {
  "id": number
  "firstName": string
  "username": string
  "lastName": string
  "type": string
}

export class RespondToTrainerRequestDto {
  "relationStatus": string
}

export class TrainerUserView {
  "id": number
  "username": string
  "usersCount": number
  "type": string
  "rating": number
  "profilePicture": string
  "_links": SelfLink
}


export class RequestToBecomeTrainer {
  trainerType: string;
  phoneNumber: string;
}

export class Rating {
  rating: number
}

export class TrainerEnums {
  trainerEnums: string[]
}
