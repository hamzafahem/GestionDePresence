import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertController, ToastController, ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';

@Component({
  selector: 'app-contactdoc',
  templateUrl: './contactdoc.page.html',
  styleUrls: ['./contactdoc.page.scss'],
})
export class ContactdocPage implements OnInit, ViewWillEnter, ViewWillLeave {

  loading = true;
  formationId: any = null;
  modules: any[] = [];
  /** moduleId -> date de la dernière présence enregistrée */
  presenceByModule: { [id: number]: string } = {};
  /** moduleId -> statut de la dernière réclamation ('EN_ATTENTE' | 'APPROUVEE' | 'REJETEE') */
  reclamationStatutByModule: { [id: number]: string } = {};

  private refreshTimer: any;

  constructor(
    private router: Router,
    private doctorantMe: DoctorantMeService,
    private alertCtrl: AlertController,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.doctorantMe.getProfile().subscribe({
      next: (profile) => {
        this.formationId = profile.formation_id;
        if (!this.formationId) {
          this.loading = false;
          return;
        }
        this.loadModules();
        this.loadPresences();
        this.loadReclamations();
      },
      error: () => this.loading = false
    });
  }

  ionViewWillEnter() {
    if (this.formationId) {
      this.loadPresences();
      this.loadReclamations();
    }
    this.refreshTimer = setInterval(() => {
      if (this.formationId) {
        this.loadPresences();
        this.loadReclamations();
      }
    }, 20000);
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  private loadModules() {
    this.doctorantMe.getModules(this.formationId).subscribe({
      next: (modules) => {
        this.modules = modules || [];
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  private loadPresences() {
    this.doctorantMe.getParticipations().subscribe({
      next: (participations: any[]) => {
        this.presenceByModule = {};
        (participations || []).forEach(p => this.presenceByModule[p.module_id] = p.date_participation);
      },
      error: () => { }
    });
  }

  private loadReclamations() {
    this.doctorantMe.getReclamations().subscribe({
      next: (reclamations: any[]) => {
        this.reclamationStatutByModule = {};
        (reclamations || []).forEach(r => this.reclamationStatutByModule[r.module_id] = r.statut);
      },
      error: () => { }
    });
  }

  estPresent(moduleId: number) {
    return !!this.presenceByModule[moduleId];
  }

  async signaler(module: any) {
    const alert = await this.alertCtrl.create({
      header: 'Signaler une présence manquante',
      message: `Vous déclarez avoir été présent(e) au module "${module.nom}" sans que cela ait été enregistré.`,
      inputs: [{ name: 'description', type: 'textarea', placeholder: 'Précisez (date, contexte...)' }],
      buttons: [
        { text: 'Annuler', role: 'cancel' },
        {
          text: 'Envoyer',
          handler: (data) => {
            this.doctorantMe.signalerReclamation(module.id, data.description || '').subscribe({
              next: async () => {
                this.reclamationStatutByModule[module.id] = 'EN_ATTENTE';
                const toast = await this.toastCtrl.create({
                  message: 'Réclamation envoyée à votre formateur.',
                  duration: 2500,
                  color: 'success'
                });
                toast.present();
              },
              error: async () => {
                const toast = await this.toastCtrl.create({
                  message: 'Échec de l\'envoi de la réclamation.',
                  duration: 2500,
                  color: 'danger'
                });
                toast.present();
              }
            });
          }
        }
      ]
    });
    await alert.present();
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
