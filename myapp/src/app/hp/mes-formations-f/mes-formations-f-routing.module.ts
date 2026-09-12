import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MesFormationsFPage } from './mes-formations-f.page';

const routes: Routes = [
  {
    path: '',
    component: MesFormationsFPage
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MesFormationsFPageRoutingModule {}
