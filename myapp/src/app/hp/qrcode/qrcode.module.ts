import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { QrcodePageRoutingModule } from './qrcode-routing.module';
import { QrcodePage } from './qrcode.page';
import { CodePComponent } from "./code-p/CodePComponent";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    QrcodePageRoutingModule,
  ],
  declarations: [CodePComponent ,QrcodePage]
})
export class QrcodePageModule {}
