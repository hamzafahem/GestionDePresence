import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ToutesFormationsPageRoutingModule } from './toutes-formations-routing.module';
import { ToutesFormationsPage } from './toutes-formations.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ToutesFormationsPageRoutingModule,
  ],
  declarations: [ToutesFormationsPage]
})
export class ToutesFormationsPageModule {}
