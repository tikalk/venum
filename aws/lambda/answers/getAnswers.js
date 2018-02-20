const settings = require('constant')
const doc = require('dynamodb-doc')
const dynamo = new doc.DynamoDB()

module.exports = (event, done) => dynamo.scan({ TableName: settings.TABLE_NAME }, (err, data) => {
  if (err) {
    done(new Error('Unable to query. Error:', JSON.stringify(err, null, 2)))
  } else {
    const ob = []
    data.Items.forEach((item) => ob.push({
      answer: item.answer,
      questionId: item.questionId,
      question: item.question,
      userId: item.userId
    }))
    done(null, ob)
  }
})
