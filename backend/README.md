# mpresence-backend

Backend Spring Boot pour l'app mobile Ionic/Angular `myapp` (voir `../myapp/API_SPEC.md` pour le contrat API d'origine).

## Lancer en local (profil dev, H2 en mémoire)

```bash
mvn spring-boot:run
```

L'API écoute sur `http://localhost:8080/api`.

Compte de démo créé automatiquement au démarrage : **cin=`admin`, password=`admin123`**.
Formation de démo (id=1) avec un module (id=1, QR code = `DEMO-QR-001`).

Console H2 (pour inspecter les données) : `http://localhost:8080/api/h2-console`
JDBC URL : `jdbc:h2:mem:mpresence`, user `sa`, password vide.

## Endpoints

| Méthode | Path | Auth |
|---|---|---|
| POST | `/api/auth/adminLogin` | non |
| GET | `/api/administration/formations` | Bearer |
| GET | `/api/administration/formations/getOne/{id}` | Bearer |
| GET | `/api/formations/participerModuleByQr/{idf}/{idm}/{qr}` | Bearer |

## Basculer sur MySQL

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Adapter `src/main/resources/application.yml` (section `mysql`) avec vos identifiants. Une base `mpresence` sera créée automatiquement si elle n'existe pas.

## Connecter le front (`myapp`)

Dans `myapp/src/environments/environment.ts`, pointer `api_url` vers `http://localhost:8080/api`.
