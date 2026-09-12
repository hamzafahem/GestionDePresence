import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertController, ToastController, ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { FormateurMeService } from 'src/app/services/formateur-me.service';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { QrService } from 'src/app/services/qr.service';

@Component({
  selector: 'app-mes-formations-f',
  templateUrl: './mes-formations-f.page.html',
  styleUrls: ['./mes-formations-f.page.scss'],
})
export class MesFormationsFPage implements OnInit, ViewWillEnter, ViewWillLeave {

  loading = true;
  myUserId: number | null = null;

  formations: any[] = [];
  modulesByFormation: { [formationId: number]: any[] } = {};

  demandes: any[] = [];

  activeTab: 'affectations' | 'demandes' | 'disponibles' = 'affectations';
  searchOpen = false;
  searchText = '';

  // Expand/collapse state for "Mes affectations" cards.
  expandedFormations: { [formationId: number]: boolean } = {};
  expandedModules: { [moduleId: number]: boolean } = {};
  // "Voir tout" toggle per formation (caps the module preview list until clicked).
  showAllModules: { [formationId: number]: boolean } = {};

  // Présence cache, keyed by module id: { presences, hasPresence, lastDate }
  presenceByModule: { [moduleId: number]: { loaded: boolean; hasPresence: boolean; lastDate: any } } = {};

  private refreshTimer: any;

  constructor(
    private router: Router,
    private formateurMe: FormateurMeService,
    private lss: LocalStorageService,
    private alertCtrl: AlertController,
    private toastCtrl: ToastController,
    private qr: QrService
  ) { }

  ngOnInit() {
    this.myUserId = this.lss.getUserId();
    this.loadAll();
  }

  ionViewWillEnter() {
    this.loadAll();
    this.loadDemandes();
    this.refreshTimer = setInterval(() => {
      this.loadAll();
      this.loadDemandes();
    }, 20000);
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  private loadAll() {
    this.loading = true;
    this.formateurMe.getFormations().subscribe({
      next: (res: any) => {
        this.formations = res.items || [];
        this.loading = false;
        this.formations.forEach(f => this.loadModules(f.id));
      },
      error: () => this.loading = false
    });
    this.loadDemandes();
  }

  private loadModules(formationId: any) {
    this.formateurMe.getModules(formationId).subscribe({
      next: (modules) => {
        this.modulesByFormation[formationId] = modules || [];
        this.loadPresencesForAssignedModules();
      },
      error: () => this.modulesByFormation[formationId] = []
    });
  }

  private loadDemandes() {
    this.formateurMe.getMesDemandes().subscribe({
      next: (rows: any[]) => this.demandes = rows || [],
      error: () => { }
    });
  }

  /** Fetches (once per module) the full présence history for every module the formateur is
   *  assigned to, whether nested under a formation-entière or assigned individually. */
  private loadPresencesForAssignedModules() {
    const modules: any[] = [];
    this.mesFormationsAssignees.forEach(f => {
      (this.modulesByFormation[f.id] || []).forEach(m => modules.push(m));
    });
    this.mesModulesAssignes.forEach(m => modules.push(m));

    modules.forEach(m => {
      if (this.presenceByModule[m.id]) { return; } // already loaded or loading
      this.presenceByModule[m.id] = { loaded: false, hasPresence: false, lastDate: null };
      this.qr.getPresences(m.formation_id, m.id).subscribe({
        next: (rows: any[]) => {
          const list = rows || [];
          let lastDate: any = null;
          list.forEach(p => {
            if (p.date_participation && (!lastDate || new Date(p.date_participation) > new Date(lastDate))) {
              lastDate = p.date_participation;
            }
          });
          this.presenceByModule[m.id] = { loaded: true, hasPresence: list.length > 0, lastDate };
        },
        error: () => this.presenceByModule[m.id] = { loaded: true, hasPresence: false, lastDate: null }
      });
    });
  }

  presenceInfo(moduleId: any) {
    return this.presenceByModule[moduleId] || { loaded: false, hasPresence: false, lastDate: null };
  }

  get mesFormationsAssignees() {
    return this.formations.filter(f => f.formateur_id === this.myUserId);
  }

  get mesModulesAssignes() {
    const all: any[] = [];
    Object.keys(this.modulesByFormation).forEach(fid => {
      (this.modulesByFormation[+fid] || []).forEach(m => {
        if (m.formateur_id === this.myUserId) all.push(m);
      });
    });
    return all;
  }

  get affectationsCount() {
    return this.mesFormationsAssignees.length + this.mesModulesAssignes.length;
  }

  private matchesSearch(name: string) {
    const q = (this.searchText || '').trim().toLowerCase();
    if (!q) { return true; }
    return (name || '').toLowerCase().includes(q);
  }

  get filteredFormationsAssignees() {
    return this.mesFormationsAssignees.filter(f => this.matchesSearch(f.intitule));
  }

  get filteredModulesAssignes() {
    return this.mesModulesAssignes.filter(m => this.matchesSearch(m.nom));
  }

  get filteredDemandesEnAttente() {
    return this.demandesEnAttente.filter(d => this.matchesSearch(d.formation_intitule) || this.matchesSearch(d.module_nom));
  }

  get filteredDemandesTraitees() {
    return this.demandesTraitees.filter(d => this.matchesSearch(d.formation_intitule) || this.matchesSearch(d.module_nom));
  }

  get filteredFormations() {
    return this.formations.filter(f => this.matchesSearch(f.intitule));
  }

  toggleSearch() {
    this.searchOpen = !this.searchOpen;
    if (!this.searchOpen) { this.searchText = ''; }
  }

  get demandesEnAttente() {
    return this.demandes.filter(d => d.statut === 'EN_ATTENTE');
  }

  get demandesTraitees() {
    return this.demandes.filter(d => d.statut !== 'EN_ATTENTE');
  }

  formationById(id: any) {
    return this.formations.find(f => f.id === id);
  }

  demandeEnCoursPourFormation(formationId: any) {
    return this.demandes.find(d => d.statut === 'EN_ATTENTE' && d.formation_id === formationId && d.module_id == null);
  }

  demandeEnCoursPourModule(moduleId: any) {
    return this.demandes.find(d => d.statut === 'EN_ATTENTE' && d.module_id === moduleId);
  }

  demanderFormation(formation: any) {
    this.formateurMe.demanderFormation(formation.id).subscribe({
      next: () => {
        this.loadDemandes();
        this.toast('Demande envoyée pour toute la formation.', 'success');
      },
      error: (err) => this.toast(err.error?.message || 'Échec de la demande.', 'danger')
    });
  }

  demanderModule(module: any) {
    this.formateurMe.demanderModule(module.id).subscribe({
      next: () => {
        this.loadDemandes();
        this.toast('Demande envoyée pour ce module.', 'success');
      },
      error: (err) => this.toast(err.error?.message || 'Échec de la demande.', 'danger')
    });
  }

  async annuler(demande: any) {
    const alert = await this.alertCtrl.create({
      header: 'Annuler la demande ?',
      message: `Annuler votre demande pour "${demande.formation_intitule}"${demande.module_nom ? ' — ' + demande.module_nom : ''} ?`,
      buttons: [
        { text: 'Non', role: 'cancel' },
        {
          text: 'Oui, annuler',
          role: 'destructive',
          handler: () => {
            this.formateurMe.annulerDemande(demande.id).subscribe({
              next: () => { this.loadDemandes(); this.toast('Demande annulée.', 'medium'); },
              error: () => this.toast("Échec de l'annulation.", 'danger')
            });
          }
        }
      ]
    });
    await alert.present();
  }

  private async toast(message: string, color: string) {
    const toast = await this.toastCtrl.create({ message, duration: 2500, color });
    toast.present();
  }

  setTab(tab: 'affectations' | 'demandes' | 'disponibles') {
    this.activeTab = tab;
  }

  toggleFormation(formationId: number) {
    this.expandedFormations[formationId] = !this.expandedFormations[formationId];
  }

  toggleModule(moduleId: number) {
    this.expandedModules[moduleId] = !this.expandedModules[moduleId];
  }

  toggleVoirTout(formationId: number, ev?: Event) {
    if (ev) { ev.stopPropagation(); }
    this.showAllModules[formationId] = !this.showAllModules[formationId];
  }

  visibleModules(formationId: number) {
    const modules = this.modulesByFormation[formationId] || [];
    if (this.showAllModules[formationId] || modules.length <= 3) { return modules; }
    return modules.slice(0, 3);
  }

  moduleCount(formationId: number) {
    return (this.modulesByFormation[formationId] || []).length;
  }

  progressPct(formationId: number) {
    const modules = this.modulesByFormation[formationId] || [];
    if (!modules.length) { return 0; }
    const withPresence = modules.filter(m => this.presenceInfo(m.id).hasPresence).length;
    return Math.round((withPresence / modules.length) * 100);
  }

  async centreAide() {
    await this.toast('Fonctionnalité à venir.', 'medium');
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
