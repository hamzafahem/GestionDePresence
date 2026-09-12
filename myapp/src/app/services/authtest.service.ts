import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient} from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class AuthtestService {

  constructor(
    private http : HttpClient
  ) { }
  auth(cin: string ,password:string){
     return this.http.post<any>(
      `${environment.api_url}/auth/adminLogin`,{cin,password,}
     );
  }
}
