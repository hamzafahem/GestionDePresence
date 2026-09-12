import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';
import { UserProfileService } from 'src/app/services/user-profile.service';

@Component({
  selector: 'app-setting',
  templateUrl: './setting.page.html',
  styleUrls: ['./setting.page.scss'],
})
export class SettingPage implements OnInit {

  isDoctorant = false;
  loading = true;

  smsEmailNotification = true;
  appNotification = true;
  securityEnabled = false;
  accountDeactivated = false;

  constructor(
    private router: Router,
    private lss: LocalStorageService,
    private doctorantMe: DoctorantMeService,
    private userProfileService: UserProfileService,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.isDoctorant = this.lss.isDoctorant();
    const source$ = this.isDoctorant ? this.doctorantMe.getSettings() : this.userProfileService.getSettings();

    source$.subscribe({
      next: (res: any) => {
        this.smsEmailNotification = res.sms_email_notification;
        this.appNotification = res.app_notification;
        this.securityEnabled = res.security_enabled;
        this.accountDeactivated = res.account_deactivated;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  save() {
    const payload = {
      sms_email_notification: this.smsEmailNotification,
      app_notification: this.appNotification,
      security_enabled: this.securityEnabled,
      account_deactivated: this.accountDeactivated
    };

    const request$ = this.isDoctorant
      ? this.doctorantMe.updateSettings(payload)
      : this.userProfileService.updateSettings(payload);

    request$.subscribe({
      next: async () => {
        const toast = await this.toastCtrl.create({ message: 'Paramètres enregistrés.', duration: 2000, color: 'success' });
        toast.present();
      },
      error: async () => {
        const toast = await this.toastCtrl.create({ message: 'Échec de l\'enregistrement.', duration: 2500, color: 'danger' });
        toast.present();
      }
    });
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
