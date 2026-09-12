
// // export default {
// //   production: true,
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
//   production: true,
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
// // zedt par defaut
export const environment = {
  production: true,
  version: '1.2.0',
  // api_url: 'https://pedoc.ngcloud.ma/api',
  // api_url: 'https://api-ced.uh1.ac.ma/api',
  // api_url: 'http://192.168.11.171:3333/api', // LAN WiFi (needs firewall rule)
  api_url: 'http://localhost:3333/api', // USB via `adb reverse tcp:3333 tcp:3333`
  loading_image_url: '/assets/img/doc_loading.gif',
  profile_image_url: '/assets/img/doc_profile.png',

  inscription_tuto_url: 'http://ced.uh1.ac.ma',
  history_years: 6,
  recaptcha: {
    siteKey: '6LdJOnYiAAAAAK4sVFX8Xy96N9mSWKIUgF6uYG4y',
  },

};
