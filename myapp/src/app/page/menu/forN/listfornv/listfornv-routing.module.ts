import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ListfornvPage } from './listfornv.page';

const routes: Routes = [
  {
    path: '',
    component: ListfornvPage
  },
  {
    path: 'module-c',
    loadChildren: () => import('./module-c/module-c.module').then( m => m.ModuleCPageModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ListfornvPageRoutingModule {}
