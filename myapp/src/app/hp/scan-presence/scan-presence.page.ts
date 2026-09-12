import { Component, OnDestroy, OnInit } from '@angular/core';
import { BarcodeScanner } from '@capacitor-community/barcode-scanner';
import { Router } from '@angular/router';
import { AlertController, ToastController } from '@ionic/angular';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';
import { MODULE_QR_PREFIX } from 'src/app/services/qr.service';

@Component({
  selector: 'app-scan-presence',
  templateUrl: './scan-presence.page.html',
  styleUrls: ['./scan-presence.page.scss'],
})
export class ScanPresencePage implements OnInit, OnDestroy {
  bodyElement: HTMLElement | null;

  mode: 'scan' | 'manual' = 'scan';
  manualCode = '';
  scanning = false;
  submitting = false;

  result: {
    success: boolean;
    message: string;
    moduleNom?: string;
  } | null = null;

  historique: any[] = [];
  historiqueLoading = false;
  showHistorique = false;

  private audioCtx: AudioContext | null = null;

  constructor(
    private router: Router,
    private doctorantMe: DoctorantMeService,
    private toastCtrl: ToastController,
    private alertCtrl: AlertController
  ) {
    this.bodyElement = document.querySelector('body');
  }

  ngOnInit() {
    this.startScan();
  }

  selectMode(mode: 'scan' | 'manual') {
    this.mode = mode;
    if (mode === 'manual' && this.scanning) {
      this.stopScan();
    } else if (mode === 'scan' && !this.result && !this.scanning) {
      this.startScan();
    }
  }

  async checkPermission() {
    try {
      const status = await BarcodeScanner.checkPermission({ force: true });

      if (status.denied) {
        const alert = await this.alertCtrl.create({
          header: 'Accès à la caméra refusé',
          message: "Le scan du QR code nécessite l'accès à la caméra. Ouvrez les paramètres de l'application et activez la permission Caméra.",
          buttons: [
            { text: 'Annuler', role: 'cancel' },
            { text: 'Ouvrir les paramètres', handler: () => BarcodeScanner.openAppSettings() }
          ]
        });
        await alert.present();
        return false;
      }

      return status.granted;
    } catch (err) {
      const toast = await this.toastCtrl.create({
        message: "Impossible d'accéder à la caméra sur cet appareil.",
        duration: 3000,
        color: 'danger'
      });
      toast.present();
      return false;
    }
  }

  async startScan() {
    const hasPermission = await this.checkPermission();
    if (!hasPermission) {
      return;
    }

    this.scanning = true;
    if (this.bodyElement) {
      this.bodyElement.classList.add('scanner-active');
    }

    try {
      await BarcodeScanner.hideBackground();
      const scanResult = await BarcodeScanner.startScan();
      this.stopScan();

      if (scanResult.hasContent) {
        this.traiterCodeScanne(scanResult.content);
      }
    } catch (err) {
      this.stopScan();
      const toast = await this.toastCtrl.create({ message: 'Erreur du scanner QR.', duration: 3000, color: 'danger' });
      toast.present();
    }
  }

  relancerCamera() {
    if (!this.scanning) {
      this.startScan();
    }
  }

  stopScan() {
    BarcodeScanner.showBackground();
    BarcodeScanner.stopScan();
    this.scanning = false;
    if (this.bodyElement) {
      this.bodyElement.classList.remove('scanner-active');
    }
  }

  soumettreCodeManuel() {
    const code = this.manualCode.trim();
    if (!code) {
      return;
    }
    this.traiterCodeScanne(code);
  }

  private traiterCodeScanne(content: string) {
    if (!content.startsWith(MODULE_QR_PREFIX)) {
      this.result = { success: false, message: "Ce code n'est pas un QR code de module CEDoc valide." };
      this.playFeedback('failed');
      return;
    }

    const moduleId = content.substring(MODULE_QR_PREFIX.length).trim();
    if (!moduleId) {
      this.result = { success: false, message: 'Code de module illisible.' };
      this.playFeedback('failed');
      return;
    }

    this.submitting = true;
    this.doctorantMe.checkin(moduleId).subscribe({
      next: (res: any) => {
        this.submitting = false;
        this.result = {
          success: true,
          message: "Demande envoyée : en attente d'approbation du formateur.",
          moduleNom: res.module_nom
        };
        this.playFeedback('fresh');
      },
      error: (err) => {
        this.submitting = false;
        this.result = { success: false, message: err.error?.message || 'Échec de la demande de pointage.' };
        this.playFeedback('failed');
      }
    });
  }

  private playFeedback(status: 'fresh' | 'duplicate' | 'failed') {
    const success = status !== 'failed';
    try {
      if (!this.audioCtx) {
        this.audioCtx = new (window.AudioContext || (window as any).webkitAudioContext)();
      }
      const ctx = this.audioCtx;
      const oscillator = ctx.createOscillator();
      const gain = ctx.createGain();
      oscillator.connect(gain);
      gain.connect(ctx.destination);

      oscillator.type = 'sine';
      oscillator.frequency.value = success ? 880 : 220;
      gain.gain.setValueAtTime(0.15, ctx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + (success ? 0.18 : 0.35));

      oscillator.start();
      oscillator.stop(ctx.currentTime + (success ? 0.18 : 0.35));
    } catch (err) {
      // Best-effort : le son n'est pas critique pour la fonctionnalité.
    }

    try {
      if ('vibrate' in navigator) {
        if (status === 'duplicate') {
          navigator.vibrate(120);
        } else if (status === 'failed') {
          navigator.vibrate([100, 60, 100]);
        }
      }
    } catch (err) {
      // Best-effort.
    }
  }

  confirmer() {
    if (!this.result && this.mode === 'manual') {
      this.soumettreCodeManuel();
      return;
    }
    this.reset();
    if (this.mode === 'scan') {
      this.startScan();
    }
  }

  private reset() {
    this.result = null;
    this.manualCode = '';
  }

  ouvrirHistorique() {
    if (this.scanning) {
      this.stopScan();
    }
    this.showHistorique = true;
    this.historiqueLoading = true;
    this.doctorantMe.getCheckins().subscribe({
      next: (rows: any[]) => {
        this.historique = (rows || []).slice().sort((a, b) =>
          new Date(b.date_creation).getTime() - new Date(a.date_creation).getTime()
        );
        this.historiqueLoading = false;
      },
      error: () => this.historiqueLoading = false
    });
  }

  fermerHistorique() {
    this.showHistorique = false;
  }

  ngOnDestroy() {
    this.stopScan();
  }

  closeModal() {
    this.stopScan();
    this.router.navigate(['/hamza']);
  }
}
