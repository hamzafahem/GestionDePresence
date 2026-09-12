import { Injectable } from '@angular/core';
import { LocalStorageService } from './local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private lss : LocalStorageService) { }

  loginUser(res: any) {
    return this.lss.loginUser(res.user, res.access_token, res.annee_uni);
  }
}
