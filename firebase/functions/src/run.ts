import * as functions from 'firebase-functions';
import { getTmpPath } from './helper';
import Crawler from './crawler';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

export const run = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }

  try {
    const crawler = await Crawler.launch();
    await crawler.run(data);
    const tmpPath = getTmpPath(`${context.auth.uid}.png`);
    await crawler.screenshot(tmpPath);
    await crawler.close();
  } catch (error) {
    if (error instanceof functions.https.HttpsError) throw error;
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
});
