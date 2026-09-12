// import { Component, OnInit } from '@angular/core';
// import { ModalController } from '@ionic/angular';


// @Component({
//   selector: 'app-code-p',
//   templateUrl: './code-p.component.html',
//   styleUrls: ['./code-p.component.scss'],
// })
// export class CodePComponent implements OnInit {



//   ngOnInit() { }
//   name: string | undefined;

//   constructor(private modalCtrl: ModalController) { }

//   cancel() {
//     return this.modalCtrl.dismiss(null, 'cancel');
//   }

//   confirm() {
//     return this.modalCtrl.dismiss(this.name, 'confirm');
//   }

// }



import { Component, OnInit } from '@angular/core';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'app-code-p',
  templateUrl: './code-p.component.html',
  styleUrls: ['./code-p.component.scss'],
})
export class CodePComponent implements OnInit {
  name!: string;

  constructor(private modalCtrl: ModalController) {}

  ngOnInit() {}

  cancel() {
    this.modalCtrl.dismiss(null, 'cancel');
  }

  confirm() {
    this.modalCtrl.dismiss(this.name, 'confirm');
  }
}
