import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './core/app-routing.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {TrainingsComponent} from './shared/components/trainings/trainings.component';
import {TrainingComponent} from "./shared/components/training/training.component";
import {TrainingInfoComponent} from './shared/components/training-info/training-info.component';
import {UsersComponent} from './shared/components/users/users.component';
import {TrainingAddComponent} from './shared/components/training-add/training-add.component';
import {MyDayComponent} from './shared/components/my-day/my-day.component';
import {RouterModule} from '@angular/router';
import {HeaderComponent} from './shared/components/header/header.component';
import {LoginComponent} from './homeModule/login/login.component';
import {HomeComponent} from './homeModule/home/home.component';
import {TrainersComponent} from './shared/components/trainers/trainers.component';
import {RegisterComponent} from './homeModule/register/register.component';
import {AuthorizationGuard} from "./core/guards/AuthorizationGuard";
import {PageNotFoundComponent} from './shared/components/page-not-found/page-not-found.component';
import {AdminComponent} from './shared/components/admin/admin.component';
import {ProfileInfoComponent} from './shared/components/profile-info/profile-info.component';
import {NoAccessComponent} from './shared/components/no-access/no-access.component';
import {UserProfileComponent} from './shared/components/user-profile/user-profile.component';
import {TrainerProfileComponent} from './shared/components/trainer-profile/trainer-profile.component';
import {ProfileViewComponent} from './shared/components/profile-view/profile-view.component';
import {SideNavComponent} from './core/sideNav/side-nav/side-nav.component';
import {NavigationComponent} from './core/header/navigation/navigation.component';
import {Youtube} from "./core/services/Youtube";
import {GlobalHttpInterceptorService} from "./core/services/global-http-interceptor.service";
import {MyTrainingsComponent} from './shared/components/my-trainings/my-trainings.component';
import {TrainingEditComponent} from './shared/components/training-edit/training-edit.component';
import {TrainingCreatorComponent} from './shared/components/training-creator/training-creator.component';
import {MyStatisticsComponent} from './shared/components/my-statistics/my-statistics.component';
import { LogsComponent } from './shared/components/logs/logs.component';

@NgModule({
  declarations: [
    AppComponent,
    TrainingsComponent,
    TrainingComponent,
    TrainingInfoComponent,
    UsersComponent,
    TrainingAddComponent,
    MyDayComponent,
    HeaderComponent,
    LoginComponent,
    HomeComponent,
    TrainersComponent,
    RegisterComponent,
    PageNotFoundComponent,
    AdminComponent,
    ProfileInfoComponent,
    NoAccessComponent,
    UserProfileComponent,
    TrainerProfileComponent,
    ProfileViewComponent,
    SideNavComponent,
    NavigationComponent,
    Youtube,
    MyTrainingsComponent,
    TrainingEditComponent,
    TrainingCreatorComponent,
    MyStatisticsComponent,
    LogsComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [AuthorizationGuard, NavigationComponent,
    {provide: HTTP_INTERCEPTORS, useClass: GlobalHttpInterceptorService, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
