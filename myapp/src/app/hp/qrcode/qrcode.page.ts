import { Component, OnDestroy, OnInit } from '@angular/core';
import { BarcodeScanner } from '@capacitor-community/barcode-scanner';
import { Router } from '@angular/router';
import { AlertController, ToastController } from '@ionic/angular';
import { QrService } from 'src/app/services/qr.service';

@Component({
  selector: 'app-qrcode',
  templateUrl: './qrcode.page.html',
  styleUrls: ['./qrcode.page.scss'],
})
export class QrcodePage implements OnInit, OnDestroy {
  bodyElement: HTMLElement | null;

  modules: any[] = [];
  selectedModuleId: any;
  formationNom = '';

  formations: any[] = [];
  selectedFormationId: any;

  mode: 'scan' | 'manual' = 'scan';
  manualCode = '';
  lastCode: string | undefined;

  result: {
    success: boolean;
    message: string;
    nom?: string;
    prenom?: string;
    doctorantId?: number;
    dejaEnregistre?: boolean;
  } | null = null;

  scanning = false;
  submitting = false;

  historique: any[] = [];
  historiqueLoading = false;
  showHistorique = false;

  private audioCtx: AudioContext | null = null;

  constructor(
    private router: Router,
    private qr: QrService,
    private toastCtrl: ToastController,
    private alertCtrl: AlertController
  ) {
    this.bodyElement = document.querySelector('body');
  }

  ngOnInit() {
    this.qr.getFts().subscribe({
      next: (res: any) => {
        this.formations = res.items || [];
        if (this.formations.length) {
          this.selectedFormationId = this.formations[0].id;
          this.updateFormationNom();
          this.loadModules();
        }
      },
      error: (err) => console.error('Erreur chargement des formations', err),
    });
  }

  /** Recalcule le nom de la formation affiché depuis la liste déjà chargée. */
  private updateFormationNom() {
    const f = this.formations.find((x) => x.id === this.selectedFormationId);
    this.formationNom = f?.intitule || '';
  }

  private loadModules() {
    if (!this.selectedFormationId) {
      this.modules = [];
      this.selectedModuleId = null;
      return;
    }
    this.qr.getModules(this.selectedFormationId).subscribe({
      next: (modules) => {
        this.modules = modules;
        this.selectedModuleId = modules.length ? modules[0].id : null;
      },
      error: (err) => console.error('Erreur chargement des modules', err),
    });
  }

  /** L'utilisateur change de formation dans le header : recharge les modules et repart de zéro. */
  onFormationChange() {
    if (this.scanning) {
      this.stopScan();
    }
    this.result = null;
    this.lastCode = undefined;
    this.updateFormationNom();
    this.loadModules();
  }

  selectMode(mode: 'scan' | 'manual') {
    this.mode = mode;
    if (mode === 'manual' && this.scanning) {
      // La caméra native tourne en arrière-plan tant qu'on ne l'arrête pas
      // explicitement : passer en saisie manuelle doit la couper.
      this.stopScan();
    } else if (mode === 'scan' && !this.result && !this.scanning) {
      this.startScan();
    }
  }

  /** Le scan ne doit démarrer que si une formation ET un module sont sélectionnés. */
  private async ensureFormationEtModuleSelectionnes(): Promise<boolean> {
    if (this.selectedFormationId && this.selectedModuleId) {
      return true;
    }
    const toast = await this.toastCtrl.create({
      message: 'Sélectionnez une formation et un module avant de scanner.',
      duration: 2500,
      color: 'warning'
    });
    toast.present();
    return false;
  }

  async checkPermission() {
    try {
      const status = await BarcodeScanner.checkPermission({ force: true });

      if (status.denied) {
        // L'utilisateur a définitivement refusé l'accès caméra : Android ne
        // réaffichera plus jamais la popup système, seul le réglage manuel
        // dans les paramètres de l'app peut la débloquer.
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
    const ready = await this.ensureFormationEtModuleSelectionnes();
    if (!ready) {
      return;
    }

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
        this.marquerPresence(scanResult.content);
      }
    } catch (err) {
      this.stopScan();
      const toast = await this.toastCtrl.create({ message: 'Erreur du scanner QR.', duration: 3000, color: 'danger' });
      toast.present();
    }
  }

  /** Relance manuellement la caméra (bouton explicite, en plus du redémarrage automatique après un scan). */
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
    this.marquerPresence(code);
  }

  private marquerPresence(code: string) {
    if (!this.selectedModuleId) {
      return;
    }
    this.submitting = true;
    this.lastCode = code;

    this.qr.setQr(this.selectedFormationId, this.selectedModuleId, code).subscribe({
      next: (res) => {
        this.submitting = false;
        this.result = {
          success: res.success,
          message: res.message,
          nom: res.doctorant_nom,
          prenom: res.doctorant_prenom,
          doctorantId: res.doctorant_id,
          dejaEnregistre: res.deja_enregistre
        };
        if (!res.success) {
          this.playFeedback('failed');
        } else if (res.deja_enregistre) {
          this.playFeedback('duplicate');
        } else {
          this.playFeedback('fresh');
        }
      },
      error: (err) => {
        this.submitting = false;
        this.result = { success: false, message: err.error?.message || 'Code introuvable' };
        this.playFeedback('failed');
      },
    });
  }

  /**
   * Bip + vibration selon le résultat du scan :
   *  - fresh     : présence fraîchement enregistrée -> bip aigu, pas de vibration.
   *  - duplicate : déjà enregistrée aujourd'hui -> bip aigu + vibration courte (à surveiller par le formateur).
   *  - failed    : code introuvable / doctorant absent de la formation -> bip grave + double vibration.
   */
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

  /** Annule la présence qui vient d'être enregistrée (pas d'effet si elle était déjà enregistrée avant). */
  annuler() {
    if (this.result?.success && !this.result.dejaEnregistre && this.result.doctorantId && this.selectedModuleId) {
      this.qr.retirerPresence(this.selectedFormationId, this.selectedModuleId, this.result.doctorantId).subscribe();
    }
    this.reset();
  }

  /** Acquitte le résultat affiché et prépare la prochaine saisie. */
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
    this.lastCode = undefined;
  }

  /** Traçabilité : historique des présences déjà enregistrées pour le module sélectionné. */
  ouvrirHistorique() {
    if (this.scanning) {
      this.stopScan();
    }
    this.showHistorique = true;
    this.historiqueLoading = true;
    this.qr.getPresences(this.selectedFormationId, this.selectedModuleId).subscribe({
      next: (rows: any[]) => {
        this.historique = (rows || []).slice().sort((a, b) =>
          new Date(b.date_participation).getTime() - new Date(a.date_participation).getTime()
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
