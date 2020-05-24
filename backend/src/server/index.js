const express = require('express');
const cors = require('cors');
const { urlencoded, json } = require('body-parser');
const routes = require('./routes');
const boomHandler = require('./boom-handler');
const { tracePublicAPI } = require('../helper');
const logger = require('../logger');

const serverLogger = logger.child({ label: 'server' });

class Server {
  constructor() {
    this._app = express();
    this._app.use(urlencoded({ extended: true }));
    this._app.use(json());
    this._app.use(cors());
    this._app.use(routes);
    this._app.use(boomHandler);
  }

  /**
   * @return {!Promise}
   */
  async start() {
    await new Promise(fullfill => {
      this._server = this._app.listen(process.env.SERVER_PORT, fullfill);
    });
    const { port } = this._server.address();
    serverLogger.info(`Start listening ${port}`);
  }

  /**
   * @return {!Promise}
   */
  async stop() {
    const { port } = this._server.address();
    serverLogger.info(`Stop listening ${port}`);
    await new Promise(fullfill => {
      this._server.close(fullfill);
    });
  }
}

tracePublicAPI(Server);

module.exports = Server;
