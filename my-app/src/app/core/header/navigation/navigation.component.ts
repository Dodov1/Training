import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {AppComponent} from "../../../app.component";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isTrainer: boolean;
  isLoggedIn: boolean;
  isAdmin: boolean;
  currentUserId = Number(localStorage.getItem("userId"));
  imageLocation: any;

  firstTimeLoggedIn = 0;
  showOverlay = true;

  constructor(private authService: AuthService, private userService: UserService, private appComp: AppComponent) {
    this.isLoggedIn = authService.isLoggedIn();
  }

  ngOnInit(): void {
    if (this.isLoggedIn) {
      this.initImage();
    }
  }


  initImage() {
    this.userService.getUserPhoto(this.currentUserId)
      .subscribe(res => {
        this.imageLocation = res.location;
      })
  }

  updateImage(imageLocation: string) {
    this.imageLocation = imageLocation;
  }

  logout() {
    this.authService.logout();
    this.appComp.isLoggedIn = false;
    this.isLoggedIn = false;
  }
}
