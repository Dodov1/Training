import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../core/services/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']).then(e => location.reload())
    }
    localStorage.removeItem("user")
    localStorage.removeItem("hasBeenRefreshed")
    localStorage.removeItem("hasUsername")
    localStorage.removeItem("hasEmail")
  }

}
