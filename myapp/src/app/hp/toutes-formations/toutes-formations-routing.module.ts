import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ToutesFormationsPage } from './toutes-formations.page';

const routes: Routes = [
  {
    path: '',
    component: ToutesFormationsPage
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ToutesFormationsPageRoutingModule {}
