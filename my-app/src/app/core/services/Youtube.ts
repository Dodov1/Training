import {Injectable, Pipe} from "@angular/core";
import {DomSanitizer} from "@angular/platform-browser";

@Pipe({
  name: 'youtube'
})

@Injectable()
export class Youtube {

  constructor(private dom: DomSanitizer) {
  }

  transform(value) {
    return this.dom.bypassSecurityTrustResourceUrl(value);
  }
}
