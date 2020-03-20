# litmgmt

__Literature management WebApp for SE2__
Backend: Javalin project: https://javalin.io/
Frontend: React.js: https://reactjs.org


### Build and run server in development:
`mvn clean compile exec:java`

### Assemble .jar file with all dependencies:
`mvn assembly:assembly`
-> Directory `target/`contains generated Jar file

### Build Docker container:
(ensure Jar file was built and is present)
`docker build -t test/litmgmt .`
this copies the jar file, save file and HTML directory into the container

### Run Docker container:
- Attached mode: `docker run --rm -t -i -p 80:80 test/litmgmt:latest`
- Detached mode: `docker run -d --name litmgmt -p 80:80 test/litmgmt:latest`
-- Stop and remove: `docker stop litmgmt`, `docker rm litmgmt`

### Some CURL commands for testing:

#### Register user:
`curl -X POST -H "Content-Type: application/json" -d '{"name":"john","password":"wayne", "email":"john.wayne@web.de"}' `http://localhost/api/register

#### Log in user:
`curl -X POST -H "Content-Type: application/json" -d '{"name":"john","password":"wayne"}'`http://localhost/api/login

#### Get all collections of that user:
`curl -H "Accept: application/json" -H "Authorization: Bearer 1F13D80A228FCAC0EA32ACAF8BD47E3BBBD66F28"` http://localhost/api/collections
(replace token)

#### Create a new collection:
`curl -X POST -H "Accept: application/json" -H "Authorization: Bearer 1F13D80A228FCAC0EA32ACAF8BD47E3BBBD66F28" -d '{"name":"col01"}'`http://localhost/api/collections

#### Create an entry in a collection:

`curl -X POST -H "Accept: application/json" -H "Authorization: Bearer 1F13D80A228FCAC0EA32ACAF8BD47E3BBBD66F28" -d '{"citeKey":"myCiteRef2018", "entryType":"article", "fields":[{"fieldType": "author", "value":"Jim Raynor"}, {"fieldType":"address", "value":"Tarsonis"}]}'` http://localhost/api/collections/0/entries

_For a complete list of operations, see Swagger API specification!_
_Further CURL commands can be found in the REST endpoint documentation, see "JavalinServer.java", l.71 ff._
