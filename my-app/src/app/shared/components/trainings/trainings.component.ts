import {Component, Input, OnInit} from '@angular/core';
import {TrainingService} from '../../../core/services/training.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "../../../core/services/user.service";
import {TrainingDto} from "../../models/TrainingModels";

@Component({
  selector: 'app-trainings',
  templateUrl: './trainings.component.html',
  styleUrls: ['./trainings.component.css']
})
export class TrainingsComponent implements OnInit {

  @Input() trainings: TrainingDto[];
  @Input() urlPrefix: string;


  constructor(private trainingsService: TrainingService,
              private userService: UserService,
              private _route: ActivatedRoute,
              private _router: Router) {

  }

  ngOnInit(): void {

  }


}
