// import { Component, OnInit } from '@angular/core';
// import { FormsModule,FormControl,Validator, FormGroup, Validators } from '@angular/forms';
// import { ToastController,ModalController } from '@ionic/angular';
// import { OtpComponent } from '../page/sign-up/otp/otp.component';
// @Component({
//   selector: 'app-sign-up',
//   templateUrl: './sign-up.page.html',
//   styleUrls: ['./sign-up.page.scss'],
// })
// export class SignUpPage implements OnInit {
//   form !: FormGroup;
//   type: boolean = true ;

//   constructor( private toastCtrl :ToastController ,private modalctrl :ModalController) {
//     this.form = new FormGroup({
//       name: new FormControl('',[Validators.required,Validators.minLength(10),Validators.maxLength(10)]),
//       phone: new FormControl('',[Validators.required,Validators.minLength(10),Validators.maxLength(10)]),
//       email: new FormControl('',[Validators.required,Validators.email]),
//       password: new FormControl('',[Validators.required,Validators.minLength(8)])

//     })
//    }

//   ngOnInit() {
//   }
//   get(){

//   }
//   forgetpassword(){

//   }
//   changetype(){
//     this.type = !this.type

//   }
//   async verify(){
//     const phonenumber = this.form.value.phone;
//     console.log(phonenumber);
//     if(phonenumber && phonenumber == 10){





//       const options: any = {
//         component: OtpComponent
//         }
//         const modal = await this.modalctrl.create( options);
//         await modal.present ();
//         } else {
//         const toast = await this.toastCtrl.create({
//         message: "Please Enter Valid Phone Number",
//         duration: 5000,
//         color: 'danger'
//         });
//         toast.present ();
//         }











//     }
//     else{

//         const toast = await this.toastCtrl.create({
//         message: "Please Enter Valid Phone Number",
//         duration: 5000,
//         color: 'danger'
//         });
//         toast.present ();

//     }

//   }
//   SignUp(){
// if(!this.form.valid){
//   this.form.markAllAsTouched();
//   return;}
// }



// }
// import { Component, OnInit } from '@angular/core';
// import { FormsModule, FormControl, Validator, FormGroup, Validators } from '@angular/forms';
// import { ToastController, ModalController } from '@ionic/angular';
// import { OtpComponent } from './otp/otp.component';

// @Component({
//   selector: 'app-sign-up',
//   templateUrl: './sign-up.page.html',
//   styleUrls: ['./sign-up.page.scss'],
// })
// export class SignUpPage implements OnInit {
//   form!: FormGroup;
//   type: boolean = true;

//   constructor(private toastCtrl: ToastController, private modalctrl: ModalController) {
//     this.form = new FormGroup({
//       name: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
//       phone: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
//       email: new FormControl('', [Validators.required, Validators.email]),
//       password: new FormControl('', [Validators.required, Validators.minLength(8)])
//     })
//   }

//   ngOnInit() {}

//   get() {}

//   forgetpassword() {}

//   changetype() {
//     this.type = !this.type
//   }

//   async verify() {
//     const phonenumber = this.form.value.phone;
//     console.log(phonenumber);
//     if (phonenumber && phonenumber == 10) {
//       const options: any = {
//        component: OtpComponent
//       }
//       const modal = await this.modalctrl.create(options);
//       await modal.present();
//     } else {
//       const toast = await this.toastCtrl.create({
//         message: "Please Enter Valid Phone Number",
//         duration: 5000,
//         color: 'danger'
//       });
//       toast.present();
//     }
//   }

//   async SignUp() {
//     if (!this.form.valid) {
//       this.form.markAllAsTouched();
//       return;
//     }
//   }
// }
import { Component, OnInit } from '@angular/core';
import { FormsModule, UntypedFormControl, Validator, UntypedFormGroup, Validators } from '@angular/forms';
import { ToastController, ModalController } from '@ionic/angular';
import { OtpComponent } from './page/sign-up/otp/otp.component';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.page.html',
  styleUrls: ['./sign-up.page.scss'],
})
export class SignUpPage implements OnInit {
  form!: UntypedFormGroup;
  type: boolean = true;

  constructor(private toastCtrl: ToastController, private modalctrl: ModalController) {
    this.form = new UntypedFormGroup({
      name: new UntypedFormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
      phone: new UntypedFormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
      email: new UntypedFormControl('', [Validators.required, Validators.email]),
      password: new UntypedFormControl('', [Validators.required, Validators.minLength(8)])
    });
  }

  ngOnInit() {}

  async verify() {
    const phonenumber = this.form.value.phone;
    console.log(phonenumber);
    if (phonenumber && phonenumber.length == 10) {
      const options: any = {
        component: OtpComponent
      };
      const modal = await this.modalctrl.create(options);
      await modal.present();
    } else {
      const toast = await this.toastCtrl.create({
        message: "Please Enter Valid Phone Number",
        duration: 5000,
        color: 'danger'
      });
      toast.present();
    }
  }

  SignUp() {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      return;
    }
  }

  changetype() {
    this.type = !this.type;
  }

  get() {}
  forgetpassword(){}


}
