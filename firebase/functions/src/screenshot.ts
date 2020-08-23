import * as functions from 'firebase-functions';
import { getTmpPath, upload } from './helper';
import Crawler from './crawler';

const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

async function saveScreenshot(macro: Macro, context: functions.https.CallableContext) {
  const destination = getTmpPath(`${context.auth!.uid}.png`);
  try {
    const crawler = await Crawler.launch();
    await crawler.run(macro);
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

async function uploadScreenshot(context: functions.https.CallableContext) {
  const source = getTmpPath(`${context.auth!.uid}.png`);
  const destination = `screenshots/${context.auth!.uid}/tmp.png`;
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
  await saveScreenshot(data, context);
  return uploadScreenshot(context);
});
