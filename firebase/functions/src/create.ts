import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { isString, isNumber, isBoolean, isEmpty, isArray, includes, extend } from 'lodash';
import { isUrl, move } from './lib/helper';

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
         isBoolean(macro.isEntirePageUpdated) &&
         isBoolean(macro.isSelectedAreaUpdated) &&
         isBoolean(macro.isFailure) &&
         isString(macro.userAgent) && !isEmpty(macro.userAgent) &&
         isString(macro.acceptLanguage) && !isEmpty(macro.acceptLanguage) &&
         isNumber(macro.viewportHeight) && macro.viewportHeight >= 1 &&
         isNumber(macro.viewportWidth) && macro.viewportWidth >= 1 &&
         isNumber(macro.deviceScaleFactor) && macro.deviceScaleFactor >= 1 &&
         isArray(macro.events) &&
         isArray(macro.histories);
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

async function createMacro(macro: Macro, context: functions.https.CallableContext) {
  const now = admin.firestore.Timestamp.now();
  try {
    await firestore.collection('macros').doc(macro.id).set(extend({}, macro, {
      uid: context.auth!.uid,
      createdAt: now,
      updatedAt: now,
      executedAt: now,
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

export const create = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (macro: Macro, context: functions.https.CallableContext) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  if (!validate(macro)) {
    throw new functions.https.HttpsError(
      'invalid-argument',
      'Argument is invalid',
    );
  }
  await moveScreenshot(macro, context);
  return createMacro(macro, context);
});
