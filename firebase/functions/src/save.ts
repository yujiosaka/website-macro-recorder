import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { isString, isNumber, isBoolean, isEmpty, includes, extend } from 'lodash';
import { isUrl, move } from './helper';

const RUNTIME_TIMEOUT_SECONDS = 30;
const RUNTIME_MEMORY = '256MB';

const firestore = admin.firestore();

function validate(macro: Macro) {
  return isString(macro.id) && !isEmpty(macro.id) &&
         isString(macro.name) &&
         isUrl(macro.url) &&
         isUrl(macro.screenshotUrl) &&
         isNumber(macro.scheduleFrequency) && includes([0, 1, 2], macro.scheduleFrequency) &&
         isNumber(macro.scheduleHour) && macro.scheduleHour >= 0 && macro.scheduleHour < 24 &&
         isNumber(macro.scheduleMinute) && macro.scheduleMinute >= 0 && macro.scheduleMinute < 60 &&
         isBoolean(macro.notifySuccess) &&
         isBoolean(macro.notifyFailure) &&
         isBoolean(macro.checkEntirePage) &&
         isBoolean(macro.checkSelectedArea) &&
         isBoolean(macro.isFailure) &&
         isString(macro.userAgent) && !isEmpty(macro.userAgent) &&
         isString(macro.acceptLanguage) && !isEmpty(macro.acceptLanguage) &&
         isNumber(macro.viewportHeight) && macro.viewportHeight >= 1 &&
         isNumber(macro.viewportWidth) && macro.viewportWidth >= 1 &&
         isNumber(macro.deviceScaleFactor) && macro.deviceScaleFactor >= 1;
}

async function moveScreenshot(macro: Macro, context: functions.https.CallableContext) {
  const source = `screenshots/${context.auth!.uid}/tmp.png`;
  const destination = `screenshots/${context.auth!.uid}/${macro.id}.png`;
  try {
    macro.screenshotUrl = await move(source, destination);
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

async function saveMacro(macro: Macro, context: functions.https.CallableContext) {
  const ts = admin.firestore.FieldValue.serverTimestamp();
  try {
    await firestore.collection('macros').doc(macro.id).set(extend({}, macro, {
      uid: context.auth!.uid,
      createdAt: ts,
      updatedAt: ts,
      executedAt: ts,
    }));
    return macro;
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

export const save = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  if (!validate(data)) {
    throw new functions.https.HttpsError(
      'invalid-argument',
      'Argument is invalid',
    );
  }
  await moveScreenshot(data, context);
  return saveMacro(data, context);
});
