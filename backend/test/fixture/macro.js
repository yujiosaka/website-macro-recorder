const MACRO = {
  name: 'Example',
  url: 'http://example.com/',
  scheduleFrequency: 0,
  scheduleHour: 0,
  scheduleMinute: 0,
  notifySuccess: false,
  notifyFailure: false,
  acceptLanguage: 'en',
  userAgent: 'Mozilla/5.0 (Linux; Android 8.1.0; Nexus 5X Build/OPM2.171019.029) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.126 Mobile Safari/537.36',
};

const SCHEDULED_MACRO = {
  name: 'Example',
  url: 'http://example.com/',
  scheduleFrequency: 1,
  scheduleHour: 10,
  scheduleMinute: 0,
  notifySuccess: false,
  notifyFailure: false,
  acceptLanguage: 'en',
  userAgent: 'Mozilla/5.0 (Linux; Android 8.1.0; Nexus 5X Build/OPM2.171019.029) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.126 Mobile Safari/537.36',
};

const INVALID_MACRO = {
  name: null,
  url: null,
  scheduleFrequency: null,
  scheduleHour: null,
  scheduleMinute: null,
  notifySuccess: null,
  notifyFailure: null,
  acceptLanguage: null,
  userAgent: null,
};

module.exports = { MACRO, SCHEDULED_MACRO, INVALID_MACRO };
