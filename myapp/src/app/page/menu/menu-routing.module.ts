import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MenuPage } from './menu.page';

// const routes: Routes = [
//   {
//     path: '',
//     component:
//   }
// ];
const routes: Routes = [
  {
  path: 'pages',
  component:MenuPage ,
  children: [
    {
      path: 'profil',
      loadChildren: () => import('../page/page/profil/profil.module').then( m => m.ProfilPageModule)
    },
    {
      path: 'homepage',
      loadChildren: () => import('../page/page/homepage/homepage.module').then( m => m.HomepagePageModule)
    },
    {
      path: 'setting',
      loadChildren: () => import('../page/page/setting/setting.module').then( m => m.SettingPageModule)
    },
    {
      path: 'change-password',
      loadChildren: () => import('../page/page/change-password/change-password.module').then( m => m.ChangePasswordPageModule)
    },
  
             ]
  },
{
  path:'',
  redirectTo:'pages/homepage',
  pathMatch:'full'
},
  {
    path: 'listfornv',
    loadChildren: () => import('./forN/listfornv/listfornv.module').then( m => m.ListfornvPageModule)
  },
  {
    path: 'listforcp',
    loadChildren: () => import('./forN/listforcp/listforcp.module').then( m => m.ListforcpPageModule)
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MenuPageRoutingModule {}
