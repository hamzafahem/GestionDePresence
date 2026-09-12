
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { DoctPageRoutingModule } from './doct-routing.module';

import { DoctPage } from './doct.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    DoctPageRoutingModule
  ],
  declarations: [DoctPage]
})
export class DoctPageModule {}
