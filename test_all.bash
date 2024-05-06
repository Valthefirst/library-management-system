#!/usr/bin/env bash
#
# Sample usage:
#   ./test_all.bash start stop
#   start and stop are optional
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
# When not in Docker
#: ${HOST=localhost}
#: ${PORT=7000}

# When in Docker
: ${HOST=localhost}
: ${PORT=8080}

#array to hold all our test data ids
allTestPatronIds=()
allTestLoanIds=()

function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

#have all the microservices come up yet?
function testUrl() {
    url=$@
    if curl $url -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
          return 1
    fi;
}

#prepare the test data that will be passed in the curl commands for posts and puts
function setupTestdata() {

##CREATE SOME PATRON TEST DATA - THIS WILL BE USED FOR THE POST REQUEST
#
body=\
'{
  "firstName":"Christine",
  "lastName":"Gerard",
  "emailAddress":"christine@gmail.com",
  "contactMethodPreference": "EMAIL",
  "streetAddress": "99 Main Street",
  "city":"Montreal",
  "province": "Quebec",
  "country": "Canada",
  "postalCode": "H3A 1A1",
  "phoneNumbers": [
    {
      "type": "HOME",
      "number": "514-555-5555"
    },
    {
      "type": "WORK",
      "number": "514-555-5556"
    }
  ]
}'
    recreatePatron 1 "$body"
#

##CREATE SOME LOAN TEST DATA - THIS WILL BE USED FOR THE POST REQUEST

body=\
'{
     "status": "ACTIVE",
     "bookISBN": [
         9780545791328,
         9780765308481
     ]
 }
'
    recreatePatronLoan 1 "$body" "cc9c2c7f-afc9-46fb-8119-17158e54d02f"

} #end of setupTestdata


#USING CUSTOMER TEST DATA - EXECUTE POST REQUEST
function recreatePatron() {
    local testId=$1
    local aggregate=$2

    #create the patron and record the generated patronId
    patronId=$(curl -X POST http://localhost:8080/api/v1/patrons -H "Content-Type:
    application/json" --data "$aggregate" | jq '.patronId')
    allTestPatronIds[$testId]=$patronId
    echo "Added Patron with patronId: ${allTestPatronIds[$testId]}"
    echo "Patron ID created: $patronId"
}

#USING SALE TEST DATA - EXECUTE POST REQUEST
function recreatePatronLoan() {
    local testId=$1
    local aggregate=$2
    local patronId=$3

    #create the loan and record the generated loanId
    loanId=$(curl -X POST http://$HOST:$PORT/api/v1/patrons/$patronId/loans -H "Content-Type:
    application/json" --data "$aggregate" | jq '.loanId')
    allTestLoanIds[$testId]=loanId
    echo "Added Customer with loanId: ${allTestLoanIds[$testId]}"
}


#don't start testing until all the microservices are up and running
function waitForService() {
    url=$@
    echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
        n=$((n + 1))
        if [[ $n == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
}

#start of test script
set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

#try to delete an entity/aggregate that you've set up but that you don't need. This will confirm that things are working
waitForService curl -X DELETE http://$HOST:$PORT/api/v1/patrons/cc9c2c7f-afc9-46fb-8119-17158e54d02f

setupTestdata

#EXECUTE EXPLICIT TESTS AND VALIDATE RESPONSE
#
##verify that a get all patrons works
echo -e "\nTest 1: Verify that a get all patrons works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/patrons -s"
assertEqual 10 $(echo $RESPONSE | jq ". | length")
#
#
## Verify that a normal get by id of earlier posted patron works
echo -e "\nTest 2: Verify that a normal get by id of earlier posted patron works"
echo "Patron ID: ${allTestPatronIds[0]}"
echo "Patron ID: ${allTestPatronIds[1]}"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/patrons/${allTestPatronIds[1]} '${body}' -s"
assertEqual ${allTestPatronIds[1]} $(echo $RESPONSE | jq .patronId)
assertEqual "\"Christine\"" $(echo $RESPONSE | jq ".firstName")
#
#
## Verify that an update of an earlier posted patron works - put at api-gateway has no response body
echo -e "\nTest 3: Verify that an update of an earlier posted patron works"
body=\
'{
  "firstName":"Christine",
  "lastName":"Gerard",
  "emailAddress":"christine@gmail.com",
  "streetAddress": "99 Main Street",
  "city":"Montreal",
  "province": "Quebec",
  "country": "Canada",
  "postalCode": "H3A 1A1",
  "phoneNumbers": [
    {
      "type": "HOME",
      "number": "514-555-5555"
    },
    {
      "type": "WORK",
      "number": "514-555-5556"
    }
  ]
}'
assertCurl 200 "curl -X PUT http://$HOST:$PORT/api/v1/patrons/${allTestPatronIds[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
#
#
## Verify that a delete of an earlier posted patron works
echo -e "\nTest 4: Verify that a delete of an earlier posted patron works"
assertCurl 204 "curl -X DELETE http://$HOST:$PORT/api/v1/patrons/${allTestPatronIds[1]} -s"
#
#
## Verify that a 404 (Not Found) status is returned for a non existing patronId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 5: Verify that a 404 (Not Found) error is returned for a get patron request with a non existing patronId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d7 -s"
##
##
### Verify that a 422 (Unprocessable Entity) status is returned for an invalid patronId (c3540a89-cb47-4c96-888e-ff96708db4d)
#echo -e "\nTest 6: Verify that a 422 (Unprocessable Entity) status is returned for a get patron request with an invalid patronId"
#assertCurl 422 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d -s"
#
#
## Verify that all get patron loans works
echo -e "\nTest 7: Verify that all get patron loans works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d/loans -s"
assertEqual 2 $(echo $RESPONSE | jq ". | length")
#
#
## Verify that a patron loan by patronId and loanId works
echo -e "\nTest 8: Verify that a patron loan by patronId and loanId works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d/loans/${allTestLoanIds[1]} -s"
assertEqual ${allTestLoanIds[1]} $(echo $RESPONSE | jq .loanId)
assertEqual "\"SALE_OFFER""" $(echo $RESPONSE | jq ".saleStatus")
assertEqual "\"Alick""" $(echo $RESPONSE | jq ".customerFirstName")
assertEqual "\"Vilma""" $(echo $RESPONSE | jq ".employeeFirstName")
assertEqual "\"Nissan""" $(echo $RESPONSE | jq ".vehicleMake")
#
#
## Verify that an update of an earlier posted loan works - put at api-gateway has no response body
echo -e "\nTest 8: Verify that an update of an earlier posted loan works - put at api-gateway has no response body"
body=\
'{
         "inventoryId": "d846a5a7-2e1c-4c79-809c-4f3f471e826d",
         "vin": "JN8AS5MTXDW375966",
         "employeeId": "e5913a79-9b1e-4516-9ffd-06578e7af261",
         "salePrice": 66500,
         "currency": "CAD",
         "saleStatus": "PURCHASE_COMPLETED",
         "saleOfferDate": "2024-04-10",
         "financingAgreementDetails": {
             "numberOfMonthlyPayments": 60,
             "monthlyPaymentAmount": 858.3400000000000318323145620524883270263671875,
             "downPaymentAmount": 15000,
             "paymentCurrency": "CAD"
         },
         "warranty": {
             "warrantyEndDate": "2029-04-10",
             "warrantyTerms": "5 years"
         }
     }'
assertCurl 200 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d/loans/${allTestLoanIds[1]} -H \"Content-Type: application/json\" -d '${body}'-s"
assertEqual ${allTestLoanIds[1]} $(echo $RESPONSE | jq .loanId)
assertEqual "\"PURCHASE_COMPLETED""" $(echo $RESPONSE | jq ".saleStatus")
assertEqual "\"Alick""" $(echo $RESPONSE | jq ".customerFirstName")
assertEqual "\"Vilma""" $(echo $RESPONSE | jq ".employeeFirstName")
assertEqual "\"Nissan""" $(echo $RESPONSE | jq ".vehicleMake")
#
#
## Verify that a delete of an earlier posted patron loan works
echo -e "\nTest 9: Verify that a delete of an earlier posted patron loan works"
assertCurl 204 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d/loans/${allTestLoanIds[1]} -s"
#
#
## Verify that a 404 (Not Found) status is returned for a non existing patronId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 10: Verify that a 404 (Not Found) error is returned for a get patron loan request with a non existing patronId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d7/loans/${allTestLoanIds[1]} -s"
#
#
## Verify that a 404 (Not Found) status is returned for a non existing loanId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 11: Verify that a 404 (Not Found) error is returned for a get patron loan request with a non existing loanId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/patrons/c3540a89-cb47-4c96-888e-ff96708db4d/loans/c3540a89-cb47-4c96-888e-ff96708db4d7 -s"
#
#


if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi