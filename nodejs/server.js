const http = require('http');
const math = require('mathjs');

const welcome = 'Welcome to calc service\nHTTP POST your expression / (via mathjs)';
const canNotEvaluate = 'Can not evaluate';

http.createServer((request, response) => {
    if (request.method === 'POST') {
        let expr = '';
        let result = canNotEvaluate;
        request
            .on('data', (chunk) => {
                expr += chunk;
            })
            .on('end', () => {
                try {
                    result = '' + math.evaluate(expr);
                } catch (exc) {
                    result += ' ' + expr + ': ' + exc.message;
                } finally {
                    response.end(result);
                }
            })
            .on('error', (err) => {
                response.end(err);
            });
    } else {
        response.end(welcome);
    }
}).listen(8080);
