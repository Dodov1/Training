import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {ActivatedRouteSnapshot, Router} from "@angular/router";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class GlobalHttpInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService, public router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    return next.handle(req).pipe(
      catchError((error) => {

        if (error.status === 403 && !this.router.url.startsWith('/login')) {
          this.router.navigateByUrl("/noAccess", {skipLocationChange: true})
        }
        if (error.status === 400) {
          this.router.navigateByUrl("/ads", {skipLocationChange: true})
        }
        if (error.status === 401 && !this.router.url.startsWith('/login')) {
          this.authService.logout();
          alert('You session expired. You are redirected to login Page');
          this.router.navigate(
            ["/login"],
            {
              queryParams: {
                prevUrl: this.router.url
              },
              queryParamsHandling: 'merge',
              skipLocationChange: false
            }).then(e => {
            location.reload()
          })
        }
        error.stat
        return throwError(error.message);
      })
    )
  }
}
