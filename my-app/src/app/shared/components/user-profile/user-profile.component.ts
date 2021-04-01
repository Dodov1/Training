import {Component, OnInit} from '@angular/core';
import {Statistic, WeightStatistic} from "../../models/Statistic";
import {TrainingDto} from "../../models/TrainingModels";
import {SuggestionModel, UserProfileViewM0del} from "../../models/UserModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {TrainerService} from "../../../core/services/trainer.service";
import {TrainingService} from "../../../core/services/training.service";
import {UserService} from "../../../core/services/user.service";
import {WeightService} from "../../../core/services/weight.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TrainingsListModel} from "../../models/LinkModels";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  id: number;
  weightStat: WeightStatistic;
  trainingStat: Statistic;
  trainings: TrainingDto[];
  user: UserProfileViewM0del;
  currentTrainerId = GlobalConstants.currentTrainerId;
  imageLocation: string;

  page: number;
  sortBy: string;
  orderBy: string;
  lastPage: number;
  links: TrainingsListModel;
  statusType: string;
  suggestionTrainings: TrainingDto[];
  searchText: string;

  constructor(private trainerService: TrainerService,
              private trainingService: TrainingService,
              private userService: UserService,
              private weightService: WeightService,
              private route: ActivatedRoute,
              private router: Router
  ) {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.searchText = "";
    this.user = new UserProfileViewM0del();
    this.route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.page = +params['page'] || 0;
        this.sortBy = params['sortBy'] || "fromDate";
        this.orderBy = params['orderBy'] || "asc";
      })
  }

  ngOnInit(): void {
    if (this.currentTrainerId == this.id) {
      this.router.navigate(['/myProfile'], {skipLocationChange: true})
    }
    this.trainerService.getInfoByTrainerIdAndUserId(this.currentTrainerId, this.id)
      .toPromise()
      .then(data => {
        this.user = data;
        this.statusType = data.statusType;
        this.userService.getUserPhoto(this.user.id)
          .subscribe(res => {
            this.imageLocation = res.location;
          })
        this.statusType = data.statusType;
        if (data.statusType == "Accepted") {


          this.trainingService.getAllTrainingsByUserId(this.id, this.page, 6, this.sortBy, this.orderBy)
            .toPromise()
            .then(data => {
              this.trainings = data.trainings;
              this.links = data._links;
              this.lastPage = data.totalPages;
            });

          this.weightService.getStatisticsForUserId(this.id)
            .toPromise().then(data => {
            this.weightStat = data
            // @ts-ignore
            let ctx = document.getElementById('myChart').getContext('2d');


            // @ts-ignore
            let chart = new Chart(ctx, {

              // The type of chart we want to create
              type: 'line',

              data: {
                labels: this.weightStat.labels,
                datasets: [{
                  label: 'kilograms',
                  backgroundColor: '#ffa600',
                  borderColor: '#003f5c',
                  data: this.weightStat.data
                }]
              },

              // Configuration options go here
              options: {
                title: {
                  display: true,
                  text: 'Monthly Diagram for kilograms'
                },
                scales: {
                  yAxes: [{
                    ticks: {
                      min: this.weightStat.minWeight,
                      max: this.weightStat.maxWeight
                    }
                  }]
                }
              }
            });
          });

          this.userService.getTrainingsStatisticsForUserId(this.id)
            .toPromise()
            .then(data => {
              this.trainingStat = data;
              // @ts-ignore
              let ctx2 = document.getElementById('myChart1').getContext('2d');
              // @ts-ignore
              let chart2 = new Chart(ctx2, {

                  // The type of chart we want to create
                  type: 'doughnut',

                  data: {
                    labels: this.trainingStat.labels,
                    datasets: [{
                      label: 'kilograms',
                      backgroundColor: ['rgb(50,205,50)', '#FFD700', '#C71585'],
                      borderColor: '#003f5c',
                      data: this.trainingStat.data
                    }
                    ]
                  },

                  // Configuration options go here
                  options: {
                    title: {
                      display: true,
                      text: 'User Training Diagram'
                    }
                  }
                })
              ;

            });

        }

      }).catch(err => {
      this.router.navigate(['/noAccess'], {skipLocationChange: true})
    })
  }

  showAll(sortBy: string, orderBy: string, href: string): void {
    this.page = 0;
    this.sortBy = sortBy;
    this.orderBy = orderBy;
    this.router.navigate(['/users/' + this.user.id], {
      queryParams: {
        page: null,
        sortBy: this.sortBy,
        orderBy: this.orderBy
      },
      queryParamsHandling: 'merge',
    });
    this.trainingService.getAllTrainingsByHref(href)
      .toPromise().then(trainings => {
      this.trainings = trainings.trainings
      this.links = trainings._links;

    }).catch(err => {
    });
  }

  sendPrevRequest(href: string) {
    this.page--;
    this.sendRequest(href);
  }

  sendNextRequest(href: string) {
    this.page++;
    this.sendRequest(href);
  }

  sendLastRequest(href: string) {
    this.page = this.lastPage;
    this.sendRequest(href);
  }

  sendFirstRequest(href: string) {
    this.page = 1;
    this.sendRequest(href);
  }

  sendRequest(href: string) {
    this.trainingService.getAllTrainingsByHref(href)
      .toPromise().then(data => {
      this.router.navigate(['/users/' + this.user.id], {
        queryParams: {
          page: this.page != 0 ? this.page : null
        },
        queryParamsHandling: 'merge',
      });
      this.trainings = data.trainings;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {

    });
  }

  applyFilter() {
    if (this.searchText != '') {

      let suggestionModel = new SuggestionModel();
      suggestionModel.input = this.searchText;
      suggestionModel.notIds = [];
      this.suggestionTrainings
        .filter(el => !el.title.toLowerCase().startsWith(this.searchText.toLowerCase()))
        .forEach(el => suggestionModel.notIds.push(el.id));
      this.trainingService.getAllTrainingSuggestions(this.id, suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionTrainings = el;
        })
    }
  }

  showSuggestions() {
    if (this.searchText == '') {
      let suggestionModel = new SuggestionModel();
      suggestionModel.input = this.searchText;
      suggestionModel.notIds = [];
      this.trainingService.getAllTrainingSuggestions(this.id, suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionTrainings = el;
        })
    }
  }
}
