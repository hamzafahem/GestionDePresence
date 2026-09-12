import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.example.mpresence',
  appName: 'mpresence',
  webDir: 'www',
  server: {
    androidScheme: 'https'
  }
};

export default config;
