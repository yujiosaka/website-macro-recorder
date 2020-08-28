import * as functions from 'firebase-functions';
import { getTmpPath, upload } from './lib/helper';
import Crawler from './lib/crawler';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

class Runner {
  macro: Macro;

  constructor(macro: Macro) {
    this.macro = macro;
  }

  async saveScreenshot() {
    const destination = getTmpPath(`${this.macro.uid}.png`);
    try {
      const crawler = await Crawler.launch();
      await crawler.crawl(this.macro);
      await crawler.screenshot({ path: destination, fullPage: true });
      await crawler.close();
    } catch (error) {
      if (error instanceof functions.https.HttpsError) throw error;
      console.warn(error);
      throw new functions.https.HttpsError(
        'unknown',
        'Unknown error occurred',
      );
    }
  }

  async uploadScreenshot() {
    const source = getTmpPath(`${this.macro.uid}.png`);
    const destination = `screenshots/${this.macro.uid}/tmp.png`;
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
}

export const screenshot = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  const runner = new Runner({ ...data, uid: context.auth.uid });
  await runner.saveScreenshot();
  return runner.uploadScreenshot();
});
