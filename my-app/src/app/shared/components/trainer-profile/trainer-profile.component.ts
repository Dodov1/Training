import {Component, OnInit} from '@angular/core';
import {UserProfileViewM0del} from "../../models/UserModels";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {TrainerService} from "../../../core/services/trainer.service";
import {UserService} from "../../../core/services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TrainingService} from "../../../core/services/training.service";
import {WeightService} from "../../../core/services/weight.service";
import {Rating, TrainerFullInfoDto} from "../../models/TrainerModels";

@Component({
  selector: 'app-trainer-profile',
  templateUrl: './trainer-profile.component.html',
  styleUrls: ['./trainer-profile.component.css']
})
export class TrainerProfileComponent implements OnInit {

  id: number;
  user: UserProfileViewM0del;
  currentUserId = GlobalConstants.currentUserId;
  imageLocation: string;
  ratingObj: Rating;

  statusType: string;
  trainerInfo: TrainerFullInfoDto;

  constructor(private trainerService: TrainerService,
              private trainingService: TrainingService,
              private userService: UserService,
              private weightService: WeightService,
              private route: ActivatedRoute,
              private router: Router) {
    this.ratingObj = new Rating();
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.user = new UserProfileViewM0del();
  }

  ngOnInit(): void {
    if (this.currentUserId == this.id) {
      this.router.navigate(['/myProfile'], {skipLocationChange: true})
    }
    this.userService.getInfoByUserIdAndTrainerId(this.currentUserId, this.id)
      .toPromise()
      .then(data => {

        this.user = data;
        this.userService.getTrainerPhoto(this.id)
          .subscribe(res => {
            this.imageLocation = res.location;
          })
        this.statusType = data.statusType;
        if (data.statusType == "Accepted") {

          this.trainerService.getByUsername(this.user.username)
            .toPromise()
            .then(el => {
              this.trainerInfo = el;
              this.ratingObj.rating = el.rating;
            })
            .catch(err => {

            });

        }

      }).catch(err => {
      this.router.navigate(['/noAccess'], {skipLocationChange: true})
    })
  }

  submitRating() {
    if (this.ratingObj.rating >= 1 && this.ratingObj.rating <= 5) {
      this.userService.submitRating(this.currentUserId, this.id, this.ratingObj)
        .toPromise()
        .then(value => {
          this.trainerInfo.rating = value.rating;
        })
        .catch(err => {

        })

    }
  }
}
