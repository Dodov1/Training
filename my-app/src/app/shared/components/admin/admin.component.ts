import {Component, OnInit} from '@angular/core';
import {RespondToTrainerRequestDto, TrainerDto} from "../../models/TrainerModels";
import {AdminService} from "../../../core/services/admin.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  notApprovedTrainers: TrainerDto[];

  constructor(private adminService: AdminService) {
  }

  ngOnInit(): void {
    this.adminService.getNotApprovedTrainers()
      .toPromise()
      .then(el => {
        this.notApprovedTrainers = el;
      });
  }

  respond(accepted: string, currentTrainer: TrainerDto) {
    let request = new RespondToTrainerRequestDto();
    request.relationStatus = accepted;
    this.adminService.respondToRequestToBecomeTrainer(currentTrainer.id, request)
      .subscribe(el => {
        this.ngOnInit();
      })
  }
}
