#logtarget

A demo log file sink.

## Components
*logtarget-common* - classes used across modules

*logtarget-parse-lambda* - lambda triggered by files written to s3 which generates a log summary
 
*logtarget-webapp* - Spring Boot Web Application which persists and retrieves log files 


##Usage
*logtarget-parse-lambda* is deployed to AWS and will trigger when appropriately named files are
written to the s3 bucket *raw-log-target* (the same summary will be generated irrespective of the
contents of the logfile)

*logtarget-webapp* can be started locally using the bootrun target from an IDE,
or by calling **./gradlew logtarget-webapp:bootrun** from the command line

*logtarget-webapp* can be packaged as a war and deployed to Tomcat, etc...

Locally logtarget can by accessed at the following endpoints:

*PUT http://localhost:8080/logtarget/upload*

The request body will be persisted

'Content-Type' defaults to 'text/plain' unless otherwise specified
'Content-Length' is required

A *fileId* will be returned

*GET http://localhost:8080/logtarget/getlog/{fileId}*

The log file contents will be return if found, a 404 otherwise

*GET http://localhost:8080/logtarget/batteryPercentage/{fileId}*

A JSON representation of the battery percentages will be returned 
if the summary is found with request status 200.

If the summary can't be found, but the raw log file is found, 
request status 202 will be returned, indicating that the file
is still being processed, and that the caller should try again.

If neither the summary nor the raw log file is found, request
status 404 will be returned, indicating that no record of the 
provided *fileId* was found, and the request should not be
retried.



## Integration test
*not yet implemented*