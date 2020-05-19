const { Router } = require('express');
const api = require('./api');

const router = Router();

router.use('/api', api);

module.exports = router;
