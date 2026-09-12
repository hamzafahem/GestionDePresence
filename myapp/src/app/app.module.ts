
// import { NgModule } from '@angular/core';
// import { BrowserModule } from '@angular/platform-browser';
// import { RouteReuseStrategy } from '@angular/router';
// import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
// import { AppComponent } from './app.component';
// import { AppRoutingModule } from './app-routing.module';
// import {environment } from '../environments/environment';


// import { AngularFireModule } from "@angular/fire/compat";
// import { AngularFireAuthModule } from "@angular/fire/compat/auth";
// import { AngularFireStorageModule } from '@angular/fire/compat/storage';
// import { AngularFirestoreModule } from '@angular/fire/compat/firestore';
// import { AngularFireDatabaseModule } from '@angular/fire/compat/database';


// export const firebaseConfig = {
//   apiKey: "AIzaSyBl_OavIDF06lv9J_nUopv8l9UEPkTDh5A",
//   authDomain: "uhpr-bcb62.firebaseapp.com",
//   projectId: "uhpr-bcb62",
//   storageBucket: "uhpr-bcb62.appspot.com",
//   messagingSenderId: "934320009723",
//   appId: "1:934320009723:web:e34940dd57051890209b5e",
//   measurementId: "G-XTS58VTLV1"
// };
// @NgModule({
//   declarations: [AppComponent],
//   imports: [

//     AppRoutingModule,AngularFireModule.initializeApp(environment.firebaseConfig),AngularFireAuthModule,AngularFireDatabaseModule,AngularFireStorageModule,
//     BrowserModule, IonicModule.forRoot(), AngularFirestoreModule
//   ],
//   providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy }],
//   bootstrap: [AppComponent],
// })
// export class AppModule {}





















// import { NgModule, isDevMode } from '@angular/core';
// import { BrowserModule } from '@angular/platform-browser';
// import { RouteReuseStrategy } from '@angular/router';
// import { IonicModule } from '@ionic/angular';
// import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
// import { AngularFireList, AngularFireObject, AngularFireDatabase } from '@Angular/fire/database';
// import { AppComponent } from './app.component';
// import { AppRoutingModule } from './app-routing.module';
// import { pers } from '../app/models/personne';
// import { AngularFireModule } from '@angular/fire';
// import { AngularFireAuthModule } from '@angular/fire/auth';
// import { AngularFireDatabaseModule } from '@angular/fire/database';
// import { AngularFireStorageModule } from '@angular/fire/storage';
// import { environment } from '../environments/environment';
// import { ServiceWorkerModule } from '@angular/service-worker';

// import { environment } from '../environments/environment';

// import { AngularFireModule } from '@angular/fire';
// import { AngularFireAuthModule } from '@angular/fire/auth';
// import { AngularFireDatabaseModule } from '@angular/fire/database';
// import { AngularFireStorageModule } from '@angular/fire/storage';

// @NgModule({
//   declarations: [AppComponent],
  // imports: [
  //   BrowserModule, AngularFireModule.initializeApp(environment.firebase),
  //   IonicModule.forRoot , AngularFireList, AngularFireObject, AngularFireDatabase,
  //   AngularFireModule.initializeApp(environment.firebaseConfig),
  //   AngularFireAuthModule,
  //   AngularFireDatabaseModule,
  //   AngularFireStorageModule(),  IonicModule.forRoot(),
  //   AppRoutingModule,
  //   ServiceWorkerModule.register('ngsw-worker.js', {
  //     enabled: !isDevMode(),
  //     // Register the ServiceWorker as soon as the application is stable
  //     // or after 30 seconds (whichever comes first).
  //     registrationStrategy: 'registerWhenStable:30000'
  //   }),

  // ],



  // @NgModule({
//   declarations: [AppComponent],
//   imports: [
//     IonicModule.forRoot(),
//     // AngularFireModule,AngularFireAuthModule,AngularFireDatabaseModule,AngularFireStorageModule,
//     // BrowserModule, IonicModule.forRoot(), AppRoutingModule
//   ],
//   providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy }],
//   bootstrap: [AppComponent],
// })
// export class AppModule {}

// HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

//   providers: [
//     { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
//   ],
//   bootstrap: [AppComponent]
// })
// export class AppModule {}




// import { NgModule } from '@angular/core';
// import { BrowserModule } from '@angular/platform-browser';
// import { IonicModule } from '@ionic/angular';

// import { AppComponent } from './app.component';


// import { RouteReuseStrategy } from '@angular/router';
// @NgModule({
//   declarations: [AppComponent],
//   imports: [BrowserModule, RouteReuseStrategy, IonicModule.forRoot()],
//   bootstrap: [AppComponent]
// })
// export class AppModule {}



























// hada li  khdem lia

// import { NgModule } from '@angular/core';
// import { BrowserModule } from '@angular/platform-browser';
// import { RouteReuseStrategy } from '@angular/router';
// import { IonicModule, IonicRouteStrategy } from '@ionic/angular';

// import { environment } from '../environments/environment';
// import { ServiceWorkerModule } from '@angular/service-worker';

// import { AppComponent } from './app.component';
// import { AppRoutingModule } from './app-routing.module';;
// import { AngularFireAuthModule } from "@angular/fire/compat/auth";
// import { AngularFireStorageModule } from '@angular/fire/compat/storage';
// import { AngularFirestoreModule } from '@angular/fire/compat/firestore';
// import { AngularFireDatabaseModule } from '@angular/fire/compat/database';
// // zedtha
// export const firebaseConfig : {
//   apiKey: "AIzaSyBl_OavIDF06lv9J_nUopv8l9UEPkTDh5A",
//   authDomain: "uhpr-bcb62.firebaseapp.com",
//   projectId: "uhpr-bcb62",
//   storageBucket: "uhpr-bcb62.appspot.com",
//   messagingSenderId: "934320009723",
//   appId: "1:934320009723:web:e34940dd57051890209b5e",
//   measurementId: "G-XTS58VTLV1"
// };
// @NgModule({
//   declarations: [AppComponent],
//   imports: [
//     BrowserModule,
//     IonicModule.forRoot(),
//     AppRoutingModule,
//     AngularFireModule.initializeApp(environment.firebaseConfig),
//     AngularFireAuthModule,
//     AngularFireDatabaseModule,
//     AngularFireStorageModule,
//     AppRoutingModule,AngularFireModule.initializeApp(environment.firebaseConfig),AngularFireAuthModule,AngularFireDatabaseModule,AngularFireStorageModule,
//       BrowserModule, IonicModule.forRoot(), AngularFirestoreModule,
//     ServiceWorkerModule.register('ngsw-worker.js', {
//       enabled: environment.production,
//       registrationStrategy: 'registerWhenStable:30000'
//     })
//   ],
//   providers: [
//     { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
//   ],
//   bootstrap: [AppComponent]
// })
// export class AppModule {}






import { HttpClientModule } from '@angular/common/http';
// hada jaded li bghina nchofo
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
// import { RouteReuseStrategy } from '@angular/router';
import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
// import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CodePComponent } from "./hp/qrcode/code-p/CodePComponent";
import { FormsModule } from '@angular/forms';
import { RouteReuseStrategy } from '@angular/router';
// import { BrowserModule } from '@angular/platform-browser';
// import { SwiperModule } from 'swiper';
@NgModule({
  declarations: [AppComponent],
  imports: [
//  CodePComponent
HttpClientModule,
    BrowserModule,
    IonicModule.forRoot(),
    AppRoutingModule,FormsModule, IonicModule.forRoot({})
    // provideFirebaseApp(() => initializeApp(environment.firebase)),
    // provideAnalytics(() => getAnalytics()),
    // provideAuth(() => getAuth()),
    // provideDatabase(() => getDatabase()),
    // provideFirestore(() => getFirestore()),
    // provideFunctions(() => getFunctions()),
    // provideMessaging(() => getMessaging()),
    // providePerformance(() => getPerformance()),
    // provideRemoteConfig(() => getRemoteConfig()),
    // provideStorage(() => getStorage()),

  ],
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    // ScreenTrackingService,UserTrackingService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
