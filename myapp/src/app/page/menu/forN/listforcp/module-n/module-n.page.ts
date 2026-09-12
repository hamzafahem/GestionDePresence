import { Component, OnInit } from '@angular/core';
import { QrService } from 'src/app/services/qr.service';
import { SelectedFormationService } from 'src/app/services/selected-formation.service';

@Component({
  selector: 'app-module-n',
  templateUrl: './module-n.page.html',
  styleUrls: ['./module-n.page.scss'],
})
export class ModuleNPage implements OnInit {

  formationId: number | null = null;
  formationIntitule = '';
  modules: any[] = [];
  loading = true;

  expandedModuleId: any = null;
  roster: any[] = [];
  rosterLoading = false;

  constructor(
    private qr: QrService,
    public selected: SelectedFormationService
  ) { }

  ngOnInit() {
    this.formationId = this.selected.id;
    this.formationIntitule = this.selected.intitule || '';

    if (!this.formationId) {
      this.loading = false;
      return;
    }

    this.qr.getModules(this.formationId).subscribe({
      next: (res) => {
        this.modules = res || [];
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  toggleRoster(module: any) {
    if (this.expandedModuleId === module.id) {
      this.expandedModuleId = null;
      return;
    }
    this.expandedModuleId = module.id;
    this.roster = [];
    this.rosterLoading = true;

    this.qr.getRoster(this.formationId, module.id).subscribe({
      next: (rows) => {
        this.roster = rows || [];
        this.rosterLoading = false;
      },
      error: () => this.rosterLoading = false
    });
  }
}
