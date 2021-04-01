import {Component} from '@angular/core';
import {AuthService} from "./core/services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  isLoggedIn: boolean;

  constructor(private authService: AuthService) {
    this.isLoggedIn = authService.isLoggedIn();
  }
}
