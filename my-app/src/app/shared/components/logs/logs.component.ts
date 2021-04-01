import { Component, OnInit } from '@angular/core';
import {AdminService} from "../../../core/services/admin.service";
import {LogModel} from "../../models/UserModels";

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit {

  logs: LogModel[];

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.adminService.getLog()
      .toPromise()
      .then(el => {
        this.logs = el;
      });
  }

}
