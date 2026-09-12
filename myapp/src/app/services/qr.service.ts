import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { LocalStorageService } from './local-storage.service';

/** Préfixe encodé dans le QR code du module, affiché par le formateur en séance. */
export const MODULE_QR_PREFIX = 'CEDOC_MODULE:';

@Injectable({
  providedIn: 'root'
})
export class QrService {

  constructor(private http: HttpClient, private lss: LocalStorageService) { }

  private getHeaders(): HttpHeaders {
    const token = this.lss.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getFts() {
    return this.http.get<any>(
      `${environment.api_url}/administration/formations`, { headers: this.getHeaders() }
    );
  }

  getOne(id: any) {
    return this.http.get<any>(
      `${environment.api_url}/administration/formations/getOne/${id}`, { headers: this.getHeaders() }
    );
  }

  getModules(formationId: any) {
    return this.http.get<any>(
      `${environment.api_url}/administration/formations/${formationId}/modules`, { headers: this.getHeaders() }
    );
  }

  /**
   * Feuille de présence d'un module (pour hp/create-pdf côté front).
   */
  getPresences(formationId: any, moduleId: any) {
    return this.http.get<any>(
      `${environment.api_url}/formations/${formationId}/modules/${moduleId}/presences`, { headers: this.getHeaders() }
    );
  }

  /**
   * Marque la présence du doctorant identifié par "qr" (son Code Apogée,
   * scanné ou saisi) au module "idm" de la formation "idf".
   */
  setQr(idf: any, idm: any, qr: any) {
    return this.http.get<any>(
      `${environment.api_url}/formations/participerModuleByQr/${idf}/${idm}`,
      { headers: this.getHeaders(), params: { code: qr } }
    );
  }

  /**
   * "Gestion de présence" : tous les doctorants de la formation, avec leur
   * statut de présence du jour pour ce module et une éventuelle réclamation
   * en attente.
   */
  getRoster(idf: any, idm: any) {
    return this.http.get<any[]>(
      `${environment.api_url}/formations/${idf}/modules/${idm}/roster`, { headers: this.getHeaders() }
    );
  }

  marquerPresenceManuelle(idf: any, idm: any, doctorantId: any) {
    return this.http.post<void>(
      `${environment.api_url}/formations/${idf}/modules/${idm}/doctorants/${doctorantId}/presence`,
      {},
      { headers: this.getHeaders() }
    );
  }

  retirerPresence(idf: any, idm: any, doctorantId: any) {
    return this.http.delete<void>(
      `${environment.api_url}/formations/${idf}/modules/${idm}/doctorants/${doctorantId}/presence`,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Liste complète des demandes (réclamations, auto-scans, etc.) — vue lecture seule
   * pour "Gestion des réclamations" (ADMIN/FORMATEUR).
   */
  getDemandes() {
    return this.http.get<any[]>(
      `${environment.api_url}/demandes`, { headers: this.getHeaders() }
    );
  }

  /** Approuver/rejeter une réclamation de présence (Demande) depuis la vue "Gestion de présence". */
  traiterDemande(demandeId: any, statut: 'APPROUVEE' | 'REJETEE', reponse: string) {
    return this.http.post<any>(
      `${environment.api_url}/demandes/${demandeId}/traiter`,
      { statut, reponse },
      { headers: this.getHeaders() }
    );
  }
}
