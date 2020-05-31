import { join } from 'path';
import { tmpdir } from 'os';
import * as functions from 'firebase-functions';
import * as puppeteer from 'puppeteer';
import { delay, upload } from './helper'

const ARGS = ['--no-sandbox', '--ignore-certificate-errors'];
const TIMEOUT = 3000;

function getTmpPath(filename: string) {
  return join(tmpdir(), filename);
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
    await delay(milliseconds);
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
    console.warn(error);
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
      'Not authenticated',
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
    console.warn(error);
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
    console.warn(error);
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
