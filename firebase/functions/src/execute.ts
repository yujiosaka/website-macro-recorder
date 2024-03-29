import { extend, omit, slice } from 'lodash';
import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import * as sharp from 'sharp';
import pixelmatch = require('pixelmatch');
import { PNG } from 'pngjs';
import { getTmpPath, download, upload } from './lib/helper';
import Crawler from './lib/crawler';
import { MAX_HISTORIES } from './lib/const';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

const firestore = admin.firestore();

class Runner {
  macro: Macro;

  constructor(macro: Macro) {
    this.macro = macro;
  }

  async downloadScreenshot() {
    const source = `screenshots/${this.macro.uid}/${this.macro.id}.png`;
    try {
      return await download(source);
    } catch (error) {
      console.warn(error);
      throw new functions.https.HttpsError('unknown', 'Unknown error occurred');
    }
  }

  async saveScreenshot() {
    const destination = getTmpPath(`${this.macro.id}.png`);
    try {
      const crawler = await Crawler.launch();
      await crawler.crawl(this.macro);
      const buffer = await crawler.screenshot({ path: destination, fullPage: true });
      await crawler.close();
      return buffer;
    } catch (error) {
      if (error instanceof functions.https.HttpsError) throw error;
      console.warn(error);
      throw new functions.https.HttpsError('unknown', 'Unknown error occurred');
    }
  }

  async checkUpdate(original: Buffer, current: Buffer) {
    const history = this.defaultHistory();
    try {
      if (this.macro.checkEntirePage) {
        history.isEntirePageUpdated = this.compareImage(original, current);
      }
      if (this.macro.checkSelectedArea && this.isAreaSelected()) {
        if (this.canSelectArea(original)) {
          const shape = this.getShape();
          const originalSelectedArea = await sharp(original).extract(shape).toBuffer();
          const currentSelectedArea = await sharp(current).extract(shape).toBuffer();
          history.isSelectedAreaUpdated = this.compareImage(originalSelectedArea, currentSelectedArea);
        } else {
          history.isSelectedAreaUpdated = true;
        }
      }
      return history;
    } catch (error) {
      console.warn(error);
      throw new functions.https.HttpsError('unknown', 'Unknown error occurred');
    }
  }

  async uploadScreenshot(history: MacroHistory) {
    const source = getTmpPath(`${this.macro.id}.png`);
    const destination = `screenshots/${this.macro.uid}/${this.macro.id}/histories/${history.executedAt.toMillis()}.png`;
    try {
      return await upload(source, destination);
    } catch (error) {
      console.warn(error);
      throw new functions.https.HttpsError('unknown', 'Unknown error occurred');
    }
  }

  async updateMacro(history: MacroHistory) {
    const update = omit(history, 'screenshotUrl');
    const histories = slice(this.macro.histories.concat(history), -MAX_HISTORIES);
    try {
      await firestore.collection('macros').doc(this.macro.id).update(extend({}, update, { histories }));
    } catch (error) {
      console.warn(error);
      throw new functions.https.HttpsError('unknown', 'Unknown error occurred');
    }
  }

  defaultHistory(override = {}) {
    return extend({
      screenshotUrl: '',
      isEntirePageUpdated: false,
      isSelectedAreaUpdated: false,
      isFailure: false,
      executedAt: admin.firestore.Timestamp.now(),
    }, override);
  }

  private compareImage(original: Buffer, current: Buffer) {
    const originalImage = PNG.sync.read(original);
    const currentImage = PNG.sync.read(current);
    if (originalImage.width !== currentImage.width) return false;
    if (originalImage.height !== currentImage.height) return false;
    const diff = pixelmatch(originalImage.data, currentImage.data, null, originalImage.width, originalImage.height);
    return diff === 1;
  }

  private isAreaSelected() {
    if (this.macro.selectedAreaLeft === null || this.macro.selectedAreaLeft === undefined) return false;
    if (this.macro.selectedAreaTop === null || this.macro.selectedAreaTop === undefined) return false;
    if (this.macro.selectedAreaRight === null || this.macro.selectedAreaRight === undefined) return false;
    if (this.macro.selectedAreaBottom === null || this.macro.selectedAreaBottom === undefined) return false;
    return true;
  }

  private canSelectArea(original: Buffer) {
    const originalImage = PNG.sync.read(original);
    if (originalImage.width < this.macro.selectedAreaRight) return false;
    if (originalImage.height < this.macro.selectedAreaBottom) return false;
    return true;
  }

  private getShape() {
    return {
      width: this.macro.selectedAreaRight - this.macro.selectedAreaLeft,
      height: this.macro.selectedAreaBottom - this.macro.selectedAreaTop,
      left: this.macro.selectedAreaLeft,
      top: this.macro.selectedAreaTop,
    };
  }
}

export const execute = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'Not authenticated');
  }
  const doc = await firestore.collection('macros').doc(data).get();
  if (!doc.exists) {
    throw new functions.https.HttpsError('invalid-argument', 'Argument is invalid');
  }
  const macro = doc.data() as Macro;
  if (context.auth.uid !== macro.uid) {
    throw new functions.https.HttpsError('permission-denied', 'Permission denied');
  }
  const runner = new Runner(macro);
  const original = await runner.downloadScreenshot();
  let current: Buffer;
  let history: MacroHistory;
  try {
    current = await runner.saveScreenshot();
  } catch (error) {
    history = runner.defaultHistory({ isFailure: true });
    await runner.updateMacro(history);
    throw error;
  }
  history = await runner.checkUpdate(original, current);
  history.screenshotUrl = await runner.uploadScreenshot(history);
  await runner.updateMacro(history);
  return history;
});
