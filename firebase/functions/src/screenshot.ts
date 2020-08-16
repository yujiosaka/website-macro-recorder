import * as functions from 'firebase-functions';
import { getTmpPath, upload } from './helper';
import Crawler from './crawler';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

async function saveScreenshot(macro: Macro, context: functions.https.CallableContext) {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  try {
    const crawler = await Crawler.launch();
    await crawler.run(macro);
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
}

async function uploadScreenshot(context: functions.https.CallableContext) {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'Not authenticated',
    );
  }
  const tmpPath = getTmpPath(`${context.auth.uid}.png`);
  const destination = `screenshots/${context.auth.uid}/tmp.png`;
  try {
    return await upload(tmpPath, destination);
  } catch (error) {
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

export const screenshot = functions.runWith({
  timeoutSeconds: RUNTIME_TIMEOUT_SECONDS,
  memory: RUNTIME_MEMORY,
}).https.onCall(async (data, context) => {
  await saveScreenshot(data, context);
  return uploadScreenshot(context);
});
