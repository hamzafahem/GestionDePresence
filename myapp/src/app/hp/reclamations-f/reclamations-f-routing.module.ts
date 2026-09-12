import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ReclamationsFPage } from './reclamations-f.page';

const routes: Routes = [
  {
    path: '',
    component: ReclamationsFPage
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReclamationsFPageRoutingModule {}
