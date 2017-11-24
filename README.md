## Denis Gallo (194441) | denis.gallo@studenti.unitn.it
My heroku server: https://assignment2-denisgallo.herokuapp.com/sdelab
My github repositories: 
https://github.com/DenisGallo/introsde-2017-assignment-2-server
https://github.com/DenisGallo/introsde-2017-assignment-2-client
I worked with Mattia Buffa.
Heroku server: https://server-as2.herokuapp.com/as2
His github repositories:
https://github.com/CommanderBuffin/introsde-2017-assignment-2-server
https://github.com/CommanderBuffin/introsde-2017-assignment-2-client

## Project

### Description of the code
The client has a package named **introsde.rest.client**, in the main package there is only 1 class, **AssignmentClient** which contains the main method to execute the entire client. In the **model** package there are 3 classes: person, activity, activitytype. These models are actually the models of my partner, in order to run my client on his server without conversion problems like different names and similar issues.
In the AssignmentClient class there is a main method which calls every step one by one. Other methods to print in the output the results are PrintResponse() and a printXML/printJSON for every model (these contain marshallers to actually understand requests or print responses).

### Description of the tasks
The client is calling sequentally every step specified on the assignment website, before every step it prints the step to be done, then it prints every request used on this step in the format specified on Step 2 (the very important step). On some steps even after the execution of the requests, the client prints additional information like stored variables or possible checks of a given step. Since the client was implemented to perform every step in xml and in json, I didn't have enough time to separate those requests to print 2 different logs. 
Step 3.4 was performed only in XML (would work even with json, just test it with postman).
Step 3.9 was performed in JSON first, then in XML (instead of the xml first like in the rest of the application).
Every bold request in the step description will be printed in the format of step 2, but only the main request will output the real status OK or ERROR based on the step. So if for example a step requires to create a person and then check if it exists, you will see a request# for the creation (with its real response status) and then the request of the get with my status "OK" or "ERROR". It's getting really intuitive once you read once the entire logfile.


## Execution
In order to run the client you just need to **ant execute.client** in the root folder of the application, it should print the entire application on your terminal and in the logfile (every run will overwrite the logfile)

## Additional Notes
As already mentioned, when not specified please add an id for everything you are creating (person and activity). Some requests were hard to understand and maybe i got a different interpretion on what i should do, you can ask me if something is really not working because all of my tests went fine on the server and the client of my partner is running perfectly.
