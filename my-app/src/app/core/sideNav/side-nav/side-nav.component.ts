import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})
export class SideNavComponent implements OnInit {

  isTrainer: boolean;
  isAdmin: boolean;

  constructor() {
  }

  ngOnInit(): void {
    let arr = JSON.parse(localStorage.getItem("userRoles"));
    if (arr != null) {
      arr.forEach(el => {
        if (el["role"] === "ROLE_TRAINER") {
          this.isTrainer = true;
        }
        if (el["role"] === "ROLE_ADMIN") {
          this.isAdmin = true;
        }
      });
    }
  }

}
