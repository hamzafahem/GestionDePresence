
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePage } from './home.page';

// const routes: Routes = [
//   {
//     path: '',
//     component: HomePage,
//   }
// ];

const routes: Routes = [
  {
  path: '',
  component: HomePage ,
  children: [
    {
      path: 'profil',
      loadChildren: () => import('../page/page/page/profil/profil.module').then( m => m.ProfilPageModule)
    },
    {
      path: 'homepage',
      loadChildren: () => import('../page/page/page/homepage/homepage.module').then( m => m.HomepagePageModule)
    },
    {
      path: 'setting',
      loadChildren: () => import('../page/page/page/setting/setting.module').then( m => m.SettingPageModule)
    }
             ]
  },

        //  {
        //         path: '',
        //         redirectTo: 'page/home',
        //         pathMatch: 'full'
        //               },
   {
    path:'**',
    component: HomePage,
     }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomePageRoutingModule {}
