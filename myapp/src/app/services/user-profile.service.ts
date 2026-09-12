import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { LocalStorageService } from './local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  constructor(private http: HttpClient, private lss: LocalStorageService) { }

  private getHeaders(): HttpHeaders {
    const token = this.lss.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getProfile() {
    return this.http.get<any>(`${environment.api_url}/users/me`, { headers: this.getHeaders() });
  }

  updateProfile(profile: any) {
    return this.http.put<any>(`${environment.api_url}/users/me`, profile, { headers: this.getHeaders() });
  }

  uploadPhoto(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(`${environment.api_url}/users/me/photo`, formData, { headers: this.getHeaders() });
  }

  deletePhoto() {
    return this.http.delete<any>(`${environment.api_url}/users/me/photo`, { headers: this.getHeaders() });
  }

  getSettings() {
    return this.http.get<any>(`${environment.api_url}/users/me/settings`, { headers: this.getHeaders() });
  }

  updateSettings(settings: any) {
    return this.http.put<any>(`${environment.api_url}/users/me/settings`, settings, { headers: this.getHeaders() });
  }

  changePassword(oldPassword: string, newPassword: string) {
    return this.http.post(
      `${environment.api_url}/users/me/change-password`,
      { old_password: oldPassword, new_password: newPassword },
      { headers: this.getHeaders() }
    );
  }
}
