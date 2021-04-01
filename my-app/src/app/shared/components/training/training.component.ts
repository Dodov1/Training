import {Component, Input, OnInit} from '@angular/core';
import {TrainingDto} from '../../models/TrainingModels';

@Component({
  selector: 'app-training',
  templateUrl: './training.component.html',
  styleUrls: ['./training.component.css']
})

export class TrainingComponent implements OnInit {

  @Input() training: TrainingDto;

  constructor() {
  }

  ngOnInit(): void {
  }
}
