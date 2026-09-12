import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { LocalStorageService } from 'src/app/services/local-storage.service';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';
import { UserProfileService } from 'src/app/services/user-profile.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.page.html',
  styleUrls: ['./change-password.page.scss'],
})
export class ChangePasswordPage implements OnInit {

  isDoctorant = false;
  oldPassword = '';
  newPassword = '';
  confirmPassword = '';
  showOld = false;
  showNew = false;
  submitting = false;

  constructor(
    private router: Router,
    private lss: LocalStorageService,
    private doctorantMe: DoctorantMeService,
    private userProfileService: UserProfileService,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.isDoctorant = this.lss.isDoctorant();
  }

  submit() {
    if (!this.oldPassword || !this.newPassword || !this.confirmPassword) {
      this.showToast('Veuillez remplir tous les champs.', 'warning');
      return;
    }
    if (this.newPassword.length < 6) {
      this.showToast('Le nouveau mot de passe doit contenir au moins 6 caractères.', 'warning');
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.showToast('Les mots de passe ne correspondent pas.', 'warning');
      return;
    }

    this.submitting = true;
    const request$ = this.isDoctorant
      ? this.doctorantMe.changePassword(this.oldPassword, this.newPassword)
      : this.userProfileService.changePassword(this.oldPassword, this.newPassword);

    request$.subscribe({
      next: async () => {
        this.submitting = false;
        const toast = await this.toastCtrl.create({ message: 'Mot de passe modifié avec succès.', duration: 2500, color: 'success' });
        toast.present();
        this.router.navigate(['/hamza']);
      },
      error: async (err) => {
        this.submitting = false;
        this.showToast(err.error?.message || 'Ancien mot de passe incorrect.', 'danger');
      }
    });
  }

  private async showToast(message: string, color: string) {
    const toast = await this.toastCtrl.create({ message, duration: 2500, color });
    toast.present();
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
