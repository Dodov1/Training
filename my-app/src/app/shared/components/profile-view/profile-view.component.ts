import {Component, Input, OnInit} from '@angular/core';
import {UserProfileViewM0del} from "../../models/UserModels";
import {RequestAddDto} from "../../models/RequesterModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {TrainerService} from "../../../core/services/trainer.service";
import {UserService} from "../../../core/services/user.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-profile-view',
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.css']
})
export class ProfileViewComponent implements OnInit {

  @Input() user: UserProfileViewM0del;
  @Input() isTrainer: boolean;
  @Input() imageLocation: string;
  @Input() statusType: string;
  currentUserId = GlobalConstants.currentUserId;
  currentTrainerId = GlobalConstants.currentTrainerId;
  id: number;


  constructor(private trainerService: TrainerService,
              private userService: UserService,
              private route: ActivatedRoute) {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }

  ngOnInit(): void {
  }

  addRequest() {
    if (this.isTrainer) {
      this.addTrainerRequest();
    } else {
      this.addUserRequest();
    }
  }

  addUserRequest() {
    this.userService.addRequest(this.currentUserId, ProfileViewComponent.createRequest(this.id))
      .subscribe(el => {
        this.statusType= "NotAnswered";
      })
  }

  addTrainerRequest() {
    this.trainerService.addRequest(this.currentTrainerId, ProfileViewComponent.createRequest(this.id))
      .subscribe(el => {
        this.statusType= "NotAnswered";
      })
  }

  private static createRequest(receiverId: number): RequestAddDto {
    let request = new RequestAddDto();
    request.statusType = "NotAnswered";
    request.receiverId = receiverId;
    return request;
  }
}
