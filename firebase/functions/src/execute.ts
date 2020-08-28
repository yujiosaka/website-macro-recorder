import { extend, omit } from 'lodash';
import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import * as sharp from 'sharp';
import pixelmatch = require('pixelmatch');
import { PNG } from 'pngjs';
import { getTmpPath, download, upload } from './lib/helper';
import Crawler from './lib/crawler';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

const firestore = admin.firestore();

async function downloadScreenshot(macro: Macro, context: functions.https.CallableContext) {
  const source = `screenshots/${context.auth!.uid}/${macro.id}.png`;
  try {
    return await download(source);
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

async function saveScreenshot(macro: Macro) {
  const destination = getTmpPath(`${macro.id}.png`);
  try {
    const crawler = await Crawler.launch();
    await crawler.crawl(macro);
    const buffer = await crawler.screenshot({ path: destination, fullPage: true });
    await crawler.close();
    return buffer;
  } catch (error) {
    if (error instanceof functions.https.HttpsError) throw error;
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

async function checkUpdate(macro: Macro, original: Buffer, current: Buffer) {
  const history = defaultHistory();
  try {
    if (macro.checkEntirePage) {
      history.isEntirePageUpdated = compareImage(original, current);
    }
    if (macro.checkSelectedArea && isAreaSelected(macro)) {
      const shape = getShape(macro);
      const originalSelectedArea = await sharp(original).extract(shape).toBuffer();
      const currentSelectedArea = await sharp(current).extract(shape).toBuffer();
      history.isSelectedAreaUpdated = compareImage(originalSelectedArea, currentSelectedArea);
    }
    return history;
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

function defaultHistory() {
  return {
    screenshotUrl: '',
    isEntirePageUpdated: false,
    isSelectedAreaUpdated: false,
    isFailure: false,
    executedAt: admin.firestore.Timestamp.now(),
  };
}

function compareImage(original: Buffer, current: Buffer) {
  const originalImage = PNG.sync.read(original);
  const currentImage = PNG.sync.read(current);
  if (originalImage.width !== currentImage.width) return false;
  if (originalImage.height !== currentImage.height) return false;
  const diff = pixelmatch(originalImage.data, currentImage.data, null, originalImage.width, originalImage.height);
  return diff === 1;
}

function isAreaSelected(macro: Macro) {
  if (macro.selectedAreaLeft === null || macro.selectedAreaLeft === undefined) return false;
  if (macro.selectedAreaTop === null || macro.selectedAreaTop === undefined) return false;
  if (macro.selectedAreaRight === null || macro.selectedAreaRight === undefined) return false;
  if (macro.selectedAreaBottom === null || macro.selectedAreaBottom === undefined) return false;
  return true;
}

function getShape(macro: Macro) {
  return {
    width: macro.selectedAreaRight - macro.selectedAreaLeft,
    height: macro.selectedAreaBottom - macro.selectedAreaTop,
    left: macro.selectedAreaLeft,
    top: macro.selectedAreaTop,
  };
}

async function updateMacro(macro: Macro, history: MacroHistory) {
  const update = omit(history, 'screenshotUrl');
  try {
    await firestore.collection('macros').doc(macro.id).update(extend({}, update, {
      histories: admin.firestore.FieldValue.arrayUnion(history),
    }));
    return { ...macro, ...update };
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

async function uploadScreenshot(macro: Macro, history: MacroHistory) {
  const source = getTmpPath(`${macro.id}.png`);
  const destination = `screenshots/${macro.uid}/${macro.id}/histories/${history.executedAt.toMillis()}.png`;
  try {
    return await upload(source, destination);
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

export const execute = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (macro: Macro, context: functions.https.CallableContext) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  try {
    const original = await downloadScreenshot(macro, context);
    const current = await saveScreenshot(macro);
    const history = await checkUpdate(macro, original, current);
    history.screenshotUrl = await uploadScreenshot(macro, history);
    return await updateMacro(macro, history);
  } catch (error) {
    const history = defaultHistory();
    await updateMacro(macro, extend({}, history, { isFailure: true }));
    throw error;
  }
});
