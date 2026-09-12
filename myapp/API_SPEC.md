# API Spec — myapp (mpresence)

Liste exhaustive des appels API réellement actifs dans le code frontend (scan complet de `src/`, tout `HttpClient` confondu). Aucun autre appel réseau actif (pas de `fetch`, pas de Firebase actif, pas d'axios/WebSocket).

Base URL (`environment.api_url`):
- Dev: `http://cedoc-api.ngcloud.ma/api`
- Prod: `https://api-ced.uh1.ac.ma/api`

---

## 1. POST /auth/adminLogin

- **Fichier**: `src/app/services/authtest.service.ts`
- **Auth requise**: non
- **Body**:
```json
{ "cin": "string", "password": "string" }
```
- **Réponse attendue** (utilisée par `AppService.loginUser` / `LocalStorageService.loginUser`):
```json
{
  "access_token": "string",
  "user": {
    "id": "number",
    "nom_fr": "string",
    "prenom_fr": "string",
    "email": "string",
    "photo": "string",
    "roles": [ "..." ],
    "permissions": [ "..." ]
  },
  "annee_uni": { "id": "number" }
}
```
- **Note**: le formulaire appelle les champs `Username`/`Password` côté UI mais les envoie comme `cin`/`password`.

---

## 2. GET /administration/formations

- **Fichier**: `src/app/services/qr.service.ts` → `getFts()`
- **Auth requise**: oui — header `Authorization: Bearer {token}`
- **Utilisé dans**: `menu.page.ts`, `listfornv.page.ts`, `module-c.page.ts` (toutes lisent `res.items`)
- **Réponse attendue**:
```json
{
  "items": [
    { "intitule": "string", "...": "autres champs pas encore consommés côté UI" }
  ]
}
```

---

## 3. GET /administration/formations/getOne/{id}

- **Fichier**: `src/app/services/qr.service.ts` → `getOne(id)`
- **Auth requise**: oui — `Authorization: Bearer {token}`
- **Statut**: définie mais **jamais appelée** dans l'UI actuelle (dead code, à garder pour compat future).

---

## 4. GET /formations/participerModuleByQr/{idf}/{idm}/{qr}

- **Fichier**: `src/app/services/qr.service.ts` → `setQr(idf, idm, qr)`
- **Auth requise**: non (pas de header envoyé actuellement — probablement un oubli)
- **⚠️ Bug connu**: l'URL générée contient un espace en trop avant `/formations` (`` `${api_url} /formations/...` ``), ce qui casse l'appel tel quel. À corriger côté front si vous branchez ce endpoint.

---

## 5. GET /formations/participerModuleByQr/1/3/{id}

- **Fichier**: `src/app/services/qr.service.ts` → `getqr(id)`
- **Auth requise**: oui — `Authorization: Bearer {token}`
- **Note**: `idf` et `idm` sont figés en dur à `1` et `3` côté front (jamais paramétrés dynamiquement).
- C'est la même route que #4, juste appelée avec des valeurs fixes.

---

## Hors périmètre (à ignorer)

- `qrcode.page.ts` → `getResponseFromServer()` appelle `https://www.google.com` — code de test, pas une vraie API métier.
- `@angular/fire` (Firestore/Auth/Database) est importé par endroits mais **tout le code Firebase est commenté** — aucune requête Firebase n'est active.
- `HttpInterceptorService` existe (`src/app/services/http-interceptor.service.ts`, ajoute le Bearer token automatiquement) mais **n'est pas enregistré** dans `app.module.ts` (`HTTP_INTERCEPTORS`) — les endpoints qui ont besoin du token le font manuellement via `getHeaders()`.

---

## Résumé pour le backend Spring Boot

| Méthode | Path | Auth | Payload |
|---|---|---|---|
| POST | `/auth/adminLogin` | non | `{cin, password}` → `{access_token, user, annee_uni}` |
| GET | `/administration/formations` | Bearer | → `{items: Formation[]}` |
| GET | `/administration/formations/getOne/{id}` | Bearer | → `Formation` |
| GET | `/formations/participerModuleByQr/{idf}/{idm}/{qr}` | Bearer (recommandé) | → à définir |

Entités suggérées: `User` (avec `roles`/`permissions`), `AnneeUniversitaire`, `Formation` (au minimum le champ `intitule`), et une relation Formation ↔ Module ↔ Participation pour la route QR.
