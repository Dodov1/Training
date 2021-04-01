import {Component, Inject, Input, LOCALE_ID, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TrainingAdd, TrainingDto, TrainingEnums} from "../../models/TrainingModels";
import {TrainingService} from "../../../core/services/training.service";
import {DayAdd} from "../../models/DayModels";
import {WorkoutAdd} from "../../models/WorkoutModels";
import {ExerciseAddDto} from "../../models/ExerciseModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {TrainerService} from "../../../core/services/trainer.service";
import {Observable} from "rxjs";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-training-add',
  templateUrl: './training-add.component.html',
  styleUrls: ['./training-add.component.css']
})
export class TrainingAddComponent implements OnInit {

  @Input() isReadyTraining: boolean;
  training: TrainingAdd;
  month: number;
  year: number;

  minDate: string;
  trainingFinishMonth: number;
  trainingStartMonth: number;
  trainingStartYear: number;
  trainingFinishYear: number;

  days: DayAdd[];

  currentDay: DayAdd;

  isDurationInvalid: boolean;

  currentExercise: ExerciseAddDto;
  currentWorkout: WorkoutAdd;
  selectedWorkout: WorkoutAdd;
  id: number;
  daysToAdd: DayAdd[];
  currentUserId = GlobalConstants.currentUserId;
  currentTrainerId = GlobalConstants.currentTrainerId;
  step: number;

  enums: TrainingEnums;
  selectedWorkoutOption: string;
  private lastUrl: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    @Inject(LOCALE_ID) private locale: string,
    private trainingService: TrainingService,
    private trainerService: TrainerService) {
    this.training = new TrainingAdd();
    this.currentExercise = new ExerciseAddDto();
    this.currentWorkout = new WorkoutAdd();
    if (Number(this.route.snapshot.paramMap.get('id')) === 0) {
      this.id = this.currentUserId;
    } else {
      this.id = Number(this.route.snapshot.paramMap.get('id'));
    }
    this.minDate = formatDate(new Date(), 'yyyy-MM-dd', this.locale);
    this.step = 1;
    let strings = location.href.split("/");
    let newUrl = "/";
    for (let i = 3; i < strings.length - 1; i++) {
      newUrl += strings[i];
      if (i != strings.length - 2) {
        newUrl += "/";
      }
    }
    this.lastUrl = newUrl;
  }

  ngOnInit(): void {
    this.trainingService.getEnums(this.id)
      .toPromise()
      .then(el => {
        this.enums = el;
      })
      .catch(err => {

      })

    this.training.fromDate = this.minDate;
    this.daysToAdd = [];
  }

  //2021-03-29
  setBasicInfo(startDate: string, endDate: string) {
    let startTokens = startDate.split('-');
    let endTokens = endDate.split('-');
    this.trainingStartMonth = Number(startTokens[1]);
    this.trainingStartYear = Number(startTokens[0]);
    this.trainingFinishMonth = Number(endTokens[1]);
    this.trainingFinishYear = Number(endTokens[0]);
  }

  //2021-03-29assa
  setCalendarDateView(date: string) {
    let tokens = date.split('-');
    this.month = Number(tokens[1]);
    this.year = Number(tokens[0]);
  }

  nextAction() {
    let currentMonth = this.month;
    if (currentMonth + 1 > 12) {
      this.month = 1;
      this.year++;
    } else {
      this.month++;
    }
    this.initDays()
    this.calculateDaysForCurrentData();
  }

  prevAction() {
    let currentMonth = this.month;
    if (currentMonth - 1 < 1) {
      this.month = 12;
      this.year--;
    } else {
      this.month--;
    }
    this.initDays()
    this.calculateDaysForCurrentData();
  }

  calculateDaysForCurrentData() {
    let days = new Date(this.year, this.month, 0).getDate();
    let start = new Date(this.year, this.month - 1, 0).getDay();
    let index = 1;
    this.days = new Array(days + start);
    this.days.forEach(e => e.active = false);
    for (let i = 0; i < days + start; i++) {
      let dayHtml = new DayAdd();
      // @ts-ignore
      dayHtml.workouts = [];
      dayHtml.active = false;
      dayHtml.date = "" + this.year + "-" + (this.month < 10 ? "0" + this.month : this.month) + "-" + (index < 10 ? "0" + index : index);
      if (i >= start) {
        dayHtml.dayOfMonth = String(index++);

        if (this.training.fromDate !== undefined && this.training.toDate !== undefined &&
          dayHtml.date.localeCompare(this.training.fromDate) >= 0 && dayHtml.date.localeCompare(this.training.toDate) <= 0) {
          dayHtml.active = true;

          for (let j = 0; j < this.daysToAdd.length; j++) {
            if (this.daysToAdd[j].date == dayHtml.date) {
              dayHtml.workouts = this.daysToAdd[j].workouts;
            }
          }

        }
      }
      this.days[i] = dayHtml;
    }
  }

  showAdd(day: DayAdd) {
    for (let i = 0; i < this.daysToAdd.length; i++) {
      if (this.daysToAdd[i].date == day.date) {
        this.currentDay = this.daysToAdd[i];
      }
    }
  }

  addExercise() {
    this.selectedWorkout.exercises.push(this.currentExercise);
    this.currentExercise = new ExerciseAddDto();
  }

  addTraining() {
    // @ts-ignore
    let keys = Object.keys(this.daysToAdd);
    let length = keys.length;
    let arr = [];
    for (let i = 0; i < length; i++) {
      arr.push(this.daysToAdd[keys[i]]);
    }
    // @ts-ignore
    this.training.days = arr;
    if (Number(this.route.snapshot.paramMap.get('id')) === 0 && !this.isReadyTraining) {
      let observable = this.trainingService.addTraining(this.training, this.id);
      this.redirect(observable);
    } else if (!this.isReadyTraining) {
      let trainingDtoObservable = this.trainerService.addTrainingToUser(this.training, this.currentTrainerId, this.id);
      this.redirect(trainingDtoObservable);
    } else {
      this.trainerService.addReadyTraining(this.training, this.currentTrainerId)
        .subscribe(data => {
          location.reload();
        });
    }
  }

  redirect(obs: Observable<TrainingDto>) {
    obs.subscribe(data => {
      this.router.navigateByUrl(this.lastUrl + "/" + data.id);
    });
  }


  addWorkout() {
    if (this.currentWorkout.type == "Custom") {
      this.currentWorkout.exercises = [];
    }
    this.currentDay.workouts.push(this.currentWorkout);
    this.currentWorkout = new WorkoutAdd();
  }

  next() {
    this.step++;
    if (this.step == 2) {
      this.setBasicInfo(this.training.fromDate, this.training.toDate);
      this.setCalendarDateView(this.training.fromDate)
      this.calculateDaysForCurrentData();
    }
  }

  prev() {
    this.step--;
    if (this.step == 2) {
      this.setBasicInfo(this.training.fromDate, this.training.toDate);
      this.setCalendarDateView(this.training.fromDate)
      this.calculateDaysForCurrentData();
    }
  }

  setCurrentWorkout(workout: WorkoutAdd) {
    this.selectedWorkout = workout;
  }

  resetWorkout() {
    this.selectedWorkout = undefined;
  }

  showExerciseInfo($event: MouseEvent) {
    // @ts-ignore
    if ($event.target.nextSibling.nextSibling.nextSibling.nextSibling.style.display === "none") {
      // @ts-ignore
      $event.target.nextSibling.nextSibling.nextSibling.nextSibling.style.display = "block";
    } else {
      // @ts-ignore
      $event.target.nextSibling.nextSibling.nextSibling.nextSibling.style.display = "none";
    }
  }

  setWorkoutOption(option: string) {
    this.selectedWorkoutOption = option;
  }

  deleteByWorkout(workout: WorkoutAdd) {
    const index = this.currentDay.workouts.indexOf(workout, 0);
    if (index >= -1) {
      this.currentDay.workouts.splice(index, 1);
    }

  }

  editWorkout(workout: WorkoutAdd) {
    this.currentWorkout = workout;
  }

  finishEditWorkout() {
    this.currentWorkout = new WorkoutAdd();
  }

  editExercise(exercise: ExerciseAddDto) {
    this.currentExercise = exercise;
  }

  deleteExercise(exercise: ExerciseAddDto, workout: WorkoutAdd) {
    const index = this.currentDay.workouts.indexOf(workout, 0);
    if (index >= -1) {
      const indexEx = this.currentDay.workouts[index].exercises.indexOf(exercise, 0);
      if (indexEx >= -1) {
        this.currentDay.workouts[index].exercises.splice(indexEx, 1);
      }
    }
  }

  finishEditExercise() {
    this.currentExercise = new ExerciseAddDto();
  }

  showLinkInfo($event: MouseEvent) {
    // @ts-ignore
    if ($event.target.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.style.display === "none") {
      // @ts-ignore
      $event.target.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.style.display = "block";
    } else {
      // @ts-ignore
      $event.target.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling.style.display = "none";
    }
  }

  initDays() {
    let fromDateParsed = new Date(this.training.fromDate);
    if (this.training.toDate == null) {
      return;
    }
    while (true) {
      let currentDate = formatDate(fromDateParsed, 'yyyy-MM-dd', this.locale);

      let hasDay = false;
      for (let j = 0; j < this.daysToAdd.length; j++) {
        if (this.daysToAdd[j].date == currentDate) {
          hasDay = true;
          break;
        }
      }

      let dayHtml = new DayAdd();
      // @ts-ignore
      dayHtml.workouts = [];
      dayHtml.date = currentDate;

      if (!hasDay) {
        this.daysToAdd.push(dayHtml);
      }

      fromDateParsed.setDate(fromDateParsed.getDate() + 1);
      if (currentDate.localeCompare(this.training.toDate) == 0) {
        break;
      }
    }
    this.calculateDaysForCurrentData();
  }

  checkForWrongDurationInput() {
    if (isNaN(this.currentExercise.duration)) {
      this.isDurationInvalid = true;
      return;
    } else {
      let number = Number(this.currentExercise.duration);
      if (number <= 0) {
        this.isDurationInvalid = true;
        return;
      }
      this.isDurationInvalid = false;
    }
  }
}
