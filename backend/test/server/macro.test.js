const axios = require('axios');
const { Macro } = require('../../src/mongo');
const Server = require('../../src/server');
const { MACRO, SCHEDULED_MACRO, INVALID_MACRO } = require('../fixture/macro');

const BASE_URL = `http://localhost:${process.env.SERVER_PORT}`;
const INSTANCE_ID = 'xxx';
const INVALID_INSTANCE_ID = 'yyy';
const INVALID_MACRO_ID = '5eca7662116127c5c253f023';

describe('Macro', () => {
  const server = new Server();

  beforeEach(async () => {
    await server.start();
  });

  afterEach(async () => {
    await server.stop();
    await Macro.deleteMany();
  });

  test('it creates macro', async () => {
    await axios.post(`${BASE_URL}/api/macros?instanceId=${INSTANCE_ID}`, MACRO);
    const macro = await Macro.findOne({ instanceId: INSTANCE_ID });
    expect(macro.name).toEqual(MACRO.name);
    expect(macro.url).toEqual(MACRO.url);
    expect(macro.scheduleFrequency).toEqual(MACRO.scheduleFrequency);
    expect(macro.scheduleHour).toEqual(MACRO.scheduleHour);
    expect(macro.scheduleMinute).toEqual(MACRO.scheduleMinute);
    expect(macro.notifySuccess).toEqual(MACRO.notifySuccess);
    expect(macro.notifyFailure).toEqual(MACRO.notifyFailure);
    expect(macro.acceptLanguage).toEqual(MACRO.acceptLanguage);
    expect(macro.userAgent).toEqual(MACRO.userAgent);
  });

  test('it fails validation for creating macro', async () => {
    try {
      await axios.post(`${BASE_URL}/api/macros?instanceId=${INSTANCE_ID}`, INVALID_MACRO);
    } catch (err) {
      expect(err.response.status).toEqual(422);
      expect(err.response.data.message).toEqual('Invalid macro');
    }
  });

  test('it does not find macro with invalid macro id', async () => {
    try {
      await axios.get(`${BASE_URL}/api/macros/${INVALID_MACRO_ID}?instanceId=${INSTANCE_ID}`);
    } catch (err) {
      expect(err.response.status).toEqual(404);
      expect(err.response.data.message).toEqual('Macro not found');
    }
  });

  test('it does not update macro with invalid macro id', async () => {
    try {
      await axios.put(`${BASE_URL}/api/macros/${INVALID_MACRO_ID}?instanceId=${INSTANCE_ID}`, MACRO);
    } catch (err) {
      expect(err.response.status).toEqual(404);
      expect(err.response.data.message).toEqual('Macro not found');
    }
  });

  test('it does not delete macro with invalid macro id', async () => {
    try {
      await axios.delete(`${BASE_URL}/api/macros/${INVALID_MACRO_ID}?instanceId=${INSTANCE_ID}`);
    } catch (err) {
      expect(err.response.status).toEqual(404);
      expect(err.response.data.message).toEqual('Macro not found');
    }
  });

  describe('after creating macro', () => {
    let _id;
    beforeEach(async () => {
      const res = await axios.post(`${BASE_URL}/api/macros?instanceId=${INSTANCE_ID}`, MACRO);
      _id = res.data._id;
    });

    test('it finds macro', async () => {
      const res = await axios.get(`${BASE_URL}/api/macros/${_id}?instanceId=${INSTANCE_ID}`);
      expect(res.data.name).toEqual(MACRO.name);
      expect(res.data.url).toEqual(MACRO.url);
      expect(res.data.scheduleFrequency).toEqual(MACRO.scheduleFrequency);
      expect(res.data.scheduleHour).toEqual(MACRO.scheduleHour);
      expect(res.data.scheduleMinute).toEqual(MACRO.scheduleMinute);
      expect(res.data.notifySuccess).toEqual(MACRO.notifySuccess);
      expect(res.data.notifyFailure).toEqual(MACRO.notifyFailure);
      expect(res.data.acceptLanguage).toEqual(MACRO.acceptLanguage);
      expect(res.data.userAgent).toEqual(MACRO.userAgent);
    });

    test('it does not find macro with invalid instance id', async () => {
      try {
        await axios.get(`${BASE_URL}/api/macros/${_id}?instanceId=${INVALID_INSTANCE_ID}`);
      } catch (err) {
        expect(err.response.status).toEqual(401);
        expect(err.response.data.message).toEqual('Invalid instance ID');
      }
    });

    test('it updates macro', async () => {
      await axios.put(`${BASE_URL}/api/macros/${_id}?instanceId=${INSTANCE_ID}`, SCHEDULED_MACRO);
      const macro = await Macro.findOne({ _id, instanceId: INSTANCE_ID });
      expect(macro.name).toEqual(SCHEDULED_MACRO.name);
      expect(macro.url).toEqual(SCHEDULED_MACRO.url);
      expect(macro.scheduleFrequency).toEqual(SCHEDULED_MACRO.scheduleFrequency);
      expect(macro.scheduleHour).toEqual(SCHEDULED_MACRO.scheduleHour);
      expect(macro.scheduleMinute).toEqual(SCHEDULED_MACRO.scheduleMinute);
      expect(macro.notifySuccess).toEqual(SCHEDULED_MACRO.notifySuccess);
      expect(macro.notifyFailure).toEqual(SCHEDULED_MACRO.notifyFailure);
      expect(macro.acceptLanguage).toEqual(SCHEDULED_MACRO.acceptLanguage);
      expect(macro.userAgent).toEqual(SCHEDULED_MACRO.userAgent);
    });

    test('it fails validation for updating macro', async () => {
      try {
        await axios.put(`${BASE_URL}/api/macros/${_id}?instanceId=${INSTANCE_ID}`, INVALID_MACRO);
      } catch (err) {
        expect(err.response.status).toEqual(422);
        expect(err.response.data.message).toEqual('Invalid macro');
      }
    });

    test('it does not find macro with invalid instance id', async () => {
      try {
        await axios.put(`${BASE_URL}/api/macros/${_id}?instanceId=${INVALID_INSTANCE_ID}`, SCHEDULED_MACRO);
      } catch (err) {
        expect(err.response.status).toEqual(401);
        expect(err.response.data.message).toEqual('Invalid instance ID');
      }
    });

    test('it deletes macro', async () => {
      await axios.delete(`${BASE_URL}/api/macros/${_id}?instanceId=${INSTANCE_ID}`);
      const macro = await Macro.findOne({ _id, instanceId: INSTANCE_ID });
      expect(macro).toBeNull();
    });

    test('it does not delete macro with invalid instance id', async () => {
      try {
        await axios.delete(`${BASE_URL}/api/macros/${_id}?instanceId=${INVALID_INSTANCE_ID}`);
      } catch (err) {
        expect(err.response.status).toEqual(401);
        expect(err.response.data.message).toEqual('Invalid instance ID');
      }
    });
  });
});
