import {Component, OnInit} from '@angular/core';
import {TrainerUserView} from "../../models/TrainerModels";
import {SuggestionModel, UserBasicPicDto} from "../../models/UserModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {UserService} from "../../../core/services/user.service";
import {RequestAddDto, RequestViewModel} from "../../models/RequesterModels";
import {TrainerService} from "../../../core/services/trainer.service";
import {TrainersListModel} from "../../models/LinkModels";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-trainers',
  templateUrl: './trainers.component.html',
  styleUrls: ['./trainers.component.css']
})
export class TrainersComponent implements OnInit {

  trainers: TrainerUserView[];
  sortingCriteria: string;
  currentUserId = GlobalConstants.currentUserId;
  currentTrainerId = GlobalConstants.currentTrainerId;
  clickedButton = 'Trainers';
  links: TrainersListModel;
  page: number;

  sortBy: string;
  orderBy: string;
  lastPage: number;

  trainerRequests: RequestViewModel[];
  searchText: string;
  suggestionTrainers: UserBasicPicDto[];
  jumpPageInput: number;

  constructor(private userService: UserService, private trainerService: TrainerService,
              private _route: ActivatedRoute,
              private _router: Router) {
    this.jumpPageInput = 1;
    this.sortingCriteria = "";
    this.searchText = "";
    this._route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.page = +params['page'] || 0;
        this.sortBy = params['sortBy'] || 'id';
        this.orderBy = params['orderBy'] || 'asc';
      })
  }

  ngOnInit(): void {
    this.userService.getAllTrainersByUserId(this.currentUserId, this.page, this.sortBy, this.orderBy)
      .toPromise().then(data => {
      this.trainers = data.trainers;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });

    this.userService.getAllTrainerRequestsByUserId(this.currentUserId)
      .toPromise().then(data => {
      this.trainerRequests = data;
    }).catch(err => {
    });
  }

  setClickButton(value: string) {
    // @ts-ignore
    this.clickedButton = value;
  }

  respond(complete: string, currentTrainer: RequestViewModel) {
    let request = new RequestAddDto();
    request.receiverId = currentTrainer.id;
    request.statusType = complete;
    this.userService.respondToRequest(this.currentUserId, request)
      .subscribe(data => {
        this.ngOnInit();
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

  sendRequest(href: string) {
    this.userService.getAllTrainersByHref(href)
      .toPromise()
      .then(data => {
        this._router.navigate(['/trainers'], {
          queryParams: {
            page: this.page != 0 ? this.page : null
          },
          queryParamsHandling: 'merge',
        });
        this.trainers = data.trainers;
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
      this.suggestionTrainers
        .filter(el => !el.username.toLowerCase().startsWith(this.searchText.toLowerCase()))
        .forEach(el => suggestionModel.notIds.push(el.id));
      if (!isNaN(this.currentTrainerId)) {
        suggestionModel.notIds.push(this.currentTrainerId);
      }
      this.trainerService.getSuggestions(suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionTrainers = el;
        })
    }
  }

  showSuggestions() {
    if (this.searchText == '') {
      let suggestionModel = new SuggestionModel();
      suggestionModel.input = this.searchText;
      suggestionModel.notIds = new Array<number>();
      if (!isNaN(this.currentTrainerId)) {
        suggestionModel.notIds.push(this.currentTrainerId);
      }
      this.trainerService.getSuggestions(suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionTrainers = el;
        })
    }
  }

  sortByMethod(sortBy: string, orderBy: string, href: string) {
    this.orderBy = orderBy;
    this.sortBy = sortBy;
    this.page = 0;
    this._router.navigate(['/trainers'], {
      queryParams: {
        page: null,
        sortBy: this.sortBy,
        orderBy: this.orderBy
      },
      queryParamsHandling: 'merge',
    });
    this.initTrainers(href);
  }

  private initTrainers(href: string) {
    this.userService.getByHref(href)
      .toPromise().then(data => {
      this.trainers = data.trainers;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });
  }

  jumpPageAction() {
    this.page = this.jumpPageInput > this.lastPage ? this.lastPage + 1 :
      this.jumpPageInput < 0 ? 0 :
        this.jumpPageInput;
    this.jumpPageInput = this.page;
    this.page--;
    this.userService.getAllTrainersByUserId(this.currentUserId, this.page, this.sortBy, this.orderBy)
      .toPromise().then(data => {
      this._router.navigate(['/trainers'], {
        queryParams: {
          page: this.page != 0 ? this.page : null
        },
        queryParamsHandling: 'merge',
      });
      this.trainers = data.trainers;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });
  }
}
