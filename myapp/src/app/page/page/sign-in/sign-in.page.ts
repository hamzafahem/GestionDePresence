// import { Component, OnInit } from '@angular/core';
// import { FormControl } from '@angular/forms';
// import { FormsModule,UntypedFormControl,Validator, UntypedFormGroup, Validators, FormGroup, FormBuilder } from '@angular/forms';
// import { AlertController, LoadingController } from '@ionic/angular';
// import { AuthtestService } from 'src/app/services/authtest.service';
// import { Router } from '@angular/router';
// @Component({
//   selector: 'app-sign-in',
//   templateUrl: './sign-in.page.html',
//   styleUrls: ['./sign-in.page.scss'],
// })
// export class SignInPage implements OnInit {
//   //   toFormData(formValue: any , setNull = false) {
//   //   const formData = new FormData();

//   //   for (const key of Object.keys(formValue)) {
//   //     const value = formValue[key];
//   //     if (value && value.label) {
//   //       if (value.value) {
//   //         formData.append(key, value.value);
//   //       } else {
//   //         if (setNull){
//   //           formData.append(key, "null");
//   //         }

//   //       }

//   //     } else if (value &&  Array.isArray(value)) {

//   //         value.forEach(e => formData.append(key + '[]', e.value ? e.value : e.id));
//   //     }else if (value) {
//   //       formData.append(key, value);
//   //     }
//   //   }

//   //   return formData;
//   // }

//   form !: UntypedFormGroup;
//   type: boolean = true ;
//   constructor( private auth:AuthtestService,
//     private router : Router) {
//   }

//   ngOnInit() {
//     this.form = new UntypedFormGroup({
//       Username : new UntypedFormControl('',[Validators.required,Validators.minLength(10),Validators.maxLength(10)]),
//       Password: new UntypedFormControl('',[Validators.required,Validators.minLength(8)])

//     })
//   }
//   // setAuth(){
//   //      this.auth.auth(this.toFormData(this.form)).subscribe(res=>{
//   //       console.log(res);
//   //       this.router.navigateByUrl('/menu');
//   //      })
//   // }
// //    setAuth(){
// // this.auth.auth(this.form.value.Username , this.form.value.Password).subscribe(res=>{
// //   console.log(res);
// //        this.router.navigateByUrl('/menu');
// // }

// // )
// //   }
// // setAuth() {
// //   const username = this.form.get('username')?.value;
// //   const Password = this.form.get('Password')?.value;

// //   this.auth.auth(username,Password).subscribe(res => {
// //     console.log(res);
// //     this.router.navigateByUrl('/menu');
// //   });
// // }
// setAuth() {
//   const username = this.form.get('username')?.value;
//   const Password = this.form.get('Password')?.value;

//   const credentials = {
//     username: username,
//     Password: Password
//   };

//   this.auth.auth(credentials).subscribe(res => {
//     console.log(res);
//     this.router.navigateByUrl('/menu');
//   });
// }


//   forgetpassword(): void{

//   }
//   changetype(){
//     this.type = !this.type

//   }
//   verify(){

//   }
// }
// } hada ahmar khedam li daret m3aya assia o ana mdareb nkhadmo















// @Component({
//   selector: 'app-sign-in',
//   templateUrl: './sign-in.page.html',
//   styleUrls: ['./sign-in.page.scss'],
// })
// export class SignInPage implements OnInit {
//   credentials :FormGroup;

//   constructor(
//     private fb:FormBuilder,
//     private loadingContrller: LoadingController,
//     private alertController: AlertController,
//     private authservice: AuthService,
//     private router: Route
//   ){}

// get email(){
//   return this.credentials.get('email');
// }
//   get password(){
//  return this.credentials.get('password');
//   }

//   ngOnInit() {
//     this.credentials = this.fb.group({
//       email:['',[Validators.required,Validators,this.email]],
//       password:['',[Validators.required,Validators.minLength(10),Validators.maxLength(10)]],
//     });
//   }

// async register(){
//   const loading = await this.loadingContrller.create();
//   await loading.present();

//   const user = await this.authservice.register(this.credentials.value);
//   await loading.dismiss();
//   if(user){
//     this.router.navigateByUrl('/home',{replaceURL:true});
//   }else{
//     this.showALlert('registration failed','please try again');
//   }
// }



// async login(){
//   const loading = await this.loadingContrller.create();
//   await loading.present();

//   const user = await this.authservice.login(this.credentials.value);
//   await loading.dismiss();
//   if(user){
//     this.router.navigateByUrl('/home',{replaceURL:true});
//   }else{
//     this.showALlert('login failed','please try again');
//   }

// }
// async showAlert(header,message){
//   const alert = await this.alertController.create({
//     header,
//     message,
//     buttons:['Ok'],
//   })
// }





// }


















// @Component({
//   selector: 'app-sign-in',
//   templateUrl: './sign-in.page.html',
//   styleUrls: ['./sign-in.page.scss'],
// })
// export class SignInPage implements OnInit {
//   credentials!: FormGroup;

//   constructor(
//     private fb: FormBuilder,
//     private loadingController: LoadingController,
//     private alertController: AlertController,
//     private authService: AuthService,
//     public router: Router,
//     // private afAuth: AngularFireAuth
//   ) {}

//   get email() {
//     return this.credentials.get('email');
//   }

//   get password() {
//     return this.credentials.get('password');
//   }

//   ngOnInit() {
//     this.credentials = this.fb.group({
//       email: ['', [Validators.required, Validators.email]],
//       password: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]],
//     });
//   }

//   async register() {
//     const loading = await this.loadingController.create();
//     await loading.present();

//     const user = await this.authService.register(this.credentials.value);
//     await loading.dismiss();

//     if (user) {
//       this.router.navigateByUrl('/home', { replaceUrl: true });
//     } else {
//       this.showAlert('Registration Failed', 'Please try again');
//     }
//   }

//   async login() {
//     const loading = await this.loadingController.create();
//     await loading.present();
//     // const user: string = await this.authService.login(this.email, this.password);
//     const user = await this.authService.login(this.credentials.value);
//     await loading.dismiss();

//     if (user) {

//       this.router.navigateByUrl('/home', { replaceUrl: true });
//     } else {
//       this.showAlert('Login Failed', 'Please try again');
//     }
//   }

//   async showAlert(header: string, message: string) {
//     const alert = await this.alertController.create({
//       header,
//       message,
//       buttons: ['OK'],
//     });

//     await alert.present();
//   }
// }













































// import { Component, OnInit } from '@angular/core';
// import { FormControl, FormGroup, Validators } from '@angular/forms';
// import { AuthtestService } from 'src/app/services/authtest.service';
// import { Router } from '@angular/router';

// function serializeFormToJson(formGroup: FormGroup): any {
//   const serialized :any= {};

//   for (const controlName in formGroup.controls) {
//     if (formGroup.controls.hasOwnProperty(controlName)) {
//       const control = formGroup.controls[controlName];
//       if (control instanceof FormGroup) {
//         serialized[controlName] = serializeFormToJson(control);
//       } else {
//         serialized[controlName] = control.value;
//       }
//     }
//   }

//   return serialized;
// }

// @Component({
//   selector: 'app-sign-in',
//   templateUrl: './sign-in.page.html',
//   styleUrls: ['./sign-in.page.scss'],
// })
// export class SignInPage implements OnInit {
//   form!: FormGroup;
//   type: boolean = true;

//   constructor(private auth: AuthtestService, private router: Router) {}

//   ngOnInit() {
//     this.form = new FormGroup({
//       Identifiant: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10)]),
//       Motdepasse: new FormControl('', [Validators.required, Validators.minLength(8)])
//     });
//   }

//   setAuth() {
//     const formJson = serializeFormToJson(this.form);
//     const jsonString = JSON.stringify(formJson);
//     console.log(jsonString);

//     this.auth.auth(this.form.value).subscribe(res => {
//       console.log(res);
//       this.router.navigateByUrl('/menu');
//     });
//   }

//   forgetpassword() {}

//   changetype() {
//     this.type = !this.type;
//   }

//   verify() {}
// }
























import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { AuthtestService } from 'src/app/services/authtest.service';
import { Router } from '@angular/router';
import { AppService } from 'src/app/services/app.service';
import { ToastController } from '@ionic/angular';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.page.html',
  styleUrls: ['./sign-in.page.scss'],
})
export class SignInPage implements OnInit {
  form!: UntypedFormGroup;
  type: boolean = true;

  constructor(
    private auth: AuthtestService,
    private router: Router,
    private app: AppService,
    private toastCtrl: ToastController
  ) {}

  ngOnInit() {
    this.form = new UntypedFormGroup({
      Username: new UntypedFormControl('', [Validators.required]),
      Password: new UntypedFormControl('', [Validators.required])
    });
  }

  setAuth() {
    if (!this.form.value.Username || !this.form.value.Password) {
      this.showError('Veuillez saisir votre identifiant et votre mot de passe.');
      return;
    }

    this.auth.auth(this.form.value.Username, this.form.value.Password).subscribe({
      next: (res) => {
        this.app.loginUser(res);
        this.router.navigateByUrl('/hamza');
      },
      error: (err) => {
        const message = err?.error?.message || 'Identifiant ou mot de passe incorrect.';
        this.showError(message);
      }
    });
  }

  private async showError(message: string) {
    const toast = await this.toastCtrl.create({ message, duration: 3000, color: 'danger' });
    toast.present();
  }

  forgetpassword(): void {
    // Logique pour la récupération du mot de passe oublié
  }

  changetype() {
    this.type = !this.type;
  }

  verify() {
    // Logique de vérification
  }
}
