function boomHandler(err, req, res, next) { // eslint-disable-line no-unused-vars
  if (!err.output) return;
  res.status(err.output.statusCode);
  res.set(err.output.headers);
  res.send(err.output.payload);
}

module.exports = boomHandler;
