import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ListforcpPageRoutingModule } from './listforcp-routing.module';

import { ListforcpPage } from './listforcp.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ListforcpPageRoutingModule
  ],
  declarations: [ListforcpPage]
})
export class ListforcpPageModule {}
