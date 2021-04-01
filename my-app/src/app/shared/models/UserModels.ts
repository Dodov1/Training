import {UsersListModel} from "./LinkModels";

export class LogModel {
  message: string
  time: string
}

export class UserDto {
  "id": number
  "firstName": string
  "lastName": string
  "username": string
  "email": string
  "age": number
  "height": number
  "trainerStatus": string
}

export class UserProfileViewM0del {
  "id": number
  "firstName": string
  "lastName": string
  "username": string
  "email": string
  "age": number
  "height": number
  "statusType": string
}

export class UserBasicDto {

  "id": number
  "firstName": string
  "lastName": string
  "username": string
}

export class UserBasicPicDto {

  "id": number
  "firstName": string
  "lastName": string
  "username": string
  "profilePicture": string
}

export class UserAdd {

  constructor() {
  }

  "firstName": string
  "lastName": string
  "password": string
  "confirmPassword": string
  "username": string
  "email": string
  "age": number
  "height": number
}


export class UserTrainer {
  "users": [UsersViewInfo]
  "totalPages": number
  "_links": UsersListModel
}

export class UsersViewInfo {
  "id": number
  "username": string
  "trainingCount": number
  "age": number
  "height": number
  "profilePicture": string;
}

export class RequestToBecomeTrainerDto {
  "userId": number
  "trainerType": string
}

export class ImageDto {
  "location": string;
}

export class SuggestionModel {
  input: string
  notIds: number[]
}
