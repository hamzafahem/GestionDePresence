import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { UserProfileService } from 'src/app/services/user-profile.service';
import { DoctorantMeService } from 'src/app/services/doctorant-me.service';
import { LocalStorageService } from 'src/app/services/local-storage.service';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.page.html',
  styleUrls: ['./profil.page.scss'],
})
export class ProfilPage implements OnInit {

  profile: any = {};
  loading = true;
  isDoctorant = false;
  uploadingPhoto = false;

  constructor(
    private router: Router,
    private userProfileService: UserProfileService,
    private doctorantMeService: DoctorantMeService,
    private lss: LocalStorageService,
    private toastCtrl: ToastController
  ) { }

  ngOnInit() {
    this.isDoctorant = this.lss.isDoctorant();
    const source = this.isDoctorant ? this.doctorantMeService.getProfile() : this.userProfileService.getProfile();

    source.subscribe({
      next: (res) => {
        this.profile = res;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  async save() {
    if (this.isDoctorant) {
      // Auto-service en lecture seule pour l'instant : le doctorant ne peut
      // pas encore modifier sa fiche depuis l'app (géré par l'admin).
      return;
    }

    this.userProfileService.updateProfile({
      nom_fr: this.profile.nom_fr,
      prenom_fr: this.profile.prenom_fr,
      email: this.profile.email,
      age: this.profile.age,
      sexe: this.profile.sexe,
      address: this.profile.address,
      phone: this.profile.phone,
    }).subscribe({
      next: async (res) => {
        this.profile = res;
        const toast = await this.toastCtrl.create({ message: 'Profil mis à jour.', duration: 2000, color: 'success' });
        toast.present();
      },
      error: async () => {
        const toast = await this.toastCtrl.create({ message: 'Échec de la mise à jour.', duration: 2000, color: 'danger' });
        toast.present();
      }
    });
  }

  onPhotoSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files[0];
    if (!file) {
      return;
    }

    this.uploadingPhoto = true;
    this.userProfileService.uploadPhoto(file).subscribe({
      next: async (res) => {
        this.profile = res;
        this.uploadingPhoto = false;
        input.value = '';
        const toast = await this.toastCtrl.create({ message: 'Photo de profil mise à jour.', duration: 2000, color: 'success' });
        toast.present();
      },
      error: async () => {
        this.uploadingPhoto = false;
        input.value = '';
        const toast = await this.toastCtrl.create({ message: 'Échec de l\'envoi de la photo.', duration: 2000, color: 'danger' });
        toast.present();
      }
    });
  }

  deletePhoto() {
    this.uploadingPhoto = true;
    this.userProfileService.deletePhoto().subscribe({
      next: async (res) => {
        this.profile = res;
        this.uploadingPhoto = false;
        const toast = await this.toastCtrl.create({ message: 'Photo de profil supprimée.', duration: 2000, color: 'success' });
        toast.present();
      },
      error: async () => {
        this.uploadingPhoto = false;
        const toast = await this.toastCtrl.create({ message: 'Échec de la suppression.', duration: 2000, color: 'danger' });
        toast.present();
      }
    });
  }

  closemodal() {
    this.router.navigate(['/hamza']);
  }
}
