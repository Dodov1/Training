import {Component, OnInit} from '@angular/core';
import {ReadyTrainingStartDateModel, TrainingBasicDto, TrainingFullInfo} from "../../models/TrainingModels";
import {TrainerService} from "../../../core/services/trainer.service";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {UsersViewInfo} from "../../models/UserModels";
import {UsersListModel} from "../../models/LinkModels";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-training-creator',
  templateUrl: './training-creator.component.html',
  styleUrls: ['./training-creator.component.css']
})
export class TrainingCreatorComponent implements OnInit {

  trainings: TrainingBasicDto[];
  newTrainings: TrainingBasicDto[];
  trainingToEdit: TrainingFullInfo;
  users: UsersViewInfo[];
  selectedUsers: UsersViewInfo[];
  links: UsersListModel;

  currentTrainerId = GlobalConstants.currentTrainerId;

  page: number;
  sortBy: string;
  size: number;
  orderBy: string;
  lastPage: number;

  startDateModel: ReadyTrainingStartDateModel;
  selectedTraining: TrainingBasicDto;
  selectedOption: string;
  trainingId: number;
  showErrorDiv: boolean;
  errorUsernames: string[];
  successUsernames: string[];
  showSuccessDiv: boolean;


  constructor(private trainerService: TrainerService,
              private _route: ActivatedRoute,
              private _router: Router) {
    if (isNaN(this.currentTrainerId)) {
      this._router.navigate(['/noAccess']).then(e => location.reload())
    }
    this._route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.page = +params['page'] || 0;
        this.selectedOption = params['option'] || undefined;
        this.trainingId = params['trainingId'] || undefined;
      })
    this.sortBy = 'id';
    this.orderBy = 'asc';
    this.size = 10;
    this.trainingToEdit = new TrainingFullInfo();
    this.selectedUsers = [];
    this.startDateModel = new ReadyTrainingStartDateModel();
    let today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const yyyy = today.getFullYear();
    this.startDateModel.startDate = String(yyyy + '-' + mm + '-' + dd);
  }

  ngOnInit(): void {
    this.trainerService.getAllReadyTrainings(this.currentTrainerId)
      .toPromise()
      .then(val => {
        this.trainings = val;
        if (this.selectedOption !== undefined) {
          let training = this.findTraining(this.trainings, this.trainingId);
          if (training != null) {
            if (this.selectedOption == 'assign') {
              this.setAssignOption('assign', training)
            } else if (this.selectedOption == 'edit') {
              this.setEditOption('edit', training)
            }
          } else {
            this.selectedOption = undefined;
            this._router.navigate(['/trainingCreator'], {
              queryParams: {
                page: undefined,
                option: undefined,
                trainingId: undefined
              },
              queryParamsHandling: 'merge',
            });
          }
        }
        console.log(this.trainings)
      })
      .catch(err => {

      })

  }

  findTraining(trainings: TrainingBasicDto[], idToSearch: number) {
    for (let i = 0; i < trainings.length; i++) {
      if (trainings[i].id == idToSearch) {
        return trainings[i];
      }
    }
    return null;
  }

  setEditOption(option: string, training: TrainingBasicDto) {
    this.selectedOption = option;
    this.selectedTraining = training;
    this.trainingId = training.id;
    this._router.navigate(['/trainingCreator'], {
      queryParams: {
        option: this.selectedOption != null ? this.selectedOption : null,
        trainingId: this.trainingId != null ? this.trainingId : null
      },
      queryParamsHandling: 'merge',
    });
    this.trainerService.getReadyTrainingForEdit(this.currentTrainerId, training.id)
      .toPromise()
      .then(el => {
        this.trainingToEdit = el;
      })
      .catch(err => {

      })
  }

  setAssignOption(option: string, training: TrainingBasicDto) {
    this.selectedOption = option;
    this.selectedUsers = [];
    this.selectedTraining = training;
    this.trainingId = training.id;
    this._router.navigate(['/trainingCreator'], {
      queryParams: {
        option: this.selectedOption != null ? this.selectedOption : null,
        trainingId: this.trainingId != null ? this.trainingId : null
      },
      queryParamsHandling: 'merge',
    });
    this.trainerService.getAllUsersByTrainerIdCustomSize(this.currentTrainerId, this.page, this.sortBy, this.orderBy, this.size)
      .toPromise().then(data => {
      this.users = data.users;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    })
  }

  handleSelected($event: Event, currentUser: UsersViewInfo) {
    // @ts-ignore
    if ($event.target.checked === true) {
      if (this.selectedUsers.indexOf(currentUser) === -1) {
        this.selectedUsers.push(currentUser);
      }
    } else {
      let index = -1;
      for (let i = 0; i < this.selectedUsers.length; i++) {
        if (this.selectedUsers[i].id == currentUser.id) {
          index = i;
          break;
        }
      }
      if (index >= -1) {
        this.selectedUsers.splice(index, 1);
      }
    }
  }

  addTrainingToUsers() {
    this.errorUsernames = [];
    this.successUsernames = [];
    this.selectedUsers
      .forEach(u => {
        this.trainerService.addReadyTrainingToUser(this.currentTrainerId, u.id, this.selectedTraining.id, this.startDateModel)
          .toPromise()
          .then(el => {
            this.successUsernames.push(u.username);
            this.showErrorDiv = false;
            this.showSuccessDiv = true;
          })
          .catch(err => {
            this.errorUsernames.push(u.username);
            this.showErrorDiv = true;
            this.showSuccessDiv = false;
          })
      })
  }

  setErrorFalse() {
    this.showErrorDiv = false;
  }

  setAddOption(option: string) {
    this.selectedOption = option;
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
      this._router.navigate(['/trainingCreator'], {
        queryParams: {
          page: this.page != 0 ? this.page : null,
          option: this.selectedOption != null ? this.selectedOption : null
        },
        queryParamsHandling: 'merge',
      });
      this.users = data.users;
      this.links = data._links;
      this.lastPage = data.totalPages;
    }).catch(err => {
    });
  }

  checkIfSelected(currentUser: UsersViewInfo): boolean {
    let length = this.selectedUsers.filter(e => e.id === currentUser.id).length;
    return length !== 0;
  }

  setSuccessFalse() {
    this.showSuccessDiv = false;
  }
}
