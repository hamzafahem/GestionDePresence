import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { MesFormationsFPageRoutingModule } from './mes-formations-f-routing.module';
import { MesFormationsFPage } from './mes-formations-f.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    MesFormationsFPageRoutingModule,
  ],
  declarations: [MesFormationsFPage]
})
export class MesFormationsFPageModule {}
