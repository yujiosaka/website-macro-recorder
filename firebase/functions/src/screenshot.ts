import { map } from 'lodash';
import { join } from 'path';
import { tmpdir } from 'os';
import * as functions from 'firebase-functions';
import * as puppeteer from 'puppeteer';
import { upload } from './helper'

const ARGS = ['--no-sandbox', '--ignore-certificate-errors'];
const EVENT_TIMEOUT_MILLISECONDS = 30000;
const RUNTIME_TIMEOUT_SECONDS = 180;
const RUNTIME_MEMORY = '2GB';

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

async function triggerEvents(page: puppeteer.Page, events: MacroEvent[]) {
  if (events.length === 0) return;
  const serialEvents: MacroEvent[] = [];
  for (let i = 0; i < events.length; i++) {
    var event = events[i];
    if (event.name !== 'page') {
      await triggerEventSeries(page, serialEvents);
      serialEvents.splice(0); // Reset array
    }
    event.position = i;
    serialEvents.push(event);
  }
  await triggerEventSeries(page, serialEvents);
}

// NOTE:
// Serieal events are ordered in the following way
// serialEvents = [
//   { name: 'click', ... },
//   { name: 'page', ... },
// ]
// Nedd to reverse the order because Promise needs to be resolved in the following order
// await Promise.all([
//   triggerEvent(page, { name: 'page', ... }),
//   triggerEvent(page, { name: 'click', ... }),
// ])
// Otherwise, page may be completed when the click event is triggered
// and it keeps waiting for the page event
async function triggerEventSeries(page: puppeteer.Page, serialEvents: MacroEvent[]) {
  if (serialEvents.length === 0) return;
  const events = serialEvents.reverse();
  await Promise.all(map(events, async event => {
    await triggerEvent(page, event);
  }));
}

async function triggerEvent(page: puppeteer.Page, event: MacroEvent) {
  try {
    if (event.name === 'timer') {
      await page.waitFor(parseInt(event.value) * 1000);
      return;
    }
    if (event.name === 'page') {
      await page.waitForNavigation({ timeout: EVENT_TIMEOUT_MILLISECONDS });
      return;
    }
    const element = await page.waitForXPath(event.xPath, { timeout: EVENT_TIMEOUT_MILLISECONDS });
    switch (event.name) {
      case 'click':
        await element.click();
        break;
      case 'select':
        await element.select(event.value);
        break;
      case 'change':
        await element.type(event.value);
        break;
      default:
        throw new functions.https.HttpsError(
          'invalid-argument',
          'Argument is invalid',
        );
    }
  } catch (error) {
    if (error instanceof functions.https.HttpsError) throw error;
    if (error instanceof puppeteer.errors.TimeoutError) {
      throw new functions.https.HttpsError(
        'deadline-exceeded',
        `Timeout error occurred position: ${event.position}`,
      );
    }
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
    await triggerEvents(page, macro.events);
    const tmpPath = getTmpPath(`${context.auth.uid}.png`);
    await page.screenshot({ path: tmpPath, fullPage: true });
    await browser.close();
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
