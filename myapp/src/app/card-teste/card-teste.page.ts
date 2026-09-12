// // import { Component, OnInit } from '@angular/core';
// import { Component, OnInit, AfterContentChecked, NgModule } from '@angular/core';
// import { IonicModule } from '@ionic/angular';
// import SwiperCore, { SwiperOptions, Pagination } from 'swiper';
// // install Swiper modules
// SwiperCore.use([Pagination]);
// @Component({
//   selector: 'app-card-teste',
//   templateUrl: './card-teste.page.html',
//   styleUrls: ['./card-teste.page.scss'],
//    imports: [
//     IonicModule,

//   ],
// })



// // export class  implements OnInit {
// export class CardTestePage implements OnInit, AfterContentChecked {
//     bannerConfig: SwiperOptions | undefined;
//   cards: any[] = [];
//   constructor() { }

//   ngOnInit(){
//   }


//  ngAfterContentChecked() {
// //  this.bannerConfig = {
// // slidesPerView: 1,
// //  centeredSlides: true,
// //  spaceBetween: 40,
// //  pagination: { clickable: true }
// // };
//  }
// }



















// import { Component, OnInit, AfterContentChecked, NgModule } from '@angular/core';
// import { IonicModule } from '@ionic/angular';
// import SwiperCore, { SwiperOptions, Pagination } from 'swiper';
// import { CommonModule } from '@angular/common';

// // Importer les styles Swiper
// import 'swiper/swiper-bundle.css';

// // Installer les modules Swiper
// SwiperCore.use([Pagination]);

// @Component({
//   selector: 'app-card-teste',
//   templateUrl: './card-teste.page.html',
//   styleUrls: ['./card-teste.page.scss'],
// })
// export class CardTestePage implements OnInit, AfterContentChecked {
//   bannerConfig: SwiperOptions | undefined;
//   cards: any[] = [];

//   constructor() { }

//   ngOnInit() {
//     this.cards = [
//       { id: 1, company_img: 'assets/imgs/mastercard.png', card_no: '5786 8945 9098 1100', card_holder: 'Nikhil Ag.', exp_date: '08/24' },
//       { id: 2, company_img: 'assets/imgs/visa.png', card_no: '2006 7091 2014 8766', card_holder: 'Nikhil Ag.', exp_date: '11/29' },
//       { id: 3, company_img: 'assets/imgs/mastercard.png', card_no: '4016 3081 2056 7890', card_holder: 'Nikhil Ag.', exp_date: '06/25' }
//     ];
//   }

//   ngAfterContentChecked() {
//     this.bannerConfig = {
//       slidesPerView: 1,
//       centeredSlides: true,
//       spaceBetween: 40,
//       pagination: { clickable: true }
//     };
//   }
// }

// @NgModule({
//   imports: [
//     IonicModule,
//     CommonModule
//   ],
//   declarations: [CardTestePage]
// })
// export class CardTestePageModule {}




















import { Component, OnInit, AfterContentChecked, NgModule } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import SwiperCore, { SwiperOptions, Pagination } from 'swiper';
import { CommonModule } from '@angular/common';

// Importer les styles Swiper
// import 'swiper/swiper-bundle.css';

// Installer les modules Swiper
// SwiperCore.use([Pagination]);

@Component({
  selector: 'app-card-teste',
  templateUrl: './card-teste.page.html',
  styleUrls: ['./card-teste.page.scss'],
})
export class CardTestePage implements OnInit, AfterContentChecked {
  bannerConfig: SwiperOptions | undefined;
  cards: any[] = [];

  constructor() { }
  @NgModule({
  imports: [
    IonicModule,
    CommonModule
  ],
  declarations: [CardTestePage]
})

  ngOnInit() {
    this.cards = [
      { id: 1, company_img: 'assets/imgs/mastercard.png', card_no: '5786 8945 9098 1100', card_holder: 'Nikhil Ag.', exp_date: '08/24' },
      { id: 2, company_img: 'assets/imgs/visa.png', card_no: '2006 7091 2014 8766', card_holder: 'Nikhil Ag.', exp_date: '11/29' },
      { id: 3, company_img: 'assets/imgs/mastercard.png', card_no: '4016 3081 2056 7890', card_holder: 'Nikhil Ag.', exp_date: '06/25' }
    ];
  }

  ngAfterContentChecked() {
    this.bannerConfig = {
      slidesPerView: 1,
      centeredSlides: true,
      spaceBetween: 40,
      pagination: { clickable: true }
    };
  }



}
