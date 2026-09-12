import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ModuleNPageRoutingModule } from './module-n-routing.module';

import { ModuleNPage } from './module-n.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ModuleNPageRoutingModule
  ],
  declarations: [ModuleNPage]
})
export class ModuleNPageModule {}
