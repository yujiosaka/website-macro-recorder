import * as functions from 'firebase-functions';
import * as sharp from 'sharp';
import pixelmatch = require('pixelmatch');
import { PNG } from 'pngjs';
import { getTmpPath, download } from './helper';
import Crawler from './crawler';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

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
  const destination = getTmpPath(`${macro.id}-current.png`);
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
  try {
    let entirePage = true;
    let selectedArea = true;
    if (macro.checkEntirePage) {
      entirePage = compareImage(original, current);
    }
    if (macro.checkSelectedArea && isAreaSelected(macro)) {
      const shape = getShape(macro);
      const originalSelectedArea = await sharp(original).extract(shape).toBuffer();
      const currentSelectedArea = await sharp(current).extract(shape).toBuffer();
      selectedArea = compareImage(originalSelectedArea, currentSelectedArea);
    }
    return { entirePage, selectedArea };
  } catch (error) {
    console.warn(error);
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

function compareImage(original: Buffer, current: Buffer) {
  const originalImage = PNG.sync.read(original);
  const currentImage = PNG.sync.read(current);
  if (originalImage.width !== currentImage.width) return false;
  if (originalImage.height !== currentImage.height) return false;
  const diff = pixelmatch(originalImage.data, currentImage.data, null, originalImage.width, originalImage.height);
  return diff === 1;
}

function getShape(macro: Macro) {
  return {
    width: macro.selectedAreaRight - macro.selectedAreaLeft,
    height: macro.selectedAreaBottom - macro.selectedAreaTop,
    left: macro.selectedAreaLeft,
    top: macro.selectedAreaTop,
  };
}

function isAreaSelected(macro: Macro) {
  if (macro.selectedAreaLeft === null || macro.selectedAreaLeft === undefined) return false;
  if (macro.selectedAreaTop === null || macro.selectedAreaTop === undefined) return false;
  if (macro.selectedAreaRight === null || macro.selectedAreaRight === undefined) return false;
  if (macro.selectedAreaBottom === null || macro.selectedAreaBottom === undefined) return false;
  return true;
}

export const execute = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  const original = await downloadScreenshot(data, context);
  const current = await saveScreenshot(data);
  return checkUpdate(data, original, current);
});
