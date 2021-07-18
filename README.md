# Item Repository Spring Boot Application #

### Project Overview ###

This project contains an unfinished implementation of a simple repository for Items.

The Project consist of 6 REST APIs:

1. List All items with id, name and description details

curl -X GET \
  http://localhost:9443/app/api/1.0/items \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 7a88abad-5eae-16f2-8697-3610353c8bf3'
  
 2. List Item Detail based on id
 3. 
 curl -X GET \
  http://localhost:9443/app/api/1.0/items/1 \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 0e163d68-7064-e757-5eae-b4284d570eae'
  
  3. List Items detail based on desciption Name
  curl -X GET \
  'http://localhost:9443/app/api/1.0/items/description?name=Noval' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 732c0476-be9f-e3e4-984b-e0b57bf6d2ff'
  
  4. Add New item to the List
  
  curl -X POST \
  http://localhost:9443/app/api/1.0/items \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ccda8263-4685-712d-4ff6-320ab6c22728' \
  -d '{
	"name": "New Item added"
}'

5. Update Item based on Item id

curl -X PUT \
  http://localhost:9443/app/api/1.0/items/1 \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 1f7c4785-f85d-8488-64ee-e20e844efe12' \
  -d '{
	"description":[
		{
			"name":"Noval"
		},
		{
			"name":"Highyly fantastic"
		}
		]
}'

6. Delete Item based on item id

curl -X DELETE \
  http://localhost:9443/app/api/1.0/items/2 \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 5dff7264-9d63-705f-e949-4d7399c31a31'
