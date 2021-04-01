import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TrainingInfoComponent} from "../shared/components/training-info/training-info.component";
import {UsersComponent} from "../shared/components/users/users.component";
import {TrainingAddComponent} from "../shared/components/training-add/training-add.component";
import {MyDayComponent} from "../shared/components/my-day/my-day.component";
import {LoginComponent} from "../homeModule/login/login.component";
import {HomeComponent} from "../homeModule/home/home.component";
import {TrainersComponent} from "../shared/components/trainers/trainers.component";
import {RegisterComponent} from "../homeModule/register/register.component";
import {AuthorizationGuard} from "./guards/AuthorizationGuard";
import {PageNotFoundComponent} from "../shared/components/page-not-found/page-not-found.component";
import {AdminComponent} from "../shared/components/admin/admin.component";
import {ProfileInfoComponent} from "../shared/components/profile-info/profile-info.component";
import {NoAccessComponent} from "../shared/components/no-access/no-access.component";
import {UserProfileComponent} from "../shared/components/user-profile/user-profile.component";
import {TrainerProfileComponent} from "../shared/components/trainer-profile/trainer-profile.component";
import {MyTrainingsComponent} from "../shared/components/my-trainings/my-trainings.component";
import {TrainingEditComponent} from "../shared/components/training-edit/training-edit.component";
import {TrainingCreatorComponent} from "../shared/components/training-creator/training-creator.component";
import {MyStatisticsComponent} from "../shared/components/my-statistics/my-statistics.component";
import {LogsComponent} from "../shared/components/logs/logs.component";

const routes: Routes = [
  {path: 'myTrainings', component: MyTrainingsComponent, canActivate: [AuthorizationGuard]},
  {path: 'myStatistics', component: MyStatisticsComponent, canActivate: [AuthorizationGuard]},
  {path: 'myTrainings/:trainingId/edit', component: TrainingEditComponent, canActivate: [AuthorizationGuard]},
  {path: 'myTrainings/add', component: TrainingAddComponent, canActivate: [AuthorizationGuard]},
  {path: 'myTrainings/:trainingId', component: TrainingInfoComponent, canActivate: [AuthorizationGuard]},
  {path: 'dashboard', component: MyDayComponent, canActivate: [AuthorizationGuard]},
  {path: 'noAccess', component: NoAccessComponent, canActivate: [AuthorizationGuard]},
  {path: '', component: HomeComponent},
  {path: 'trainingCreator', component: TrainingCreatorComponent, canActivate: [AuthorizationGuard]},
  {path: 'trainingCreator/training/:trainingId', component: TrainingCreatorComponent, canActivate: [AuthorizationGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'myProfile', component: ProfileInfoComponent, canActivate: [AuthorizationGuard]},
  {path: 'register', component: RegisterComponent},
  {path: 'users', component: UsersComponent, canActivate: [AuthorizationGuard]},
  {path: 'trainers', component: TrainersComponent, canActivate: [AuthorizationGuard]},
  {path: 'users/:id/trainings/add', component: TrainingAddComponent, canActivate: [AuthorizationGuard]},
  {path: 'users/:userId/trainings/:trainingId/edit', component: TrainingEditComponent, canActivate: [AuthorizationGuard]},
  {path: 'users/:id', component: UserProfileComponent, canActivate: [AuthorizationGuard]},
  {path: 'users/:userId/trainings/:trainingId', component: TrainingInfoComponent, canActivate: [AuthorizationGuard]},
  {path: 'trainers/:id', component: TrainerProfileComponent, canActivate: [AuthorizationGuard]},
  {path: 'admin', component: AdminComponent, canActivate: [AuthorizationGuard]},
  {path: 'logs', component: LogsComponent , canActivate: [AuthorizationGuard]},
  {path: '', redirectTo: '', pathMatch: 'full'},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
