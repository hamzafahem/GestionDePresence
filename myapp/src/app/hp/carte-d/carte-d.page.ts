import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AlertController, ToastController, ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';
import * as QRCode from 'qrcode';
import JsBarcode from 'jsbarcode';

@Component({
  selector: 'app-carte-d',
  templateUrl: './carte-d.page.html',
  styleUrls: ['./carte-d.page.scss'],
})
export class CarteDPage implements OnInit, AfterViewChecked, ViewWillEnter, ViewWillLeave {
  profile: any = {};
  loading = true;
  rotation = 0;

  demandesCarte: any[] = [];
  envoiEnCours = false;

  @ViewChild('qrCanvas') qrCanvasRef?: ElementRef<HTMLCanvasElement>;
  @ViewChild('barcodeSvg') barcodeSvgRef?: ElementRef<SVGElement>;
  @ViewChild('flipStage') flipStageRef?: ElementRef<HTMLDivElement>;

  private codesRendered = false;
  dragging = false;
  private startX = 0;
  private startRotation = 0;

  private refreshTimer: any;

  constructor(
    private router: Router,
    private doctorantMe: DoctorantMeService,
    private alertCtrl: AlertController,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.doctorantMe.getProfile().subscribe({
      next: (res) => {
        this.profile = res;
        this.loading = false;
      },
      error: () => this.loading = false
    });
    this.loadDemandesCarte();
  }

  ionViewWillEnter() {
    this.loadDemandesCarte();
    this.refreshTimer = setInterval(() => {
      this.loadDemandesCarte();
    }, 20000);
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  private loadDemandesCarte() {
    this.doctorantMe.getDemandesCarte().subscribe({
      next: (rows: any[]) => {
        this.demandesCarte = (rows || []).slice().sort((a, b) =>
          new Date(b.date_creation).getTime() - new Date(a.date_creation).getTime()
        );
      },
      error: () => { }
    });
  }

  /** La plus récente demande de nouvelle carte, s'il y en a une. */
  get derniereDemandeCarte() {
    return this.demandesCarte.length ? this.demandesCarte[0] : null;
  }

  async demanderNouvelleCarte() {
    const alert = await this.alertCtrl.create({
      header: 'Demander une nouvelle carte',
      message: "Précisez la raison (carte perdue, endommagée...). L'administration traitera votre demande.",
      inputs: [{ name: 'description', type: 'textarea', placeholder: 'Raison de la demande' }],
      buttons: [
        { text: 'Annuler', role: 'cancel' },
        {
          text: 'Envoyer',
          handler: (data) => {
            this.envoiEnCours = true;
            this.doctorantMe.demanderNouvelleCarte(data.description || '').subscribe({
              next: async () => {
                this.envoiEnCours = false;
                this.loadDemandesCarte();
                const toast = await this.toastCtrl.create({
                  message: 'Demande envoyée à l\'administration.',
                  duration: 2500,
                  color: 'success'
                });
                toast.present();
              },
              error: async () => {
                this.envoiEnCours = false;
                const toast = await this.toastCtrl.create({
                  message: "Échec de l'envoi de la demande.",
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

  ngAfterViewChecked() {
    if (!this.loading && !this.codesRendered && this.qrCanvasRef && this.barcodeSvgRef) {
      this.codesRendered = true;
      this.renderCodes();
    }
  }

  private renderCodes() {
    const code = this.profile.code_apogee || '';
    if (this.qrCanvasRef) {
      QRCode.toCanvas(this.qrCanvasRef.nativeElement, code, { width: 110, margin: 1 }).catch(() => { });
    }
    if (this.barcodeSvgRef) {
      try {
        JsBarcode(this.barcodeSvgRef.nativeElement, code, {
          format: 'CODE128', width: 1.6, height: 34, displayValue: true, fontSize: 11, margin: 4
        });
      } catch (err) { }
    }
  }

  flip() {
    this.rotation += 180;
  }

  onPointerDown(event: PointerEvent) {
    this.dragging = true;
    this.startX = event.clientX;
    this.startRotation = this.rotation;
    (event.target as HTMLElement).setPointerCapture(event.pointerId);
  }

  onPointerMove(event: PointerEvent) {
    if (!this.dragging) {
      return;
    }
    const delta = event.clientX - this.startX;
    this.rotation = this.startRotation + delta * 0.6;
  }

  onPointerUp() {
    if (!this.dragging) {
      return;
    }
    this.dragging = false;
    this.rotation = Math.round(this.rotation / 180) * 180;
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
