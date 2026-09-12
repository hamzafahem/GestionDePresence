import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CarteDPage } from './carte-d.page';

const routes: Routes = [
  {
    path: '',
    component: CarteDPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CarteDPageRoutingModule {}
