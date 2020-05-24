const { inspect } = require('util');
const map = require('lodash/map');
const isString = require('lodash/isString');
const isFunction = require('lodash/isFunction');
const startsWith = require('lodash/startsWith');
const trim = require('lodash/trim');
const logger = require('./logger');

class Helper {
  /**
   * @param {!number} milliseconds
   * @return {!Promise}
   */
  static delay(milliseconds) {
    return new Promise(resolve => setTimeout(resolve, milliseconds));
  }

  /**
   * @param {!Object} classType
   * @return {void}
   */
  static tracePublicAPI(classType) {
    const className = classType.prototype.constructor.name.toLowerCase();
    const classLogger = logger.child({ label: className });
    Reflect.ownKeys(classType.prototype).forEach(methodName => {
      if (methodName === 'constructor' || !isString(methodName) || startsWith(methodName, '_')) return;
      const method = Reflect.get(classType.prototype, methodName);
      if (!isFunction(method)) return;
      Reflect.set(classType.prototype, methodName, function(...args) {
        const argsText = map(args, Helper.stringifyArgument).join(', ');
        classLogger.debug(`${String(methodName)}(${argsText})`);
        return method.call(this, ...args);
      });
    });
  }

  /**
   * @param {any} arg
   * @return {!string}
   */
  static stringifyArgument(arg) {
    return map(inspect(arg).split('\n'), line => trim(line)).join(' ');
  }
}

module.exports = Helper;
