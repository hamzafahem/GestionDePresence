import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ListforcpPage } from './listforcp.page';

const routes: Routes = [
  {
    path: '',
    component: ListforcpPage
  },
  {
    path: 'module-n',
    loadChildren: () => import('./module-n/module-n.module').then( m => m.ModuleNPageModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ListforcpPageRoutingModule {}
