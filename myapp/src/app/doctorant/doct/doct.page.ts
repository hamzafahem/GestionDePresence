
// import { Component, OnInit } from '@angular/core';
// import { AdminService } from '../../shared/admin.service';
// import { pers } from '../../models/personne';

// @Component({
//   selector: 'app-doct',
//   templateUrl: './doct.page.html',
//   styleUrls: ['./doct.page.scss'],
// })
// export class DoctPage implements OnInit {
// doctorants= [];
//   constructor(
//     private adminservice: AdminService
//     ) { }

//   ngOnInit() {
//     let docres = this.adminservice.getdoclist()
//     docres.snapshotChange().subscribe({res=>
//     this.doctorants=[]
//     res.forEach(item=>{
//       let a = item.payload.toJSON()
//       a['$key'] = item.$key
//       this.doctorants.push(a as  pers)
//     })
//     })
//   }
//   fetchdoc()
// {
//   this.adminservice.getdoclist().valuechange().subscribe(res=>{
//     console.log(res)
//   })
// }
// deletedoc(id){
//   this.adminservice.getdoclist().valuechange().subscribe(res=>{
//     console.log(res)
//     if(window.confirm('ARE YOU SURE ?' )) this.AdminService.deletedoc(id)(res=>{
//       console.log(res)
//     })
//   })
// }
// }
// import { Component, OnInit } from '@angular/core';
// import { AdminService } from '../../shared/admin.service';
// import { pers } from '../../models/personne';

// @Component({
//   selector: 'app-doct',
//   templateUrl: './doct.page.html',
//   styleUrls: ['./doct.page.scss'],
// })
// export class DoctPage implements OnInit {
//   doctorants = [];

//   constructor(private adminservice: AdminService) {}

//   ngOnInit() {/*
//     let docres = this.adminservice.getperslist();
//     docres.snapshotChanges().subscribe((res: any[]) => {
//       this.doctorants = [];
//       res.forEach((item) => {
//         let a = item.payload.toJSON();
//         a['$key'] = item.key;
//         this.doctorants.push(a)
//       });
//     });*/
//   }

//   fetchdoc() {
//     this.adminservice.getperslist().valueChanges().subscribe((res: any) => {
//       console.log(res);
//     });
//   }

//   deletedoc(ID:string) {
//     console.log(ID)
//       if (window.confirm('ARE YOU SURE?')) {
//         this.adminservice.deletepers(ID)
//       }
//     }
//   }

