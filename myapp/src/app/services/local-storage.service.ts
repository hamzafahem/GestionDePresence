import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() { }
  public loginUser(user: any, access_token: string, annee_uni: any) {
    localStorage.setItem("AuthToken", access_token);
    localStorage.setItem("NOM_FR", user.nom_fr);
    localStorage.setItem("PRENOM_FR", user.prenom_fr);
    localStorage.setItem("USER_ID", String(user.id));
    localStorage.setItem("EMAIL", user.email);
    localStorage.setItem("PDP", user.photo);
    localStorage.setItem("ANNEE_UNI_ID", String(annee_uni.id));
    localStorage.setItem("ROLES", JSON.stringify(user.roles));
    localStorage.setItem("PERMISSIONS", JSON.stringify(user.permissions)); console.log("hi"+user.nom_fr)
    return true;}
    public getToken() {
      return localStorage.getItem("AuthToken");
  }

  public getUserId(): number | null {
    const id = localStorage.getItem("USER_ID");
    return id ? Number(id) : null;
  }

  public getFullName() {
    return `${localStorage.getItem("PRENOM_FR") || ''} ${localStorage.getItem("NOM_FR") || ''}`.trim();
  }

  public getRoles(): string[] {
    try {
      return JSON.parse(localStorage.getItem("ROLES") || '[]');
    } catch {
      return [];
    }
  }

  public isDoctorant(): boolean {
    return this.getRoles().includes('DOCTORANT');
  }

  public getPhoto(): string | null {
    const photo = localStorage.getItem("PDP");
    return photo && photo !== 'null' && photo !== 'undefined' ? photo : null;
  }

  public getRoleLabel(): string {
    const roles = this.getRoles();
    if (roles.includes('ADMIN')) return 'Administrateur';
    if (roles.includes('FORMATEUR')) return 'Formateur';
    if (roles.includes('DOCTORANT')) return 'Doctorant';
    return '';
  }

  public logout(): void {
    localStorage.removeItem("AuthToken");
    localStorage.removeItem("NOM_FR");
    localStorage.removeItem("PRENOM_FR");
    localStorage.removeItem("USER_ID");
    localStorage.removeItem("EMAIL");
    localStorage.removeItem("PDP");
    localStorage.removeItem("ANNEE_UNI_ID");
    localStorage.removeItem("ROLES");
    localStorage.removeItem("PERMISSIONS");
  }

}
