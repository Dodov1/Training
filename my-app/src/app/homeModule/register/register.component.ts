import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserAdd} from "../../shared/models/UserModels";
import {Router} from "@angular/router";
import {UserService} from "../../core/services/user.service";
import {AuthService} from "../../core/services/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {

  user: UserAdd;
  hasBeenRefreshed: Boolean;
  hasEmail: boolean;
  hasUsername: boolean;
  hasUser: boolean;
  isHeightInvalid: boolean;
  isAgeInvalid: boolean;

  regexEmail = new RegExp("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

  constructor(private router: Router, private userService: UserService, private authService: AuthService) {
    this.user = localStorage.getItem("user") == null ? new UserAdd() : JSON.parse(localStorage.getItem("user"));
    this.hasBeenRefreshed = localStorage.getItem("hasBeenRefreshed") != null;
    this.hasUsername = localStorage.getItem("hasUsername") != null;
    this.hasEmail = localStorage.getItem("hasEmail") != null;
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']).then(e => location.reload())
    }
    window.onbeforeunload = () => {
      localStorage.setItem("user", JSON.stringify(this.user))
      localStorage.setItem("hasBeenRefreshed", 'true')
      if (this.hasUsername) {
        localStorage.setItem("hasUsername", '' + this.hasUsername)
      }
      if (this.hasEmail) {
        localStorage.setItem("hasEmail", '' + this.hasEmail)
      }
    }
  }

  checkIfUsernameExists() {
    if (this.user.username != '') {
      this.userService.checkIfUserExistsWithUsername(this.user.username)
        .toPromise()
        .then(e => {
          this.hasUsername = true;
        }).catch(err => {
        this.hasUsername = false;
      });
    }
  }

  checkIfEmailExists() {
    if (this.user.email != '') {
      this.userService.checkIfUserExistsWithEmail(this.user.email)
        .toPromise()
        .then(e => {
          this.hasEmail = true;
        }).catch(err => {
        this.hasEmail = false;
      });
    }
  }

  registerUser() {
    if (isNaN(this.user.height) && isNaN(this.user.age)) {
      this.isAgeInvalid = true;
      this.isHeightInvalid = true;
      return;
    }
    if (isNaN(this.user.height)) {
      this.isHeightInvalid = true;
      return;
    } else {
      let number = Number(this.user.height);
      if (number <= 0) {
        this.isHeightInvalid = true;
        return;
      }
      this.isHeightInvalid = false;
    }
    if (isNaN(this.user.age)) {
      this.isAgeInvalid = true;
      return;
    } else {
      let number = Number(this.user.age);
      if (number <= 0) {
        this.isAgeInvalid = true;
        return;
      }
      this.isAgeInvalid = false;
    }
    this.userService.registerNewUser(this.user)
      .toPromise()
      .then(el => {
        this.router.navigateByUrl("/login");
      }).catch(el => {

    })
    ;
  }

  ngOnDestroy(): void {
    localStorage.removeItem("user")
    localStorage.removeItem("hasBeenRefreshed")
    localStorage.removeItem("hasUsername")
    localStorage.removeItem("hasEmail")
  }


  checkForWrongHeightInput() {
    if (isNaN(this.user.height)) {
      this.isHeightInvalid = true;
      return;
    } else {
      let number = Number(this.user.height);
      if (number <= 0) {
        this.isHeightInvalid = true;
        return;
      }
      this.isHeightInvalid = false;
    }
  }

  checkForWrongAgeInput() {
    if (isNaN(this.user.age)) {
      this.isAgeInvalid = true;
      return;
    } else {
      let number = Number(this.user.age);
      if (number <= 0) {
        this.isAgeInvalid = true;
        return;
      }
      this.isAgeInvalid = false;
    }
  }
}
