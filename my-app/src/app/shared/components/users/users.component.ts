import {Component, OnInit} from '@angular/core';
import {SuggestionModel, UserBasicPicDto, UsersViewInfo} from "../../models/UserModels";
import {TrainerService} from "../../../core/services/trainer.service";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {RequestAddDto, RequestViewModel} from "../../models/RequesterModels";
import {UserService} from "../../../core/services/user.service";
import {UsersListModel} from "../../models/LinkModels";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: UsersViewInfo[];
  links: UsersListModel;
  currentTrainerId = GlobalConstants.currentTrainerId;
  currentUserId = GlobalConstants.currentUserId;

  page: number;
  sortBy: string;
  orderBy: string;
  lastPage: number;

  usersRequests: RequestViewModel[];
  searchText: string;
  suggestionUsers: UserBasicPicDto[];
  jumpPageInput: number;

  constructor(private trainerService: TrainerService, private userService: UserService,
              private _route: ActivatedRoute,
              private _router: Router) {
    if (isNaN(this.currentTrainerId)) {
      this._router.navigate(['/noAccess']).then(e => location.reload())
    }
    this.jumpPageInput = 1;
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
    this.trainerService.getAllUsersByTrainerId(this.currentTrainerId, this.page, this.sortBy, this.orderBy)
      .toPromise().then(data => {
      this.users = data.users;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });

    this.trainerService.getAllUsersRequestsByTrainerId(this.currentTrainerId)
      .toPromise().then(data => {
      this.usersRequests = data;
    }).catch(err => {
    });
  }

  respond(status: string, currentUser: RequestViewModel) {
    let request = new RequestAddDto();
    request.receiverId = currentUser.id;
    request.statusType = status;
    this.trainerService.respondToRequest(this.currentTrainerId, request)
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
    this.trainerService.getAllUsersByTrainerHref(href)
      .toPromise().then(data => {
      this._router.navigate(['/users'], {
        queryParams: {
          page: this.page != 0 ? this.page : null
        },
        queryParamsHandling: 'merge',
      });
      this.users = data.users;
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
      this.suggestionUsers
        .filter(el => !el.username.toLowerCase().startsWith(this.searchText.toLowerCase()))
        .forEach(el => suggestionModel.notIds.push(el.id));
      suggestionModel.notIds.push(this.currentUserId);
      this.userService.getSuggestions(suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionUsers = el;
        })
    }
  }

  showSuggestions() {
    if (this.searchText == '') {
      let suggestionModel = new SuggestionModel();
      suggestionModel.input = this.searchText;
      suggestionModel.notIds = [];
      suggestionModel.notIds.push(this.currentUserId);
      this.userService.getSuggestions(suggestionModel)
        .toPromise()
        .then(el => {
          this.suggestionUsers = el;
        })
    }
  }

  sortByMethod(sortBy: string, orderBy: string, href: string) {
    this.orderBy = orderBy;
    this.sortBy = sortBy;
    this.page = 0;
    this._router.navigate(['/users'], {
      queryParams: {
        page: null,
        sortBy: this.sortBy,
        orderBy: this.orderBy
      },
      queryParamsHandling: 'merge',
    });
    this.initUsers(href);
  }

  private initUsers(href: string) {
    this.trainerService.getAllUsersByTrainerHref(href)
      .toPromise().then(data => {
      this.users = data.users;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });
  }

  jumpPageAction() {
    this.page = this.jumpPageInput > this.lastPage ? this.lastPage+1 :
      this.jumpPageInput  < 0 ? 0 :
        this.jumpPageInput;
    this.jumpPageInput = this.page;
    this.page--;
    this.trainerService.getAllUsersByTrainerId(this.currentTrainerId, this.page, this.sortBy, this.orderBy)
      .toPromise().then(data => {
      this._router.navigate(['/users'], {
        queryParams: {
          page: this.page != 0 ? this.page : null
        },
        queryParamsHandling: 'merge',
      });
      this.users = data.users;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });
  }
}
