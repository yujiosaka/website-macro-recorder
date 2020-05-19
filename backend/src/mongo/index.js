const mongoose = require('mongoose');

// Make Mongoose use `findOneAndUpdate()`. Note that this option is `true`
// by default, you need to set it to false.
mongoose.set('useFindAndModify', false);
mongoose.set('useCreateIndex', true);
mongoose.set('autoIndex', process.env.MONGO_AUTO_INDEX === 'true');
mongoose.set('debug', process.env.LOG_LEVEL === 'debug');

const connection = mongoose.createConnection(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

const macro = new mongoose.Schema({
  instanceId: { type: String, required: true },
  name: { type: String, required: true },
  url: { type: String, required: true },
  scheduleFrequency: { type: Number, required: true },
  scheduleHour: { type: Number, required: true },
  scheduleMinute: { type: Number, required: true },
  notifySuccess: { type: Boolean, required: true },
  notifyFailure: { type: Boolean, required: true },
  acceptLanguage: { type: String, required: true },
  userAgent: { type: String, required: true },
  events: [{
    _id: false,
    name: { type: String, required: true },
    xPath: { type: String, required: true },
    targetType: { type: String, required: true },
    value: { type: String, required: true },
  }],
  createdAt: { type: Date, default: Date.now },
  updatedAt: { type: Date, default: Date.now },
});

macro.index({ instanceId: 1, _id: 1 }, { unique: true });

const Macro = connection.model('Macro', macro);

module.exports = { connection, Macro };
