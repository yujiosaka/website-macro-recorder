import { map } from 'lodash';
import * as puppeteer from 'puppeteer';
import * as functions from 'firebase-functions';

const ARGS = ['--no-sandbox', '--ignore-certificate-errors'];
const EVENT_TIMEOUT_MILLISECONDS = 30000;

export default class Crawler {
  static async launch() {
    const browser = await puppeteer.launch({ args: ARGS });
    const page = await browser.newPage();
    return new Crawler(browser, page);
  }

  browser: puppeteer.Browser;
  page: puppeteer.Page;

  constructor(browser: puppeteer.Browser, page: puppeteer.Page) {
    this.browser = browser;
    this.page = page;
  }

  async crawl(macro: Macro) {
    const device = this.getDevice(macro);
    await this.page.emulate(device);
    await this.page.goto(macro.url);
    await this.triggerEvents(macro.events);
  }

  getDevice(macro: Macro) {
    return {
      viewport: {
        height: Math.floor(macro.viewportHeight / macro.deviceScaleFactor),
        width: Math.floor(macro.viewportWidth / macro.deviceScaleFactor),
        deviceScaleFactor: macro.deviceScaleFactor,
        isMobile: true,
        hasTouch: true,
        isLandscape: false,
      },
      userAgent: macro.userAgent,
    };
  }

  async triggerEvent(event: MacroEvent) {
    try {
      if (event.name === 'timer') {
        await this.page.waitFor(parseInt(event.value) * 1000);
        return;
      }
      if (event.name === 'page') {
        await this.page.waitForNavigation({ timeout: EVENT_TIMEOUT_MILLISECONDS });
        return;
      }
      const element = await this.page.waitForXPath(event.xPath, { timeout: EVENT_TIMEOUT_MILLISECONDS });
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
      if (error instanceof puppeteer.errors.TimeoutError) {
        throw new functions.https.HttpsError(
          'deadline-exceeded',
          `Timeout error occurred position: ${event.position}`,
        );
      }
      throw error;
    }
  }

  async triggerEvents(events: MacroEvent[]) {
    if (events.length === 0) return;
    const serialEvents: MacroEvent[] = [];
    for (let i = 0; i < events.length; i++) {
      const event = events[i];
      if (event.name !== 'page') {
        await this.triggerEventSeries(serialEvents);
        serialEvents.splice(0); // Reset array
      }
      event.position = i;
      serialEvents.push(event);
    }
    await this.triggerEventSeries(serialEvents);
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
  async triggerEventSeries(serialEvents: MacroEvent[]) {
    if (serialEvents.length === 0) return;
    const events = serialEvents.reverse();
    await Promise.all(map(events, async (event: MacroEvent) => {
      await this.triggerEvent(event);
    }));
  }

  async screenshot(options: object) {
    return await this.page.screenshot(options);
  }

  async close() {
    await this.browser.close();
  }
}
