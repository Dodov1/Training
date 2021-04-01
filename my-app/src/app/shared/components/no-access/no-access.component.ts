import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-no-access',
  templateUrl: './no-access.component.html',
  styleUrls: ['./no-access.component.css']
})
export class NoAccessComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
    if (!('Notification' in window)) {
      console.log('Web Notification not supported');
      return;
    }

    Notification.requestPermission(function (permission) {
      var notification = new Notification("No Access", {
        body: 'Don`t have access',
        icon: 'https://thumbs.dreamstime.com/b/vector-yellow-hazard-warning-symbol-danger-icon-sign-warn-isolated-white-background-use-web-typography-app-road-155959729.jpg',
        dir: 'ltr'
      });
      setTimeout(function () {
        notification.close();
      }, 3000);
    });
  }

}
