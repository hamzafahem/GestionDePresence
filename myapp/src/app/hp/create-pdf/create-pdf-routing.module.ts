import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CreatePDFPage } from './create-pdf.page';

const routes: Routes = [
  {
    path: '',
    component: CreatePDFPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CreatePDFPageRoutingModule {}
