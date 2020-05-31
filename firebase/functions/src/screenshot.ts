import * as path from 'path';
import * as os from 'os';
import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as puppeteer from 'puppeteer';
import * as moment from 'moment';
import * as helper from './helper'

const ARGS = ['--no-sandbox', '--ignore-certificate-errors'];
const TIMEOUT = 3000;

const storage = admin.storage();

function getTmpPath(filename: string) {
  return path.join(os.tmpdir(), filename);
}

function getDevice(macro: Macro) {
  return {
    viewport: {
      height: Math.floor(macro.height / macro.deviceScaleFactor),
      width: Math.floor(macro.width / macro.deviceScaleFactor),
      deviceScaleFactor: macro.deviceScaleFactor,
      isMobile: true,
      hasTouch: true,
    },
    userAgent: macro.userAgent,
  };
}

async function triggerEvent(page: puppeteer.Page, event: MacroEvent) {
  if (event.name === 'wait') {
    const milliseconds = parseInt(event.value);
    await helper.delay(milliseconds);
    return;
  }
  const element = await page.waitForXPath(event.xPath);
  switch(event.name) {
    case 'click':
      await element.click();
      break;
    case 'select':
      await element.select(event.value);
      break;
    case 'change':
      await element.type(event.value);
      break;
  }
  try {
    await page.waitForNavigation({ timeout: TIMEOUT });
  } catch (error) {
    if (error instanceof puppeteer.errors.TimeoutError) return;
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

async function saveScreenshot(macro: Macro, context: functions.https.CallableContext) {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      'unauthenticated',
      'User is not authenticated',
    );
  }
  try {
    const browser = await puppeteer.launch({ args: ARGS });
    const page = await browser.newPage();
    const device = getDevice(macro)
    await page.emulate(device);
    await page.goto(macro.url);
    for (const event of macro.events) {
      await triggerEvent(page, event);
    }
    const tmpPath = getTmpPath(`${context.auth.uid}.png`);
    await page.screenshot({ path: tmpPath, fullPage: true });
    await browser.close();
  } catch (error) {
    if (error instanceof puppeteer.errors.TimeoutError) {
      throw new functions.https.HttpsError(
        'deadline-exceeded',
        'Timeout error occurred',
      );
    }
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
      'User is not authenticated',
    );
  }
  const tmpPath = getTmpPath(`${context.auth.uid}.png`);
  const destination = `screenshots/${context.auth.uid}/tmp.png`;
  const expires = moment().add(1, 'hour').toDate();
  try {
    const [file] = await storage.bucket().upload(tmpPath, { destination });
    const [signedUrl] = await file.getSignedUrl({ action: 'read', expires });
    return signedUrl;
  } catch (error) {
    throw new functions.https.HttpsError(
      'unknown',
      'Unknown error occurred',
    );
  }
}

export const screenshot = functions.https.onCall(async (data, context) => {
  await saveScreenshot(data, context);
  return uploadScreenshot(context);
});
