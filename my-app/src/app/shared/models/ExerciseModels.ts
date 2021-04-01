export class ExerciseDto {
  "id": number
  "status": string
  "duration": number
  "durationType": string
  "name": string
  "type": string;
  target: string;
}


export class ExerciseAddDto {
  "id": number
  "target": string
  "duration": number
  "durationType": string
  "name": string
  "type": string;
}
