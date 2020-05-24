const { connection } = require('../src/mongo');

beforeAll(async () => {
  await connection.dropDatabase();
});

afterAll(async () => {
  await connection.dropDatabase();
  await connection.close();
});
