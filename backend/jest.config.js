module.exports = {
  testEnvironment: 'node',
  setupFiles: ['dotenv-flow/config'],
  setupFilesAfterEnv: ['./test/setup.js'],
  reporters: ['default', 'jest-junit'],
};
