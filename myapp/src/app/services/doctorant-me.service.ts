import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { LocalStorageService } from './local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class DoctorantMeService {

  constructor(private http: HttpClient, private lss: LocalStorageService) { }

  private getHeaders(): HttpHeaders {
    const token = this.lss.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getProfile() {
    return this.http.get<any>(`${environment.api_url}/doctorants/me`, { headers: this.getHeaders() });
  }

  getFormations() {
    return this.http.get<any>(`${environment.api_url}/doctorants/me/formations`, { headers: this.getHeaders() });
  }

  getParticipations() {
    return this.http.get<any>(`${environment.api_url}/doctorants/me/participations`, { headers: this.getHeaders() });
  }

  getInscriptions() {
    return this.http.get<any>(`${environment.api_url}/doctorants/me/inscriptions`, { headers: this.getHeaders() });
  }

  sinscrire(formationId: any) {
    return this.http.post<any>(
      `${environment.api_url}/doctorants/me/inscriptions`,
      { formation_id: formationId },
      { headers: this.getHeaders() }
    );
  }

  /** Annule une demande d'inscription encore EN_ATTENTE. */
  annulerInscription(id: any) {
    return this.http.delete<void>(`${environment.api_url}/doctorants/me/inscriptions/${id}`, { headers: this.getHeaders() });
  }

  getModules(formationId: any) {
    return this.http.get<any[]>(
      `${environment.api_url}/administration/formations/${formationId}/modules`,
      { headers: this.getHeaders() }
    );
  }

  getReclamations() {
    return this.http.get<any[]>(`${environment.api_url}/doctorants/me/reclamations`, { headers: this.getHeaders() });
  }

  signalerReclamation(moduleId: any, description: string) {
    return this.http.post<any>(
      `${environment.api_url}/doctorants/me/reclamations`,
      { module_id: moduleId, description },
      { headers: this.getHeaders() }
    );
  }

  changePassword(oldPassword: string, newPassword: string) {
    return this.http.post(
      `${environment.api_url}/doctorants/me/change-password`,
      { old_password: oldPassword, new_password: newPassword },
      { headers: this.getHeaders() }
    );
  }

  getSettings() {
    return this.http.get<any>(`${environment.api_url}/doctorants/me/settings`, { headers: this.getHeaders() });
  }

  updateSettings(settings: any) {
    return this.http.put<any>(`${environment.api_url}/doctorants/me/settings`, settings, { headers: this.getHeaders() });
  }

  /** "2e méthode" : le doctorant scanne le QR code du module affiché par le formateur en séance. */
  checkin(moduleId: any) {
    return this.http.post<any>(
      `${environment.api_url}/doctorants/me/checkin`,
      null,
      { headers: this.getHeaders(), params: new HttpParams().set('moduleId', moduleId) }
    );
  }

  getCheckins() {
    return this.http.get<any[]>(`${environment.api_url}/doctorants/me/checkins`, { headers: this.getHeaders() });
  }

  /** Demande de nouvelle carte (carte perdue, endommagée...) — section "Ma carte". */
  getDemandesCarte() {
    return this.http.get<any[]>(`${environment.api_url}/doctorants/me/demandes-carte`, { headers: this.getHeaders() });
  }

  demanderNouvelleCarte(description: string) {
    return this.http.post<any>(
      `${environment.api_url}/doctorants/me/demandes-carte`,
      null,
      { headers: this.getHeaders(), params: description ? new HttpParams().set('description', description) : undefined }
    );
  }
}
