// import { Injectable } from '@angular/core';
// import { Auth, createUserWithEmailAndPassword } from '@angular/fire/auth';
// import { signInWithEmailAndPassword, signOut } from 'firebase/auth';

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {
//   login(value: any) {
//     throw new Error('Method not implemented.');
//   }

//   constructor(private auth:Auth) { }

//   async register({email ,password}){
//    try{
//       const user = await createUserWithEmailAndPassword(
//         this.auth,
//         email,
//         password
//       );
//       return user;
//    }catch(e){
//     return null;
//    }
//    }
//   }
//   async login({email,password}){
//     try{
//       const user = await signInWithEmailAndPassword(
//         this.auth,
//         email,
//         password
//       );
//       return user;
//    }catch(e){
//     return null;
//    }
//    }


//   logout(){
//     return signOut(this.auth);
//   }















//   import { Inject, Injectable } from '@angular/core';
//   import { AngularFireAuth } from '@angular/fire/compat/auth';
//   import {  Auth,createUserWithEmailAndPassword, signInWithEmailAndPassword } from 'firebase/auth';


//   @Injectable({
//     providedIn: 'root'
//   })
//   export class AuthService {

// // private afAuth: AngularFireAut
//     constructor(@Inject(AngularFireAuth) private afAuth: AngularFireAuth) {

//     }

//     async register(credentials: { email: string, password: string }) {
//       try {
//         const userCredential = await createUserWithEmailAndPassword(
//           this.afAuth.auth,
//           credentials.email,
//           credentials.password
//         );
//         return userCredential.user;
//       } catch (error) {
//         console.error('Registration failed:', error);
//         return null;
//       }
//     }

//     async login(credentials: { email: string, password: string }) {
//       try {
//         const userCredential = await signInWithEmailAndPassword(
//           this.afAuth.auth,
//           credentials.email,
//           credentials.password
//         );
//         return userCredential.user;
//       } catch (error) {
//         console.error('Login failed:', error);
//         return null;
//       }
//     }
//   }
