import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../core/services/user.service";
import {WeightService} from "../../../core/services/weight.service";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {Statistic, WeightStatistic} from "../../models/Statistic";

@Component({
  selector: 'app-my-statistics',
  templateUrl: './my-statistics.component.html',
  styleUrls: ['./my-statistics.component.css']
})
export class MyStatisticsComponent implements OnInit {

  currentUserId = GlobalConstants.currentUserId;

  weightStat: WeightStatistic;
  trainingStat: Statistic;

  constructor(private userService: UserService, private weightService: WeightService) {
  }

  ngOnInit(): void {
    this.weightService.getStatisticsForUserId(this.currentUserId)
      .toPromise().then(data => {
      console.log(data)
      this.weightStat = data

      // @ts-ignore
      let ctx = document.getElementById('weightsStat').getContext('2d');


      // @ts-ignore
      let chart = new Chart(ctx, {

        // The type of chart we want to create
        type: 'line',

        data: {
          labels: this.weightStat.labels,
          datasets: [{
            label: 'kilograms',
            backgroundColor: '#ffa600',
            borderColor: '#003f5c',
            data: this.weightStat.data
          }]
        },

        // Configuration options go here
        options: {
          title: {
            display: true,
            text: 'Monthly Diagram for kilograms'
          },
          scales: {
            yAxes: [{
              ticks: {
                min: this.weightStat.minWeight,
                max: this.weightStat.maxWeight
              }
            }]
          }
        }
      });
    });

    this.userService.getTrainingsStatisticsForUserId(this.currentUserId)
      .toPromise()
      .then(data => {
        this.trainingStat = data;
        // @ts-ignore
        let ctx2 = document.getElementById('trainingsStat').getContext('2d');
        // @ts-ignore
        let chart2 = new Chart(ctx2, {

            // The type of chart we want to create
            type: 'doughnut',

            data: {
              labels: this.trainingStat.labels,
              datasets: [{
                label: 'kilograms',
                backgroundColor: ['rgb(50,205,50)', '#FFD700', '#C71585'],
                borderColor: '#003f5c',
                data: this.trainingStat.data
              }
              ]
            },

            // Configuration options go here
            options: {
              title: {
                display: true,
                text: 'User Training Diagram'
              }
            }
          })
        ;

      });
  }

}
