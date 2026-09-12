import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { CreatePDFPageRoutingModule } from './create-pdf-routing.module';

import { CreatePDFPage } from './create-pdf.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    CreatePDFPageRoutingModule
  ],
  declarations: [CreatePDFPage]
})
export class CreatePDFPageModule {}
