import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ViewWillEnter, ViewWillLeave } from '@ionic/angular';
import { QrService } from 'src/app/services/qr.service';

@Component({
  selector: 'app-toutes-formations',
  templateUrl: './toutes-formations.page.html',
  styleUrls: ['./toutes-formations.page.scss'],
})
export class ToutesFormationsPage implements OnInit, ViewWillEnter, ViewWillLeave {

  loading = true;
  formations: any[] = [];
  searchTerm = '';

  private refreshTimer: any;

  constructor(private router: Router, private qr: QrService) { }

  ngOnInit() {
    this.loadFormations();
  }

  ionViewWillEnter() {
    this.loadFormations();
    this.refreshTimer = setInterval(() => {
      this.loadFormations();
    }, 20000);
  }

  ionViewWillLeave() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer);
      this.refreshTimer = null;
    }
  }

  private loadFormations() {
    this.qr.getFts().subscribe({
      next: (res: any) => {
        this.formations = res.items || [];
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  get filteredFormations() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      return this.formations;
    }
    return this.formations.filter(f => `${f.intitule} ${f.description || ''}`.toLowerCase().includes(term));
  }

  closemodal() {
    this.router.navigate(['/menu']);
  }
}
