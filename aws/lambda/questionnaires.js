'use strict';

console.log('Loading function');

const doc = require('dynamodb-doc');
const dynamo = new doc.DynamoDB();


/**
 * This lamda will service the questionnaires collection
 *
 *
 */
exports.handler = (event, context, callback) => {
    //console.log('Received event:', JSON.stringify(event, null, 2));

    const done = (err, res) => callback(null, {
        statusCode: err ? '400' : '200',
        body: err ? err.message : JSON.stringify(res),
        headers: {
            'Content-Type': 'application/json',
        },
    });

    console.log('runing..');

    switch (event.httpMethod) {
        case 'DELETE':
            console.log('DELETE..');
            //dynamo.deleteItem(JSON.parse(event.body), done);
            break;
        case 'GET':
            console.log('GET..');
            const questionnair = event['queryStringParameters']['questionnair'];
            console.log('questionnair: ' + questionnair);
            var params = {
                TableName : "questionnaires",
                IndexName: "qId-index",
                ProjectionExpression:"question, qname, answers",
                KeyConditionExpression: "qId = :id",
                ExpressionAttributeValues: {
                    ":id": questionnair
                }
            };

            dynamo.query(params, function(err, data) {
                if (err) {
                    console.error("Unable to query. Error:", JSON.stringify(err, null, 2));
                    done(new Error("Unable to query. Error:", JSON.stringify(err, null, 2)));
                } else {
                    console.log("Query succeeded.");
                    const ob = [];
                    data.Items.forEach(function(item) {
                        console.log('item.answers: ' + item.answers);
                        const answersList = [];
                        answersList.push(item.answers);
                        ob.push({ "name" : item.qname, "question" : item.question, "answers" : answersList});
                    });

                    done(null,JSON.stringify(ob));
                }
            });
            break;
        case 'POST':
            console.log('POST..');
            //dynamo.putItem(JSON.parse(event.body), done);
            break;
        case 'PUT':
            console.log('PUT..');
            //dynamo.updateItem(JSON.parse(event.body), done);
            break;
        default:
        //done(new Error(`Unsupported method "${event.httpMethod}"`));
    }

};
