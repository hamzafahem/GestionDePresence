import { Injectable } from '@angular/core';

/**
 * Porte l'id/intitulé de la formation cliquée dans listfornv/listforcp
 * jusqu'à la page module-c/module-n (pas de paramètre de route pour l'instant).
 */
@Injectable({
  providedIn: 'root'
})
export class SelectedFormationService {
  id: number | null = null;
  intitule: string | null = null;
}
