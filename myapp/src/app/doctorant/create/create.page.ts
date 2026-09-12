

// // hadchi li kint ayr mnin bdat katgad de3wa dekhelt lcode l chat gadatto o derto te7t
// // import { Component, OnInit } from '@angular/core';
// // import { FormBuilder, FormGroup } from '@angular/forms';
// // import {  Router } from '@angular/router';
// // import { AdminService } from '../../shared/admin.service';


// // @Component({
// //   selector: 'app-create',
// //   templateUrl: './create.page.html',
// //   styleUrls: ['./create.page.scss'],
// // })
// // export class CreatePage implements OnInit {
// //   docform : FormGroup;
// //   constructor(
// //     private adminservice: AdminService,
// //     private router:Router,
// //    private fb :FormBuilder
// //     ) { }

// //   ngOnInit() {
// //     this. docform = this.fb.group({
// //       $key: [''],
// //       name: [''],
// //       email:[''],
// //       mobile:[''],
// //       details:[''],
// //     })
// //   }
// //   formsubmit(){
// //     if(!this.docform.valid){
// //       return false;
// //     }else{
// //      this.adminservice.createpers(this.docform.value).then((res: any)=>{
// //       console.log(res)
// //       this.docform.reset();
// //       this.router.navigate(['/الصفحة الرىيسية'])
// //      }).catch( (error: any) => console.log(error))
// //     }
// //   }

// // }
// // ZEDT GODAM ERROR :ANY HAYEDH AILA WçE" CHI PROBLE














// // db khdena ghire version corriger men chat gbt ms ghanjerbooha
// import { Component, OnInit } from '@angular/core';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { Router } from '@angular/router';
// import { AdminService } from '../../shared/admin.service';

// @Component({
//   selector: 'app-create',
//   templateUrl: './create.page.html',
//   styleUrls: ['./create.page.scss'],
// })

// export class CreatePage implements OnInit {

// // docform : FormGroup ='';
// docform : FormGroup = new FormGroup({})
//   constructor(
//     private adminservice: AdminService,
//     private router: Router,
//     private fb: FormBuilder,
//   ) {}

//   ngOnInit() {
//     this.docform = this.fb.group({
//       $key: [''],
//       name: ['', Validators.required],
//       email: ['', [Validators.required, Validators.email]],
//       mobile: ['', Validators.required],
//       details: ['', Validators.required],
//     });
//   }

//   // formsubmit(){
//   //   if (!this.docform.valid) {
//   //     return false;
//   //   } else {
//   //     this.adminservice.createpers(this.docform.value)
//   //       .then((res: any) => {
//   //         console.log(res);
//   //         this.docform.reset();
//   //         this.router.navigate(['/الصفحة الرىيسية']);
//   //       }).catch((error: any) => console.log(error));
//   //   }
//   // }
//   formsubmit(): boolean {
//     if (!this.docform.valid) {
//       return false;
//     } else {
//       this.adminservice.createpers(this.docform.value)
//         .then((res: any) => {
//           console.log(res);
//           this.docform.reset();
//           this.router.navigate(['/الصفحة الرىيسية']);
//         })
//         .catch((error: any) => console.log(error));
//       return true;
//     }
//   }


// }

