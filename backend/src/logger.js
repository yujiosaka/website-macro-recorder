const {
  createLogger,
  transports,
  format,
} = require('winston');

const customPrintf = format.printf(({
  level,
  message,
  label,
  timestamp,
}) => {
  const note = label ? ` (${label})` : '';
  return `${timestamp} ${level}${note}: ${message}`;
});

const customFormat = format.combine(
  format.colorize(),
  format.timestamp(),
  customPrintf,
);

const logger = createLogger({
  level: process.env.LOG_LEVEL,
  transports: [
    new transports.Console({ format: customFormat }),
  ],
});

module.exports = logger;
