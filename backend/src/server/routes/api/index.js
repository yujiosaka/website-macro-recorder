const { Router } = require('express');
const macros = require('./macros');

const router = Router();

router.use('/macros', macros);

module.exports = router;
