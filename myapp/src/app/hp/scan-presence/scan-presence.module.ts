import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ScanPresencePageRoutingModule } from './scan-presence-routing.module';
import { ScanPresencePage } from './scan-presence.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ScanPresencePageRoutingModule,
  ],
  declarations: [ScanPresencePage]
})
export class ScanPresencePageModule {}
