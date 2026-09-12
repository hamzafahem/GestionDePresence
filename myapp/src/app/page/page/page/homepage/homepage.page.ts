import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController, ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.page.html',
  styleUrls: ['./homepage.page.scss'],
})
export class HomepagePage implements OnInit, ViewWillEnter, ViewWillLeave {
  subjects: any;
  fullName = '';
  firstName = '';
  isDoctorant = false;
  photo: string | null = null;
  roleLabel = '';

  formations: any[] = [];
  participations: any[] = [];
  /** formationId -> statut de la dernière demande d'inscription ('EN_ATTENTE' | 'APPROUVEE' | 'REJETEE') */
  inscriptionStatutByFormation: { [id: number]: string } = {};
  /** formationId -> id de la demande d'inscription (pour pouvoir l'annuler tant qu'elle est EN_ATTENTE) */
  inscriptionIdByFormation: { [id: number]: number } = {};
  loading = false;

  private refreshTimer: any;

  constructor(
    private router: Router,
    private lss: LocalStorageService,
    private doctorantMe: DoctorantMeService,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.fullName = this.lss.getFullName();
    this.firstName = this.fullName.split(' ')[0] || this.fullName;
    this.isDoctorant = this.lss.isDoctorant();
    this.photo = this.lss.getPhoto();
    this.roleLabel = this.lss.getRoleLabel();

    if (this.isDoctorant) {
      this.loadDoctorantAccueil();
      return;
    }

    this.subjects = [
      {
        name: 'Formations',
        icon: 'school-outline',
        color: 'violet',
        desc: 'Consultez toutes les formations, modules et parcours pédagogiques disponibles.'
      },
      {
        name: 'Mes présences',
        icon: 'people-outline',
        color: 'blue',
        desc: 'Visualisez vos feuilles de présence et suivez votre assiduité.'
      },
      {
        name: 'Scanner un QR code',
        icon: 'qr-code-outline',
        color: 'green',
        desc: 'Scannez les QR codes pour enregistrer rapidement vos présences.'
      },
      {
        name: 'Gestion des réclamations',
        icon: 'document-text-outline',
        color: 'teal',
        desc: 'Soumettez vos réclamations et suivez leur traitement.'
      },
      {
        name: 'Mes documents',
        icon: 'cloud-download-outline',
        color: 'orange',
        desc: 'Téléchargez vos documents (présences, attestations, etc.).'
      },
      {
        name: 'Mon profil',
        icon: 'person-outline',
        color: 'pink',
        desc: 'Consultez et mettez à jour vos informations personnelles.'
      },
      {
        name: 'Mes formations',
        icon: 'person-video3-outline',
        color: 'violet',
        desc: 'Consultez vos affectations et demandez à être affecté à une formation ou un module.'
      },
    ];
  }

  ionViewWillEnter() {
    if (this.isDoctorant) {
      this.loadDoctorantAccueil();
      this.refreshTimer = setInterval(() => {
        this.loadDoctorantAccueil();
      }, 20000);
    }
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  private loadDoctorantAccueil() {
    this.loading = true;
    this.doctorantMe.getFormations().subscribe({
      next: (res) => this.formations = res.items || [],
      error: () => { }
    });
    this.doctorantMe.getParticipations().subscribe({
      next: (res) => {
        this.participations = res || [];
        this.loading = false;
      },
      error: () => this.loading = false
    });
    this.refreshInscriptions();
  }

  get approvedFormations() {
    return this.formations.filter(f => this.inscriptionStatutByFormation[f.id] === 'APPROUVEE');
  }

  /** "Nouvelles formations" : celles pas encore approuvées (nouvelles ou demande en cours/rejetée) — une fois approuvée, une formation vit dans "Mes présences", plus ici. */
  get nouvellesFormations() {
    return this.formations.filter(f => this.inscriptionStatutByFormation[f.id] !== 'APPROUVEE');
  }

  presenceCountByFormation(formationId: number) {
    return this.participations.filter(p => p.formation_id === formationId).length;
  }

  private refreshInscriptions() {
    this.doctorantMe.getInscriptions().subscribe({
      next: (res: any[]) => {
        this.inscriptionStatutByFormation = {};
        this.inscriptionIdByFormation = {};
        (res || []).forEach(i => {
          this.inscriptionStatutByFormation[i.formation_id] = i.statut;
          this.inscriptionIdByFormation[i.formation_id] = i.id;
        });
      },
      error: () => { }
    });
  }

  annulerInscription(formationId: number) {
    const id = this.inscriptionIdByFormation[formationId];
    if (!id) {
      return;
    }
    this.doctorantMe.annulerInscription(id).subscribe({
      next: async () => {
        this.refreshInscriptions();
        const toast = await this.toastCtrl.create({
          message: "Demande d'inscription annulée.",
          duration: 2500,
          color: 'medium'
        });
        toast.present();
      },
      error: async () => {
        const toast = await this.toastCtrl.create({
          message: "Échec de l'annulation.",
          duration: 2500,
          color: 'danger'
        });
        toast.present();
      }
    });
  }

  sinscrire(formationId: number) {
    this.doctorantMe.sinscrire(formationId).subscribe({
      next: async () => {
        this.refreshInscriptions();
        const toast = await this.toastCtrl.create({
          message: "Demande d'inscription envoyée.",
          duration: 2500,
          color: 'success'
        });
        toast.present();
      },
      error: async () => {
        const toast = await this.toastCtrl.create({
          message: "Échec de la demande d'inscription.",
          duration: 2500,
          color: 'danger'
        });
        toast.present();
      }
    });
  }

  logout() {
    this.lss.logout();
    this.router.navigateByUrl('/sign-in');
  }

  subject(p: string) {
    if (p === "Scanner un QR code") {
      this.router.navigate(['/qrcode']);
    } else if (p === "Mes documents") {
      this.router.navigate(['/hajar']);
    } else if (p === "Gestion des réclamations") {
      this.router.navigate(['/reclamations-f']);
    } else if (p === "Formations") {
      this.router.navigate(['/menu']);
    } else if (p === "Mes présences") {
      this.router.navigate(['/contact']);
    } else if (p === "Mon profil") {
      this.router.navigate(['/profil']);
    } else if (p === "Mes formations") {
      this.router.navigate(['/mes-formations-f']);
    }
  }
}
