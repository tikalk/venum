'use strict'
const getAnswers = require('getAnswers')
const postAnswers = require('postAnswers')
console.log('Loading function')

/**
 * Demonstrates a simple HTTP endpoint using API Gateway. You have full
 * access to the request and response payload, including headers and
 * status code.
 *
 * To scan a DynamoDB table, make a GET request with the TableName as a
 * query string parameter. To put, update, or delete an item, make a POST,
 * PUT, or DELETE request respectively, passing in the payload to the
 * DynamoDB API as a JSON body.
 */
exports.handler = (event, context, callback) => {
    // console.log('Received event:', JSON.stringify(event, null, 2));

  const done = (err, res) => callback(null, {
    statusCode: err ? '400' : '200',
    body: err ? err.message : JSON.stringify(res),
    headers: {
      'Content-Type': 'application/json'
    }
  })

  switch (event.httpMethod) {
    case 'GET':
      console.log('GET..')
      getAnswers(event, done)
      break
    case 'POST':
      console.log('POST..')
      postAnswers(event, done)
      break
    default:
      done(new Error(`Unsupported method "${event.httpMethod}"`))
  }
}
