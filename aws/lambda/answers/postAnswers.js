const settings = require('constant')
const doc = require('dynamodb-doc')
const dynamo = new doc.DynamoDB()
const uuid = require('uuid')

module.exports = (event, done) => dynamo.putItem({
  TableName: settings.TABLE_NAME,
  Item: {
    id: uuid.v1(),
    answer: JSON.parse(event.body).answer,
    questionId: JSON.parse(event.body).questionId,
    question: JSON.parse(event.body).question,
    userId: JSON.parse(event.body).userId
  },
  ReturnConsumedCapacity: 'TOTAL'
}, (err, data) => {
  if (err) {
    done(new Error('Unable to query. Error:', JSON.stringify(err, null, 2)))
  } else {
    done(null, data)
  }
})
