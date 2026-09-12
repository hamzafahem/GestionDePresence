import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ModuleCPageRoutingModule } from './module-c-routing.module';

import { ModuleCPage } from './module-c.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ModuleCPageRoutingModule
  ],
  declarations: [ModuleCPage]
})
export class ModuleCPageModule {}
