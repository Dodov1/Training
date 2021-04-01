import {Component, OnInit} from '@angular/core';
import {TrainingDto, Trainings} from "../../models/TrainingModels";
import {TrainingsListModel} from "../../models/LinkModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {TrainingService} from "../../../core/services/training.service";
import {UserService} from "../../../core/services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {SuggestionModel} from "../../models/UserModels";

@Component({
  selector: 'app-my-trainings',
  templateUrl: './my-trainings.component.html',
  styleUrls: ['./my-trainings.component.css']
})
export class MyTrainingsComponent implements OnInit {

  trainings: TrainingDto[];
  isChecked: boolean;
  page: number;
  sortBy: string;
  orderBy: string;
  lastPage: number;
  links: TrainingsListModel;
  currentUserId = GlobalConstants.currentUserId;
  jumpPageInput: number;
  searchText: string;
  suggestionTrainings: TrainingDto[];

  constructor(private trainingsService: TrainingService,
              private userService: UserService,
              private _route: ActivatedRoute,
              private _router: Router) {
    this.jumpPageInput = 1;
    this.searchText = "";
    this._route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.page = +params['page'] || 0;
        this.sortBy = params['sortBy'] || 'fromDate';
        this.orderBy = params['orderBy'] || 'asc';
      })
  }

  ngOnInit(): void {
    this.trainingsService.getAllTrainingsByUserId(this.currentUserId, this.page, 6, this.sortBy, this.orderBy)
      .toPromise().then(trainings => {
      this.trainings = trainings.trainings
      this.links = trainings._links;
      this.lastPage = trainings.totalPages
    }).catch(err => {
    });
  }

  showAll(sortBy: string, orderBy: string, href: string): void {
    this.page = 0;
    this.sortBy = sortBy;
    this.orderBy = orderBy;
    this._router.navigate(['/myTrainings'], {
      queryParams: {
        page: null,
        sortBy: this.sortBy,
        orderBy: this.orderBy
      },
      queryParamsHandling: 'merge',
    });
    this.trainingsService.getAllTrainingsByHref(href)
      .toPromise().then(trainings => {
      this.trainings = trainings.trainings
      this.links = trainings._links;
      this.lastPage = trainings.totalPages
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
    this.trainingsService.getAllTrainingsByHref(href)
      .toPromise().then(trainings => {
      this._router.navigate(['/myTrainings'], {
        queryParams: {
          page: this.page != 0 ? this.page : null
        },
        queryParamsHandling: 'merge',
      });
      this.trainings = trainings.trainings
      this.lastPage = trainings.totalPages
      this.links = trainings._links;
    }).catch(err => {
    });

  }


  jumpPageAction() {
    this.page = this.jumpPageInput > this.lastPage ? this.lastPage+1 :
      this.jumpPageInput  < 0 ? 0 :
        this.jumpPageInput;
    this.jumpPageInput = this.page;
    this.page--;
    this.trainingsService.getAllTrainingsByUserId(this.currentUserId, this.page, 6, this.sortBy, this.orderBy)
      .toPromise().then(trainings => {
      this._router.navigate(['/myTrainings'], {
        queryParams: {
          page: this.page != 0 ? this.page : null
        },
        queryParamsHandling: 'merge',
      });
      this.lastPage = trainings.totalPages
      this.trainings = trainings.trainings
      this.links = trainings._links;
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
      this.trainingsService.getAllTrainingSuggestions(this.currentUserId, suggestionModel)
        .toPromise()
        .then(el => {
          console.log(el)
          this.suggestionTrainings = el;
        })
    }
  }

  showSuggestions() {
    if (this.searchText == '') {
      let suggestionModel = new SuggestionModel();
      suggestionModel.input = this.searchText;
      suggestionModel.notIds = [];
      this.trainingsService.getAllTrainingSuggestions(this.currentUserId, suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionTrainings = el;
        })
    }
  }
}
