import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";

import {AuthService} from '../services/auth.service';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

@Injectable()
export class AuthorizationGuard implements CanActivate {

  private allowedRoles: string[];

  constructor(private authService: AuthService, private router: Router) {
  }


  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
      alert('You session expired. You are redirected to login Page');


      console.log(state.url)
      this.router.navigate(
        ["/login"],
        {
          queryParams: {
            prevUrl: next.url[0].path
          },
          queryParamsHandling: 'merge',
          skipLocationChange: false
        },
      ).then(e => {
        location.reload();
      });
      return false;
    } else {
      return true;
    }
  }
}
