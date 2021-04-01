import {Component, OnInit} from '@angular/core';
import {TrainingService} from "../../../core/services/training.service";
import {ActivatedRoute, Router} from '@angular/router';
import {Statistic} from "../../models/Statistic";
import {TrainingWithLinksInfo} from "../../models/TrainingModels";
import {DayActive, DayFullModel} from "../../models/DayModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {DayService} from "../../../core/services/day.service";
import {TrainerService} from "../../../core/services/trainer.service";


@Component({
  selector: 'app-training-info',
  templateUrl: './training-info.component.html',
  styleUrls: ['./training-info.component.css']
})
export class TrainingInfoComponent implements OnInit {

  training: TrainingWithLinksInfo;
  daysToShow: DayActive[];
  trainingStat: Statistic;

  currentUserId = GlobalConstants.currentUserId;
  currentTrainerId = GlobalConstants.currentTrainerId;
  trainingId = +this.route.snapshot.paramMap.get('trainingId');
  userId = +this.route.snapshot.paramMap.get('userId');

  month: number;
  year: number;

  trainingStartMonth: number;
  trainingFinishMonth: number;
  trainingStartYear: number;
  trainingFinishYear: number;

  currentDay: DayFullModel;
  currentDayExercises: number;
  currentDatWorkoutsCount: number;

  constructor(private trainingService: TrainingService,
              private trainerService: TrainerService,
              private dayService: DayService,
              private route: ActivatedRoute,
              private router: Router) {
    this.trainingStat = new Statistic();
  }

  ngOnInit(): void {
    if (this.userId == 0) {
      this.userId = this.currentUserId;
      this.trainingService.getTrainingWithLinksById(this.trainingId, this.userId).toPromise()
        .then(training => {
          this.training = training

          this.setTrainingDurationInfo(training.training.fromDate, training.training.toDate);
          this.calculateDaysForCurrentData();
        }).catch(err => {
        this.router.navigate(['/noAccess'], {skipLocationChange: true})
      });
    } else {

      this.trainerService.getTrainingWithLinksById(this.currentTrainerId, this.userId, this.trainingId).toPromise()
        .then(training => {
          this.training = training

          this.setTrainingDurationInfo(training.training.fromDate, training.training.toDate);
          this.calculateDaysForCurrentData();
        }).catch(err => {
        this.router.navigate(['/noAccess'], {skipLocationChange: true})
      });

    }
  }

  setTrainingDurationInfo(fromDate: string, toDate: string) {
    const startingMonth = fromDate.split("-")[1];
    const startingYear = fromDate.split("-")[0];
    const endingMonth = toDate.split("-")[1];
    const endingYear = toDate.split("-")[0];
    this.month = Number(startingMonth);
    this.year = Number(startingYear);
    this.trainingStartMonth = Number(startingMonth);
    this.trainingStartYear = Number(startingYear);
    this.trainingFinishMonth = Number(endingMonth);
    this.trainingFinishYear = Number(endingYear);
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
  }

  calculateDaysForCurrentData() {
    let days = new Date(this.year, this.month, 0).getDate();
    let start = new Date(this.year, this.month - 1, 0).getDay();
    let index = 1;
    this.daysToShow = new Array(days + start);
    this.daysToShow.forEach(e => e.active = false);
    for (let i = 0; i < days + start; i++) {
      let dayHtml = new DayActive();
      dayHtml.active = false;
      if (i >= start) {
        dayHtml.date = String(index++);
        for (let j = 0; j < this.training.days.length; j++) {
          let currentDate = this.training.days[j].date;
          let date = this.year + "-" + String(this.month).padStart(2, '0') + "-" + String(dayHtml.date).padStart(2, '0');
          if (currentDate.localeCompare(date) === 0
            && date >= this.training.training.fromDate && date <= this.training.training.toDate) {
            dayHtml.active = true;
            dayHtml.index = j;
            dayHtml.workoutsCount = this.training.days[j].workoutsCount;
            dayHtml._links = this.training.days[j]._links;
            break;
          }
        }
      }
      this.daysToShow[i] = dayHtml;
    }
  }

  showInfoForDay(day: DayActive) {
    this.dayService.getInfoForDay(day._links.workouts.href)
      .toPromise()
      .then(el => {
        this.currentDay = el;
      })
  }

  editTrainingPlan() {
    this.router.navigateByUrl(this.router.url + '/edit')
  }
}
