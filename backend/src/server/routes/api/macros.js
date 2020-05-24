const Boom = require('boom');
const { Router } = require('express');
const { Macro } = require('../../../mongo');

const router = Router();

router.post('/', async (req, res, next) => {
  const { instanceId } = req.query;
  const macro = new Macro({ ...req.body, instanceId });
  try {
    await macro.validate();
  } catch (err) {
    next(Boom.badData('Invalid macro'));
    return;
  }
  try {
    await macro.save();
  } catch (err) {
    next(Boom.badImplementation('Unexpected error'));
    return;
  }
  res.json(macro);
});

router.get('/:_id', async (req, res, next) => {
  let macro;
  try {
    macro = await Macro.findOne(req.params);
  } catch (err) {
    next(Boom.badImplementation('Unexpected error'));
    return;
  }
  if (!macro) {
    next(Boom.notFound('Macro not found'));
    return;
  }
  const { instanceId } = req.query;
  if (macro.instanceId !== instanceId) {
    next(Boom.unauthorized('Invalid instance ID'));
    return;
  }
  res.json(macro);
});

router.put('/:_id', async (req, res, next) => {
  let macro;
  try {
    macro = await Macro.findOne(req.params);
  } catch (err) {
    next(Boom.badImplementation('Unexpected error'));
    return;
  }
  if (!macro) {
    next(Boom.notFound('Macro not found'));
    return;
  }
  const { instanceId } = req.query;
  if (macro.instanceId !== instanceId) {
    next(Boom.unauthorized('Invalid instance ID'));
    return;
  }
  macro.set(req.body);
  try {
    await macro.validate();
  } catch (err) {
    next(Boom.badData('Invalid macro'));
    return;
  }
  try {
    await macro.save();
  } catch (err) {
    next(Boom.badImplementation('Unexpected error'));
    return;
  }
  res.json(macro);
});

router.delete('/:_id', async (req, res, next) => {
  let macro;
  try {
    macro = await Macro.findOne(req.params);
  } catch (err) {
    next(Boom.badImplementation('Unexpected error'));
    return;
  }
  if (!macro) {
    next(Boom.notFound('Macro not found'));
    return;
  }
  const { instanceId } = req.query;
  if (macro.instanceId !== instanceId) {
    next(Boom.unauthorized('Invalid instance ID'));
    return;
  }
  try {
    await Macro.deleteOne({ ...req.params, instanceId });
  } catch (err) {
    next(Boom.badImplementation('Unexpected error'));
    return;
  }
  res.json(null);
});

module.exports = router;
