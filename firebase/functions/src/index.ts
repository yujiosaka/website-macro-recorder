import * as admin from 'firebase-admin';

admin.initializeApp({
  credential: admin.credential.applicationDefault(),
  storageBucket: 'website-macro-recorder.appspot.com',
});

export * from './screenshot';
