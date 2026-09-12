import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ReclamationsFPageRoutingModule } from './reclamations-f-routing.module';
import { ReclamationsFPage } from './reclamations-f.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ReclamationsFPageRoutingModule,
  ],
  declarations: [ReclamationsFPage]
})
export class ReclamationsFPageModule {}
