import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { isString, isNumber, isBoolean, isEmpty, includes } from 'lodash';
import { isUrl, move } from './helper'

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
         isBoolean(macro.isFailure) &&
         isString(macro.userAgent) && !isEmpty(macro.userAgent) &&
         isString(macro.acceptLanguage) && !isEmpty(macro.acceptLanguage) &&
         isNumber(macro.height) && macro.height >= 1 &&
         isNumber(macro.width) && macro.width >= 1 &&
         isNumber(macro.deviceScaleFactor) && macro.deviceScaleFactor >= 1;
}

export const save = functions.https.onCall(async (data, context) => {
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
  const source = `screenshots/${context.auth.uid}/tmp.png`;
  const destination = `screenshots/${context.auth.uid}/${data.id}.png`;
  try {
    data.uid = context.auth.uid;
    data.screenshotUrl = await move(source, destination);
    data.updatedAt = admin.firestore.FieldValue.serverTimestamp();
    if (!data.createdAt) data.createdAt = data.updatedAt;
    await firestore.collection('macros').doc(data.id).set(data);
    return data;
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
});
