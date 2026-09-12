import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { CardTestePageRoutingModule } from './card-teste-routing.module';

import { CardTestePage } from './card-teste.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    CardTestePageRoutingModule
  ],
  declarations: [CardTestePage]
})
export class CardTestePageModule {}
