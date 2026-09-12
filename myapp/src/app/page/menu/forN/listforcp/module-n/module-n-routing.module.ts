import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ModuleNPage } from './module-n.page';

const routes: Routes = [
  {
    path: '',
    component: ModuleNPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ModuleNPageRoutingModule {}
