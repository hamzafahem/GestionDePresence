import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ContactdocPage } from './contactdoc.page';

const routes: Routes = [
  {
    path: '',
    component: ContactdocPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ContactdocPageRoutingModule {}
