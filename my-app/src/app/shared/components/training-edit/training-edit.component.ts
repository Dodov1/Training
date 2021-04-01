import {Component, Inject, Input, LOCALE_ID, OnInit} from '@angular/core';
import {TrainingDto, TrainingEnums, TrainingFullInfo} from "../../models/TrainingModels";
import {DayAdd} from "../../models/DayModels";
import {ExerciseAddDto} from "../../models/ExerciseModels";
import {WorkoutAdd} from "../../models/WorkoutModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {ActivatedRoute, Router} from "@angular/router";
import {TrainingService} from "../../../core/services/training.service";
import {TrainerService} from "../../../core/services/trainer.service";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-training-edit',
  templateUrl: './training-edit.component.html',
  styleUrls: ['./training-edit.component.css']
})

export class TrainingEditComponent implements OnInit {

  @Input() training: TrainingFullInfo;
  month: number;
  year: number;


  minDate: string;
  trainingFinishMonth: number;
  trainingStartMonth: number;
  trainingStartYear: number;
  trainingFinishYear: number;


  days: DayAdd[];

  @Input() isReadyTraining: boolean;
  currentDay: DayAdd;

  currentExercise: ExerciseAddDto;
  currentWorkout: WorkoutAdd;
  selectedWorkout: WorkoutAdd;
  trainingId = +this.route.snapshot.paramMap.get('trainingId');
  userId = +this.route.snapshot.paramMap.get('userId');
  daysToAdd: DayAdd[];
  currentUserId = GlobalConstants.currentUserId;
  step: number;

  enums: TrainingEnums;

  selectedWorkoutOption: string;
  lastUrl: string;
  currentTrainerId = GlobalConstants.currentTrainerId;
  cantEdit: boolean;
  isDurationInvalid: boolean;

  constructor(private route: ActivatedRoute, private router: Router,
              @Inject(LOCALE_ID) private locale: string,
              private trainingService: TrainingService, private trainerService: TrainerService) {
    this.currentExercise = new ExerciseAddDto();
    this.currentWorkout = new WorkoutAdd();
    if (Number(this.route.snapshot.paramMap.get('userId')) === 0) {
      this.userId = this.currentUserId;
    } else {
      this.userId = Number(this.route.snapshot.paramMap.get('userId'));
    }
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

    this.daysToAdd = [];

    this.trainingService.getEnums(this.userId)
      .toPromise()
      .then(el => {
        this.enums = el;
      })
      .catch(err => {

      })
    if (!this.isReadyTraining) {
      if (Number(this.route.snapshot.paramMap.get('userId')) === 0) {
        this.trainingService.getFullTrainingById(this.trainingId, this.userId)
          .toPromise()
          .then(el => {
            this.training = el
            this.minDate = el.training.fromDate;
            if (this.training.training.statusType === 'Completed') {
              this.cantEdit = true;
            }
          })
          .catch(err => {
            this.router.navigateByUrl('noAccess', {skipLocationChange: true})
          });
      } else {
        this.trainerService.getFullTrainingToEdit(this.currentTrainerId, this.userId, this.trainingId)
          .toPromise()
          .then(el => {
            this.training = el
            this.minDate = el.training.fromDate;
            if (this.training.training.statusType === 'Completed') {
              this.cantEdit = true;
            }
          })
          .catch(err => {
            this.router.navigateByUrl('noAccess', {skipLocationChange: true})
          });
      }
    }
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

  //2021-03-29
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
    this.calculateDaysForCurrentData();
    this.initDays();
  }

  prevAction() {
    let currentMonth = this.month;
    if (currentMonth - 1 < 1) {
      this.month = 12;
      this.year--;
    } else {
      this.month--;
    }
    this.calculateDaysForCurrentData();
    this.initDays();
  }

  calculateDaysForCurrentData() {
    let days = new Date(this.year, this.month, 0).getDate();
    let start = new Date(this.year, this.month - 1, 0).getDay();
    let index = 1;
    let sizeArr = days + start;
    let currentDate = formatDate(new Date(), 'yyyy-MM-dd', this.locale);
    this.days = new Array(sizeArr);
    this.days.forEach(e => e.active = false);
    for (let i = 0; i < days + start; i++) {
      let dayHtml = new DayAdd();
      // @ts-ignore
      dayHtml.workouts = [];
      dayHtml.active = false;
      dayHtml.date = "" + this.year + "-" + (this.month < 10 ? "0" + this.month : this.month) + "-" + (index < 10 ? "0" + index : index);
      if (i >= start) {
        dayHtml.dayOfMonth = String(index++);
        if (dayHtml.date.localeCompare(this.training.training.fromDate) >= 0 && dayHtml.date.localeCompare(this.training.training.toDate) <= 0
          && dayHtml.date.localeCompare(currentDate) >= 0) {
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

  editTraining() {
    this.training.days = this.daysToAdd;
    if (Number(this.route.snapshot.paramMap.get('userId')) === 0 && !this.isReadyTraining) {
      let objectObservable = this.trainingService.editTraining(this.training, this.userId);
      this.redirect(objectObservable);
    } else if (!this.isReadyTraining) {
      let trainingDtoObservable1 = this.trainerService.editTraining(this.training, this.currentTrainerId, this.userId, this.trainingId);
      this.redirect(trainingDtoObservable1);
    } else {
      this.trainerService.editReadyTraining(this.training, this.currentTrainerId, this.training.training.id)
        .subscribe(data => {
          location.reload();
        });
    }
  }

  redirect(obs) {
    obs.subscribe(data => {
      this.router.navigateByUrl(this.lastUrl);
    });
  }

  addWorkout() {
    if (this.currentWorkout.type == "Custom") {
      this.currentWorkout.exercises = [];
    }
    this.currentDay.workouts.push(this.currentWorkout);
    this.currentWorkout = new WorkoutAdd();
    this.calculateDaysForCurrentData();
  }

  next() {
    this.step++;
    if (this.step == 2) {
      this.setBasicInfo(this.training.training.fromDate, this.training.training.toDate);
      this.setCalendarDateView(this.training.training.fromDate)
      this.calculateDaysForCurrentData();
      this.initDays();
    }
  }

  prev() {
    this.step--;
    if (this.step == 2) {
      this.setBasicInfo(this.training.training.fromDate, this.training.training.toDate);
      this.setCalendarDateView(this.training.training.fromDate)
      this.calculateDaysForCurrentData();
      this.initDays();
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
    let fromDateParsed = new Date(this.training.training.fromDate);
    while (true) {
      let currentDate = formatDate(fromDateParsed, 'yyyy-MM-dd', this.locale);

      let hasDay = false;
      for (let j = 0; j < this.daysToAdd.length; j++) {
        if (this.daysToAdd[j].date == currentDate) {
          hasDay = true;
          break;
        }
      }
      for (let j = 0; j < this.training.days.length; j++) {
        if (this.training.days[j].date == currentDate && !hasDay) {
          this.daysToAdd.push(this.training.days[j]);
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
      if (currentDate.localeCompare(this.training.training.toDate) == 0) {
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
