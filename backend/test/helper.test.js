const { EventEmitter } = require('events');
const noop = require('lodash/noop');
const logger = require('../src/logger');
const {
  delay,
  tracePublicAPI,
  stringifyArgument,
} = require('../src/helper');

describe('Helper', () => {
  describe('Helper.delay', () => {
    test('it waits until shorter delay', async () => {
      let waited = false;
      delay(10).then(() => { waited = true; });
      await delay(20);
      expect(waited).toBe(true);
    });

    test('it does not wait until longer delay', async () => {
      let waited = false;
      delay(20).then(() => { waited = true; });
      await delay(10);
      expect(waited).toBe(false);
    });
  });

  describe('Helper.tracePublicAPI', () => {
    beforeEach(() => {
      class Emitter extends EventEmitter {
        emit(...args) { super.emit(...args); }

        on(...args) { super.on(...args); }

        _eventNames() { super.eventNames(); }
      }
      Emitter.Events = { Start: 's' };
      tracePublicAPI(Emitter);
      this.emitter = new Emitter();
      this.debug = jest.spyOn(logger, 'debug');
    });

    afterEach(() => {
      this.debug.mockRestore();
    });

    test('it does not trace parent API', () => {
      this.emitter.eventNames();
      expect(this.debug).not.toHaveBeenCalled();
    });

    test('it does not trace private API', () => {
      this.emitter._eventNames();
      expect(this.debug).not.toHaveBeenCalled();
    });

    test('it traces public API with object argument', () => {
      this.emitter.emit('u', { hubspotUserToken: null });
      expect(this.debug).toHaveBeenCalledTimes(1);
      expect(this.debug).toHaveBeenCalledWith("emit('u', { hubspotUserToken: null })");
    });

    test('it traces public API with function argument', () => {
      this.emitter.on('u', noop);
      expect(this.debug).toHaveBeenCalledTimes(1);
      expect(this.debug).toHaveBeenCalledWith("on('u', [Function: noop])");
    });
  });

  describe('Helper.stringifyArgument', () => {
    test('it stringifies undefined', () => {
      const actual = stringifyArgument(undefined);
      const expected = 'undefined';
      expect(actual).toBe(expected);
    });

    test('it stringifies null', () => {
      const actual = stringifyArgument(null);
      const expected = 'null';
      expect(actual).toBe(expected);
    });

    test('it stringifies boolean', () => {
      const actual = stringifyArgument(false);
      const expected = 'false';
      expect(actual).toBe(expected);
    });

    test('it stringifies string', () => {
      const actual = stringifyArgument('http://localhost:8081/');
      const expected = "'http://localhost:8081/'";
      expect(actual).toBe(expected);
    });

    test('it stringifies number', () => {
      const actual = stringifyArgument(3);
      const expected = '3';
      expect(actual).toBe(expected);
    });

    test('it stringifies function', () => {
      const actual = stringifyArgument(noop);
      const expected = '[Function: noop]';
      expect(actual).toBe(expected);
    });

    test('it stringifies object', () => {
      const actual = stringifyArgument({
        _id: 'localhost',
        url: 'http://localhost:8081/',
      });
      const expected = "{ _id: 'localhost', url: 'http://localhost:8081/' }";
      expect(actual).toBe(expected);
    });
  });
});
