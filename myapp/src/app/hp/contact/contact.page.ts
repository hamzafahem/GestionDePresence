import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController, AlertController, ActionSheetController, ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { QrService, MODULE_QR_PREFIX } from 'src/app/services/qr.service';
import * as QRCode from 'qrcode';

const AVATAR_COLORS = ['#8b5cf6', '#3f7fe0', '#14a06d', '#e8763b', '#d1373f', '#0891b2'];

@Component({
  selector: 'app-contact',
  templateUrl: './contact.page.html',
  styleUrls: ['./contact.page.scss'],
})
export class ContactPage implements OnInit, AfterViewChecked, ViewWillEnter, ViewWillLeave {

  @ViewChild('moduleQrCanvas') moduleQrCanvasRef?: ElementRef<HTMLCanvasElement>;
  showQrModal = false;
  private qrCodeNeedsRender = false;

  formations: any[] = [];
  selectedFormationId: any;

  modules: any[] = [];
  selectedModuleId: any;
  roster: any[] = [];
  searchTerm = '';
  loading = false;
  busyDoctorantId: any = null;
  editMode = false;
  todayLabel = new Date().toLocaleDateString('fr-FR', { weekday: 'long', day: 'numeric', month: 'long' });

  private refreshTimer: any;

  constructor(
    private router: Router,
    private qr: QrService,
    private toastCtrl: ToastController,
    private alertCtrl: AlertController,
    private actionSheetCtrl: ActionSheetController
  ) { }

  ngOnInit() {
    this.qr.getFts().subscribe({
      next: (res: any) => {
        this.formations = res.items || [];
        if (this.formations.length) {
          this.selectedFormationId = this.formations[0].id;
          this.loadModules();
        }
      },
      error: () => this.showToast('Erreur de chargement des formations.', 'danger')
    });
  }

  ionViewWillEnter() {
    if (this.selectedModuleId) {
      this.loadRoster();
    }
    this.refreshTimer = setInterval(() => {
      if (!this.editMode) {
        this.loadRoster();
      }
    }, 20000);
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  onFormationChange() {
    this.modules = [];
    this.selectedModuleId = null;
    this.roster = [];
    this.loadModules();
  }

  private loadModules() {
    if (!this.selectedFormationId) {
      return;
    }
    this.qr.getModules(this.selectedFormationId).subscribe({
      next: (modules: any[]) => {
        this.modules = modules;
        if (modules.length) {
          this.selectedModuleId = modules[0].id;
          this.loadRoster();
        }
      },
      error: () => this.showToast('Erreur de chargement des modules.', 'danger')
    });
  }

  onModuleChange() {
    this.loadRoster();
  }

  loadRoster() {
    if (!this.selectedModuleId) {
      return;
    }
    this.loading = true;
    this.qr.getRoster(this.selectedFormationId, this.selectedModuleId).subscribe({
      next: (rows: any[]) => {
        this.roster = rows;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.showToast('Erreur de chargement de la présence.', 'danger');
      }
    });
  }

  get filteredRoster() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      return this.roster;
    }
    return this.roster.filter(r =>
      `${r.nom} ${r.prenom} ${r.code_apogee}`.toLowerCase().includes(term)
    );
  }

  get presentCount() {
    return this.roster.filter(r => r.present).length;
  }

  get absentCount() {
    return this.roster.filter(r => !r.present).length;
  }

  initials(row: any) {
    return `${(row.prenom || ' ')[0]}${(row.nom || ' ')[0]}`.toUpperCase();
  }

  avatarColor(row: any) {
    return AVATAR_COLORS[row.doctorant_id % AVATAR_COLORS.length];
  }

  toggleEditMode() {
    this.editMode = !this.editMode;
    if (!this.editMode) {
      this.showToast('Présence enregistrée.', 'success');
    }
  }

  togglePresence(row: any) {
    if (!this.editMode) {
      return;
    }
    this.busyDoctorantId = row.doctorant_id;
    const action = row.present
      ? this.qr.retirerPresence(this.selectedFormationId, this.selectedModuleId, row.doctorant_id)
      : this.qr.marquerPresenceManuelle(this.selectedFormationId, this.selectedModuleId, row.doctorant_id);

    action.subscribe({
      next: () => {
        this.busyDoctorantId = null;
        this.loadRoster();
      },
      error: () => {
        this.busyDoctorantId = null;
        this.showToast('Échec de la mise à jour de la présence.', 'danger');
      }
    });
  }

  async openRowMenu(row: any) {
    const buttons: any[] = [];

    if (row.reclamation_id) {
      buttons.push({
        text: 'Approuver — ' + this.reclamationLabel(row),
        icon: 'checkmark-circle-outline',
        handler: () => this.traiterReclamation(row, 'APPROUVEE')
      });
      buttons.push({
        text: 'Rejeter — ' + this.reclamationLabel(row),
        icon: 'close-circle-outline',
        role: 'destructive',
        handler: () => this.traiterReclamation(row, 'REJETEE')
      });
    }

    buttons.push({ text: 'Annuler', role: 'cancel' });

    const sheet = await this.actionSheetCtrl.create({
      header: `${row.prenom} ${row.nom}`,
      buttons
    });
    await sheet.present();
  }

  async traiterReclamation(row: any, statut: 'APPROUVEE' | 'REJETEE') {
    const isAutoCheckin = row.reclamation_type === 'AUTOCHECKIN_PRESENCE';
    const alert = await this.alertCtrl.create({
      header: statut === 'APPROUVEE' ? 'Approuver ?' : 'Rejeter ?',
      message: isAutoCheckin
        ? `${row.prenom} ${row.nom} a scanné le QR code du module en séance.`
        : `${row.prenom} ${row.nom} déclare avoir été présent(e) à ce module.`,
      inputs: [{ name: 'reponse', type: 'text', placeholder: 'Réponse (optionnel)' }],
      buttons: [
        { text: 'Annuler', role: 'cancel' },
        {
          text: 'Confirmer',
          handler: (data) => {
            this.qr.traiterDemande(row.reclamation_id, statut, data.reponse || '').subscribe({
              next: async () => {
                const toast = await this.toastCtrl.create({
                  message: statut === 'APPROUVEE' ? 'Réclamation approuvée, présence ajoutée.' : 'Réclamation rejetée.',
                  duration: 2500,
                  color: statut === 'APPROUVEE' ? 'success' : 'medium'
                });
                toast.present();
                this.loadRoster();
              },
              error: () => this.showToast('Échec du traitement de la réclamation.', 'danger')
            });
          }
        }
      ]
    });
    await alert.present();
  }

  ngAfterViewChecked() {
    if (this.qrCodeNeedsRender && this.moduleQrCanvasRef) {
      this.qrCodeNeedsRender = false;
      const content = MODULE_QR_PREFIX + this.selectedModuleId;
      QRCode.toCanvas(this.moduleQrCanvasRef.nativeElement, content, { width: 220, margin: 1 }).catch(() => { });
    }
  }

  /** Affiche en grand le QR code du module sélectionné, à projeter/montrer en séance. */
  ouvrirQrModule() {
    if (!this.selectedModuleId) {
      return;
    }
    this.showQrModal = true;
    this.qrCodeNeedsRender = true;
  }

  fermerQrModule() {
    this.showQrModal = false;
  }

  reclamationLabel(row: any) {
    return row.reclamation_type === 'AUTOCHECKIN_PRESENCE' ? 'Auto-scan en attente' : 'Réclamation en attente';
  }

  async showTodayInfo() {
    const toast = await this.toastCtrl.create({
      message: `Vous consultez la présence d'aujourd'hui — ${this.todayLabel}.`,
      duration: 2500,
      color: 'medium'
    });
    toast.present();
  }

  private async showToast(message: string, color: string) {
    const toast = await this.toastCtrl.create({ message, duration: 2500, color });
    toast.present();
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
