import express from 'express';
import {
  evaluateMiddleware,
  welcomeMiddleware,
} from './middleware/middleware.js';
import bodyParser from 'body-parser';

const app = express();
app.use(bodyParser.text({ type: 'text/plain' }));
app.get('*any', welcomeMiddleware);
app.post('*any', evaluateMiddleware);

const port = 8080;
app.listen(port, '0.0.0.0', () => {});
