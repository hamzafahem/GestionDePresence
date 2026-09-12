import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ScanPresencePage } from './scan-presence.page';

const routes: Routes = [
  {
    path: '',
    component: ScanPresencePage
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ScanPresencePageRoutingModule {}
