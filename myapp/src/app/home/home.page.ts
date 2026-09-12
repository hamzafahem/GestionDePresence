import { Component } from '@angular/core';
@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  pages=[
    {
      name:"Home",
      icon:"home",
      url:"/menu/pages/home"
    },
    {
      name:"profil",
      icon:"person-circle",
      url:"/app/page/page/page/profil"
    },
    {
      name:"change password",
      icon:"pencil",
      url:"/menu/pages/home"
    },
    {
      name:"setting",
      icon:"information-circle",
      url:"/app/page/page/page/setting"
    },
    {
      name:"log out ",
      icon:"log-out",
      url:"/menu/pages/home"
    }
  ]
  constructor() {}
  onClick(){

  }

}
