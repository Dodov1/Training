import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../core/services/user.service";
import {UserDto} from "../../models/UserModels";
import {TrainerService} from "../../../core/services/trainer.service";
import {RequestToBecomeTrainer, TrainerEnums, TrainerFullInfoDto} from "../../models/TrainerModels";
import {AppComponent} from "../../../app.component";
import {NavigationComponent} from "../../../core/header/navigation/navigation.component";

@Component({
  selector: 'app-profile-info',
  templateUrl: './profile-info.component.html',
  styleUrls: ['./profile-info.component.css']
})
export class ProfileInfoComponent implements OnInit {

  userInfo: UserDto;
  trainerInfo: TrainerFullInfoDto;
  requestModel: RequestToBecomeTrainer;
  currentUserId: number;
  selectedFile: File;
  imageLocation: any;
  trainerEnums: TrainerEnums;

  regexTelephone = new RegExp("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");


  constructor(private userService: UserService,
              private trainerService: TrainerService, private appCom: NavigationComponent) {
    this.currentUserId = Number(localStorage.getItem("userId"));
    this.requestModel = new RequestToBecomeTrainer();
  }

  ngOnInit(): void {
    this.userService.getInfoByUserId(this.currentUserId)
      .toPromise()
      .then(el => {
        this.userInfo = el
        if (el.trainerStatus == "Accepted") {
          this.trainerService.getByUsername(this.userInfo.username)
            .toPromise()
            .then(elT => {
              this.trainerInfo = elT
            });
        } else {
          this.userService.getTrainerEnums()
            .toPromise()
            .then(elT => {
              this.trainerEnums = elT
            });
        }
      });
    this.setImage();
  }

  sendRequestToBecomeTrainer() {
    this.userService.sendRequestToBecomeTrainer(this.currentUserId, this.requestModel)
      .subscribe(res => {
        this.ngOnInit();
      })
  }

  setImage() {
    this.userService.getUserPhoto(this.currentUserId)
      .subscribe(res => {
        this.imageLocation = res.location;
        this.appCom.updateImage(res.location);
      })
  }

  public onFileChanged(event) {
    //Select File
    this.selectedFile = event.target.files[0];
    this.onUpload();
  }

  onUpload() {
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);

    this.userService.updateUserPhoto(uploadImageData, this.currentUserId)
      .subscribe((response) => {
          this.setImage();
        }
      );
  }
}
