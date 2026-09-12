import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { QrService } from 'src/app/services/qr.service';

/**
 * "Gestion des réclamations" — vue de consultation en LECTURE SEULE pour le
 * Formateur/Admin : liste toutes les demandes (réclamations de présence,
 * auto-scans QR, etc.) avec leur statut. Aucune action d'approbation/rejet
 * ici — ce traitement existe déjà dans hp/contact (Gestion de présence).
 */
@Component({
  selector: 'app-reclamations-f',
  templateUrl: './reclamations-f.page.html',
  styleUrls: ['./reclamations-f.page.scss'],
})
export class ReclamationsFPage implements OnInit, ViewWillEnter, ViewWillLeave {

  loading = true;
  demandes: any[] = [];
  expandedId: any = null;

  private refreshTimer: any;

  constructor(
    private router: Router,
    private qr: QrService
  ) { }

  ngOnInit() {
    this.loadDemandes();
  }

  ionViewWillEnter() {
    this.loadDemandes();
    this.refreshTimer = setInterval(() => {
      this.loadDemandes();
    }, 20000);
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  private loadDemandes() {
    this.loading = true;
    this.qr.getDemandes().subscribe({
      next: (rows: any[]) => {
        this.demandes = (rows || []).slice().sort((a, b) => {
          const da = a.date_creation ? new Date(a.date_creation).getTime() : 0;
          const db = b.date_creation ? new Date(b.date_creation).getTime() : 0;
          return db - da;
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  typeLabel(type: string): string {
    switch (type) {
      case 'RECLAMATION_PRESENCE': return 'Réclamation présence';
      case 'AUTOCHECKIN_PRESENCE': return 'Auto-scan QR';
      default: return type || '—';
    }
  }

  statutLabel(statut: string): string {
    switch (statut) {
      case 'EN_ATTENTE': return 'En attente';
      case 'APPROUVEE': return 'Approuvée';
      case 'REJETEE': return 'Rejetée';
      default: return statut || '—';
    }
  }

  toggleExpand(d: any) {
    this.expandedId = this.expandedId === d.id ? null : d.id;
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
