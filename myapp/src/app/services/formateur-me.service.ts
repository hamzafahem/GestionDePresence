import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { LocalStorageService } from './local-storage.service';

/**
 * Auto-service du Formateur connecté sur l'app mobile : parcourir les
 * formations/modules disponibles et demander à y être affecté (en
 * complément de l'affectation directe faite par l'admin depuis le
 * back-office, qui reste possible en parallèle).
 */
@Injectable({
  providedIn: 'root'
})
export class FormateurMeService {

  constructor(private http: HttpClient, private lss: LocalStorageService) { }

  private getHeaders(): HttpHeaders {
    const token = this.lss.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  /** Toutes les formations, avec formateur_id/formateur_nom_fr déjà renseignés si affectées. */
  getFormations() {
    return this.http.get<any>(`${environment.api_url}/administration/formations`, { headers: this.getHeaders() });
  }

  /** Modules d'une formation, avec formateur_id/formateur_nom_fr déjà renseignés si affectés. */
  getModules(formationId: any) {
    return this.http.get<any[]>(
      `${environment.api_url}/administration/formations/${formationId}/modules`, { headers: this.getHeaders() }
    );
  }

  getMesDemandes() {
    return this.http.get<any[]>(`${environment.api_url}/formateurs/me/demandes`, { headers: this.getHeaders() });
  }

  demanderFormation(formationId: any) {
    return this.http.post<any>(
      `${environment.api_url}/formateurs/me/demandes/formation`,
      null,
      { headers: this.getHeaders(), params: new HttpParams().set('formationId', formationId) }
    );
  }

  demanderModule(moduleId: any) {
    return this.http.post<any>(
      `${environment.api_url}/formateurs/me/demandes/module`,
      null,
      { headers: this.getHeaders(), params: new HttpParams().set('moduleId', moduleId) }
    );
  }

  annulerDemande(id: any) {
    return this.http.delete<void>(`${environment.api_url}/formateurs/me/demandes/${id}`, { headers: this.getHeaders() });
  }
}
