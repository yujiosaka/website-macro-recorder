import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { isString, isNumber, isBoolean, isEmpty, isArray, includes, extend } from 'lodash';
import { isUrl, move } from './lib/helper';

const RUNTIME_TIMEOUT_SECONDS = 30;
const RUNTIME_MEMORY = '256MB';

const firestore = admin.firestore();

class Runner {
  macro: Macro;

  constructor(macro: Macro) {
    this.macro = Macro;
  }

  validateMacro() {
    return isString(this.macro.id) && !isEmpty(this.macro.id) &&
           isString(this.macro.name) &&
           isUrl(this.macro.url) &&
           isUrl(this.macro.screenshotUrl) &&
           isNumber(this.macro.scheduleFrequency) && includes([0, 1, 2], this.macro.scheduleFrequency) &&
           isNumber(this.macro.scheduleHour) && this.macro.scheduleHour >= 0 && this.macro.scheduleHour < 24 &&
           isNumber(this.macro.scheduleMinute) && this.macro.scheduleMinute >= 0 && this.macro.scheduleMinute < 60 &&
           isBoolean(this.macro.notifySuccess) &&
           isBoolean(this.macro.notifyFailure) &&
           isBoolean(this.macro.checkEntirePage) &&
           isBoolean(this.macro.checkSelectedArea) &&
           isBoolean(this.macro.isEntirePageUpdated) &&
           isBoolean(this.macro.isSelectedAreaUpdated) &&
           isBoolean(this.macro.isFailure) &&
           isString(this.macro.userAgent) && !isEmpty(this.macro.userAgent) &&
           isString(this.macro.acceptLanguage) && !isEmpty(this.macro.acceptLanguage) &&
           isNumber(this.macro.viewportHeight) && this.macro.viewportHeight >= 1 &&
           isNumber(this.macro.viewportWidth) && this.macro.viewportWidth >= 1 &&
           isNumber(this.macro.deviceScaleFactor) && this.macro.deviceScaleFactor >= 1 &&
           isArray(this.macro.events) &&
           isArray(this.macro.histories);
  }

  async moveScreenshot() {
    const source = `screenshots/${this.macro.uid}/tmp.png`;
    const destination = `screenshots/${this.macro.uid}/${this.macro.id}.png`;
    try {
      this.macro.screenshotUrl = await move(source, destination);
    } catch (error) {
      console.warn(error);
      throw new functions.https.HttpsError(
        'unknown',
        'Unknown error occurred',
      );
    }
  }

  async createMacro() {
    const now = admin.firestore.Timestamp.now();
    this.macro.createdAt = now;
    this.macro.updatedAt = now;
    this.macro.executedAt = now;
    try {
      await firestore.collection('macros').doc(this.macro.id).set(this.macro);
    } catch (error) {
      console.warn(error);
      throw new functions.https.HttpsError(
        'unknown',
        'Unknown error occurred',
      );
    }
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
  const runner = new Runner({ ...macro, uid: context.auth.uid });
  if (!runner.validateMacro()) {
    throw new functions.https.HttpsError(
      'invalid-argument',
      'Argument is invalid',
    );
  }
  await runner.moveScreenshot();
  await runner.createMacro();
  return runner.macro;
});
