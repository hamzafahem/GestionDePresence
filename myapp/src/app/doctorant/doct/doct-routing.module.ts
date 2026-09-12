
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DoctPage } from './doct.page';

const routes: Routes = [
  {
    path: '',
    component: DoctPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DoctPageRoutingModule {}
