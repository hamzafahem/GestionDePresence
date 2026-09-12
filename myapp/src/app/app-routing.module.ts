
import {  Component , NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { OtpComponent } from './page/page/page/sign-up/page/sign-up/otp/otp.component';
import { HomepagePage } from './page/page/page/homepage/homepage.page';
import { ProfilPage } from './page/page/page/profil/profil.page';
import { ChangePasswordPage } from '../app/page/page/page/change-password/change-password.page';
import { SettingPage } from '../app/page/page/page/setting/setting.page';
import { CreatePDFPage } from './hp/create-pdf/create-pdf.page'; // Importez le composant CreatePDFPage ici


import {
  redirectUnauthorizedTo,
  redirectLoggedInTo,
  canActivate
} from  '@angular/fire/auth-guard'
const C =()=>redirectUnauthorizedTo(['']);
const redirectUnauthorizedTomenu =()=>redirectUnauthorizedTo(['menu']);
const routes: Routes = [
  {
    path: 'M',
    loadChildren: () => import('./home/home.module').then(m => m.HomePageModule)
  },
  {
    path: '',
    redirectTo: 'sign-in',
    pathMatch: 'full'
  },
  {
    path: 'sign-in',
    loadChildren: () => import('./page/page/sign-in/sign-in.module').then(m => m.SignInPageModule)
    // ...canActivate(redirectUnauthorizedTomenu)
  },
  {
    path: 'sign-up',
    loadChildren: () => import('./page/page/page/sign-up/sign-up.module').then(m => m.SignUpPageModule)
  },
  {
    path: 'otp',
    component: OtpComponent
  },

  {
    path: 'profil',
    component: ProfilPage
  },
  {
    path: 'hamza',
    component: HomepagePage
  },

   {
    path: 'Change-Password',
    component: ChangePasswordPage
  },
  {
    path: 'setting',
    component: SettingPage
  },

  {
    path: 'qrcode',
    loadChildren: () => import('./hp/qrcode/qrcode.module').then( m => m.QrcodePageModule)
  },
  {

    path: 'hajar',
    loadChildren: () => import('./hp/create-pdf/create-pdf.module').then( m => m.CreatePDFPageModule)
  },

  {
    path: 'carte-d',
    loadChildren: () => import('./hp/carte-d/carte-d.module').then( m => m.CarteDPageModule)
  },
  {
    path: 'scan-presence',
    loadChildren: () => import('./hp/scan-presence/scan-presence.module').then( m => m.ScanPresencePageModule)
  },
  {
    path: 'mes-formations-f',
    loadChildren: () => import('./hp/mes-formations-f/mes-formations-f.module').then( m => m.MesFormationsFPageModule)
  },
  {
    path: 'reclamations-f',
    loadChildren: () => import('./hp/reclamations-f/reclamations-f.module').then( m => m.ReclamationsFPageModule)
  },
  {
    path: 'toutes-formations',
    loadChildren: () => import('./hp/toutes-formations/toutes-formations.module').then( m => m.ToutesFormationsPageModule)
  },
  {
    path: 'menu',
    loadChildren: () => import('./page/menu/menu.module').then( m => m.MenuPageModule)
    // ...canActivate(redirectUnauthorizedTomenu)
  },
  // {
  //   path: 'edit/ID',
  //   loadChildren: () => import('./doctorant/edit/edit.module').then( m => m.EditPageModule)
  // },

  // {
  //   path: 'doct',
  //   loadChildren: () => import('./doctorant/doct/doct.module').then( m => m.DoctPageModule)
  // },
  {
    path: 'del',
    loadChildren: () => import('./doctorant/delete/delete.module').then( m => m.DeletePageModule)
  },
  // {
  //   path: 'create',
  //   loadChildren: () => import('./doctorant/create/create.module').then( m => m.CreatePageModule)
  // },
  {
    path: 'att',
    loadChildren: () => import('./hp/attendance/attendance.module').then( m => m.AttendancePageModule)
  },

  {
    path: 'card',
    loadChildren: () => import('./card-teste/card-teste.module').then( m => m.CardTestePageModule)
  },
  {
    path: 'contact',
    loadChildren: () => import('./hp/contact/contact.module').then( m => m.ContactPageModule)
  },
  {
    path: 'contactdoc',
    loadChildren: () => import('./hp/contactdoc/contactdoc.module').then( m => m.ContactdocPageModule)
  },
  // {
  //   path: 'listfornv',
  //   loadChildren: () => import('././page/menu/forN/listfornv/listfornv.module').then( m => m.ListfornvPageModule)
  // },
  // {
  //   path: 'listforcp',
  //   loadChildren: () => import('././page/menu/forN/listforcp/listforcp.module').then( m => m.ListforcpPageModule)
  // }


];




@NgModule({
  imports: [RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })],
  exports: [RouterModule]
})
export class AppRoutingModule { }

