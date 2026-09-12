import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { CarteDPageRoutingModule } from './carte-d-routing.module';

import { CarteDPage } from './carte-d.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    CarteDPageRoutingModule
  ],
  declarations: [CarteDPage]
})
export class CarteDPageModule {}
