import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../core/services/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string
  password: string
  wrongData: boolean
  verifyEmail: boolean;
  private prevUrl: string;


  constructor(private router: Router, private authService: AuthService,
              private _route: ActivatedRoute) {
    this._route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.prevUrl = params['prevUrl'] || null;
      })
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      if (this.prevUrl == null) {
        this.router.navigate(['/dashboard']).then(e => location.reload())
      } else {
        this.router.navigate([this.prevUrl]).then(e => location.reload())
      }
    }
  }

  authorize() {
    this.authService.login(this.username, this.password)
      .toPromise().then(result => {
      localStorage.setItem('access_token', result.headers.get("Authorization"));
      localStorage.setItem('userId', result.body["userId"]);
      localStorage.setItem('trainerId', result.body["trainerId"]);
      localStorage.setItem("userRoles", JSON.stringify(result.body["authorities"]));
      if (this.prevUrl == null) {
        this.router.navigate(['/dashboard']).then(e => location.reload())
      } else {
        this.router.navigate([this.prevUrl]).then(e => location.reload())
      }
    })
      .catch((errorResponse: any) => {
        if (errorResponse.indexOf("403") != -1) {
          this.wrongData = false;
          this.verifyEmail = true;
        } else {
          this.verifyEmail = false;
          this.wrongData = true;
        }
      });
  }
}

