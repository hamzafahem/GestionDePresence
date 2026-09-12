// import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';


// import { IonicModule } from '@ionic/angular';

// import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';




// import { MenuPageRoutingModule } from './menu-routing.module';

// import { MenuPage } from './menu.page';

// @NgModule({
//   imports: [
//     CommonModule, IonicModule,

//     FormsModule,
//     IonicModule,
//     MenuPageRoutingModule
//   ],
//   declarations: [MenuPage]
// })

// export class MenuPageModule {
// }
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { MenuPageRoutingModule } from './menu-routing.module';
import { MenuPage } from './menu.page';

@NgModule({
imports: [
CommonModule,
FormsModule,
IonicModule,
MenuPageRoutingModule
],
declarations: [MenuPage],
schemas: [CUSTOM_ELEMENTS_SCHEMA] // Ajout du schéma CUSTOM_ELEMENTS_SCHEMA
})
export class MenuPageModule {}
