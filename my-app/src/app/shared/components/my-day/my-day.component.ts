import {Component, OnInit} from '@angular/core';
import {DayService} from "../../../core/services/day.service";
import {WeightAdd, WeightDto} from "../../models/WeightModels";
import {WeightService} from "../../../core/services/weight.service";
import {UserService} from "../../../core/services/user.service";
import {GlobalConstants} from "../../../core/constants/GlobalConstants";
import {MyDay} from "../../models/DayModels";
import {ExerciseDto} from "../../models/ExerciseModels";
import {ExerciseService} from "../../../core/services/exercise.service";
import {ChangeStatusDto, Workout} from "../../models/WorkoutModels";

@Component({
    selector: 'app-my-day',
    templateUrl: './my-day.component.html',
    styleUrls: ['./my-day.component.css']
})
export class MyDayComponent implements OnInit {

    myDay: MyDay;
    header: string;
    date: string;
    weight: WeightDto;
    weightInput: number;
    showErrorDiv: boolean;
    arr = Array;
    minSize = 6;
    startNum = 0;
    maxElements = 3;
    workouts: Workout[];

    currentUserId = GlobalConstants.currentUserId;

    showWeightAddView: boolean;

    constructor(private dayService: DayService,
                private weightService: WeightService,
                private exerciseService: ExerciseService,
                private userService: UserService) {

        this.showErrorDiv = false;
        let today = new Date();
        const dd = String(today.getDate()).padStart(2, '0');
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const yyyy = today.getFullYear();
        this.header = "My Day " + String(mm + '/' + dd + '/' + yyyy);
    }

    ngOnInit(): void {
        this.userService.getTodayForUserId(this.currentUserId)
            .toPromise()
            .then(data => {
                this.myDay = data;
                this.workouts = data.workouts.slice(this.startNum, this.maxElements);
            })
            .catch(err => {
            });
        this.weightService.getCurrentWeightByUserId(this.currentUserId)
            .toPromise()
            .then(data => {
                this.weight = data;
            })
            .catch(err => {
            });
    }

    setNewWeight() {
        let weight = new WeightAdd();
        let currentDate = new Date();
        weight.date = String(currentDate.getFullYear())
            + "-" + String(currentDate.getMonth() + 1).padStart(2, '0')
            + "-" + String(currentDate.getDate()).padStart(2, '0')
            + "T" + String(currentDate.getHours()).padStart(2, '0')
            + ":" + String(currentDate.getMinutes()).padStart(2, '0')
            + ":" + String(currentDate.getSeconds()).padStart(2, '0')
            + "." + String(currentDate.getMilliseconds()).padStart(2, '0');
        weight.userId = this.currentUserId;
        // @ts-ignore
        weight.weight = this.weightInput;
        this.weightService.setNewWeightForCurrentUser(weight)
            .toPromise()
            .then(data => {
                this.weight = data;
                this.weightInput = null;
            })
            .catch(err => {
                this.showErrorDiv = true;
            });
        if (this.weight.change != '--') {
            this.toggleWeightView();
        }
    }


    markAsDone(exercise: ExerciseDto, currentWorkout: Workout) {
        console.log(exercise)
        console.log(currentWorkout)
        let statusDto = new ChangeStatusDto();
        statusDto.statusType = "Completed";
        this.exerciseService.setExerciseTypeToDone(exercise.id, currentWorkout.id, this.currentUserId, statusDto)
            .subscribe(data => {
                this.userService.getTodayForUserId(this.currentUserId)
                    .toPromise()
                    .then(data => {
                        this.myDay = data;
                        this.reloadWorkouts();
                    })
                    .catch(err => {
                    });
            })
    }

    prevEl() {
        this.startNum--;
        this.reloadWorkouts();
    }

    reloadWorkouts() {
        this.workouts = this.myDay.workouts.slice(this.startNum, this.startNum + this.maxElements);
    }

    nextEl() {
        this.startNum++;
        this.reloadWorkouts();
    }

    toggleWeightView() {
        this.showWeightAddView = !this.showWeightAddView;
    }

    setErrorFalse() {
        this.showErrorDiv = false;
    }
}
