import { Component, OnInit } from '@angular/core';
import { QrService } from 'src/app/services/qr.service';
import { SelectedFormationService } from 'src/app/services/selected-formation.service';

@Component({
  selector: 'app-listfornv',
  templateUrl: './listfornv.page.html',
  styleUrls: ['./listfornv.page.scss'],
})
export class ListfornvPage implements OnInit {

  allFormations: any[] = [];
  Formation: any[] = [];
  loading = true;
  searchTerm = '';

  constructor(
    private qr: QrService,
    private selected: SelectedFormationService
  ) { }

  ngOnInit() {
    this.qr.getFts().subscribe({
      next: (res) => {
        const today = new Date();
        this.allFormations = (res.items || []).filter((f: any) => !f.date_fin || new Date(f.date_fin) >= today);
        this.Formation = this.allFormations;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  onSearchChange(event: any) {
    const term = (event?.detail?.value || '').toLowerCase();
    this.searchTerm = term;
    this.Formation = term
      ? this.allFormations.filter(f => (f.intitule || '').toLowerCase().includes(term))
      : this.allFormations;
  }

  selectFormation(item: any) {
    this.selected.id = item.id;
    this.selected.intitule = item.intitule;
  }
}
