import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ContactdocPageRoutingModule } from './contactdoc-routing.module';

import { ContactdocPage } from './contactdoc.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ContactdocPageRoutingModule
  ],
  declarations: [ContactdocPage]
})
export class ContactdocPageModule {}
