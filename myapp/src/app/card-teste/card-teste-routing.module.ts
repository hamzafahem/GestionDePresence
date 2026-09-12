import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CardTestePage } from './card-teste.page';

const routes: Routes = [
  {
    path: '',
    component: CardTestePage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CardTestePageRoutingModule {}
