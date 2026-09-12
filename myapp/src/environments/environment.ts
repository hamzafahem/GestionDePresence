// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// export const environment = {
//   firebase: {
//     projectId: 'uhpr-bcb62',
//     appId: '1:934320009723:web:e34940dd57051890209b5e',
//     databaseURL: 'https://uhpr-bcb62-default-rtdb.firebaseio.com',
//     storageBucket: 'uhpr-bcb62.appspot.com',
//     apiKey: 'AIzaSyBl_OavIDF06lv9J_nUopv8l9UEPkTDh5A',
//     authDomain: 'uhpr-bcb62.firebaseapp.com',
//     messagingSenderId: '934320009723',
//     measurementId: 'G-XTS58VTLV1',
//   },
//   production: false,

//   firebaseConfig : {
//     apiKey: "AIzaSyBl_OavIDF06lv9J_nUopv8l9UEPkTDh5A",
//     authDomain: "uhpr-bcb62.firebaseapp.com",
//     projectId: "uhpr-bcb62",
//     storageBucket: "uhpr-bcb62.appspot.com",
//     messagingSenderId: "934320009723",
//     appId: "1:934320009723:web:e34940dd57051890209b5e",
//     measurementId: "G-XTS58VTLV1"
//   },

// };

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.

export const environment = {
  production: false,
  version: '1.0.0',
  api_url: 'http://localhost:3333/api',
  // app_url: "http://127.0.0.1:8000/",
  loading_image_url: '/assets/img/doc_loading.gif',
  profile_image_url: '/assets/img/doc_profile.png',
  inscription_tuto_url: 'https://pedoc.ngcloud.ma/api',
  history_years: 6,
  recaptcha: {
    siteKey: '6LdJOnYiAAAAAK4sVFX8Xy96N9mSWKIUgF6uYG4y',
  },
};
