import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { QrService } from 'src/app/services/qr.service';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

@Component({
  selector: 'app-create-pdf',
  templateUrl: './create-pdf.page.html',
  styleUrls: ['./create-pdf.page.scss'],
})
export class CreatePDFPage implements OnInit {

  formateurName = '';
  today = new Date();

  formations: any[] = [];
  modules: any[] = [];
  selectedFormationId: any = null;
  selectedModuleId: any = null;
  selectedFormationIntitule = '';
  selectedModuleNom = '';

  presences: any[] = [];
  loading = false;
  generatingPdf = false;

  constructor(
    private router: Router,
    private qr: QrService,
    private lss: LocalStorageService
  ) { }

  ngOnInit() {
    this.formateurName = this.lss.getFullName();

    this.qr.getFts().subscribe({
      next: (res) => {
        this.formations = res.items || [];
        if (this.formations.length) {
          this.selectedFormationId = this.formations[0].id;
          this.onFormationChange();
        }
      },
      error: () => { }
    });
  }

  onFormationChange() {
    const formation = this.formations.find(f => f.id === this.selectedFormationId);
    this.selectedFormationIntitule = formation?.intitule || '';
    this.modules = [];
    this.selectedModuleId = null;
    this.presences = [];

    if (!this.selectedFormationId) {
      return;
    }

    this.qr.getModules(this.selectedFormationId).subscribe({
      next: (res) => {
        this.modules = res || [];
        if (this.modules.length) {
          this.selectedModuleId = this.modules[0].id;
          this.onModuleChange();
        }
      },
      error: () => { }
    });
  }

  onModuleChange() {
    const module = this.modules.find(m => m.id === this.selectedModuleId);
    this.selectedModuleNom = module?.nom || '';

    if (!this.selectedFormationId || !this.selectedModuleId) {
      this.presences = [];
      return;
    }

    this.loading = true;
    this.qr.getPresences(this.selectedFormationId, this.selectedModuleId).subscribe({
      next: (res) => {
        this.presences = res || [];
        this.loading = false;
      },
      error: () => {
        this.presences = [];
        this.loading = false;
      }
    });
  }

  async createPdf() {
    const pdfBlock = document.getElementById('print-wrapper');
    if (!pdfBlock) {
      return;
    }

    this.generatingPdf = true;
    try {
      const canvas = await html2canvas(pdfBlock, { scale: 2 });
      const imageData = canvas.toDataURL('image/jpeg', 1.0);

      const doc = new jsPDF('p', 'mm', 'a4');
      const pageWidth = doc.internal.pageSize.getWidth();
      const imgHeight = (canvas.height * pageWidth) / canvas.width;
      doc.addImage(imageData, 'JPEG', 0, 10, pageWidth, imgHeight);

      const fileName = `presence-${this.selectedModuleNom || 'module'}.pdf`;
      doc.save(fileName);
    } catch (e) {
      console.error('Erreur génération PDF', e);
    } finally {
      this.generatingPdf = false;
    }
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
