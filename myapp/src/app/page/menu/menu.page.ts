import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'src/app/services/local-storage.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.page.html',
  styleUrls: ['./menu.page.scss'],
})
export class MenuPage implements OnInit {

  photo: string | null = null;

  constructor(private lss: LocalStorageService) { }

  ngOnInit() {
    this.photo = this.lss.getPhoto();
  }
}
