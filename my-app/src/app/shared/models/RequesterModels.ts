export class RequestDto {
  isTrainerRequester: boolean
  trainerId: number
  userId: number
  statusType: string
}

export class RequestAddDto {
  statusType: string
  receiverId: number
}


export class RequestViewModel {
  id: number;
  username: string;
  profilePicture: string;

}
