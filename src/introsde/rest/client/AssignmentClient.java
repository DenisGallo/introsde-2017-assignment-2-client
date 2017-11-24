package introsde.rest.client;

import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import introsde.rest.model.Activity;
import introsde.rest.model.ActivityType;
import introsde.rest.model.Person;

public class AssignmentClient {
	
	//configurations
	static ClientConfig clientConfig = new ClientConfig();
	static Client client = ClientBuilder.newClient(clientConfig);
	static WebTarget service = client.target(getBaseURI());
	
	//main method
	public static void main(String[] args) throws JAXBException {
		//runXML();
		//runJSON();
		long firstAndLast[]=new long[2];
		firstAndLast[0]=-1;
		firstAndLast[1]=-1;
		long id=-1;
		
		//printing the URI used in this client
		System.out.println("URL of the server being used: "+service.getUri());
		System.out.println("---------------------------------");
		System.out.println();
		
		//DATABASE INIT
		service.path("db").request().delete();
		service.path("db").request().get();
		
		//Step 3.1
		
			System.out.println("Send Request#1 (GET BASE_URL/person). Calculate how many people are in the response. If more than 4, result is OK, else is ERROR (less than 5 persons). Save into a variable id of the first person (first_person_id) and of the last person (last_person_id)");
			firstAndLast=requestOne();
			System.out.println("Saved 2 variables. Id of first person is "+firstAndLast[0]+" and id of the last person is "+firstAndLast[1]+"\n\n");
			System.out.println("---------------------------------");
			System.out.println();
		
		//Step 3.2
		
			if(firstAndLast[0]!=-1) {
				System.out.println("Send Request#2 for first_person_id. If the responses for this is 200 or 202, the result is OK.");
				requestTwo(firstAndLast[0]);
				System.out.println("---------------------------------");
				System.out.println();
			}
		
		
		
		//Step 3.3
		
			if(firstAndLast[0]!=-1) {
				System.out.println("Send Request#3 for first_person_id changing the firstname. If the responses has the name changed, the result is OK.");
				requestThree(firstAndLast[0]);
				System.out.println("You can check if the name has changed on as2/person/"+firstAndLast[0]+"\n");
				System.out.println("---------------------------------");
				System.out.println();
			}
		
		
		//Step 3.4
		
			System.out.println("Send Request#4 to create a new person (with one activity preference) using an XML. Store the id of the new person. If the answer is 201 (200 or 202 are also applicable) with a person in the body who has an ID, the result is OK.");
			id=requestFour();
			System.out.println("---------------------------------");
			System.out.println();
		
		
		//Step 3.5if(taskevaluator.charAt(4)=='1') {
			System.out.println("Send Request#5 for the person you have just created. Then send Request#1 with the id of that person. If the answer is 404, your result must be OK.");
			if(id!=-1)
				requestFive(id);
			System.out.println("---------------------------------");
			System.out.println();
		
		
		//Step 3.6
		List<ActivityType> atList=new ArrayList<ActivityType>();
		
			System.out.println("Follow now with the Request#6 (GET BASE_URL/activity_types ). If response contains more than 2 activity_types - result is OK, else is ERROR (less than 3 activity_types ). Save all activity Types into array (activity_types)");
			atList=requestSix();
			String output="ActivityType list created with the following activitytypes: ";
			for (ActivityType at:atList)
				output+=at.getName()+", ";
			System.out.println(output.substring(0, output.length()-2)+"\n");
			System.out.println("---------------------------------");
			System.out.println();
		
		//Step 3.7
		Activity a=null;
		
			System.out.println("Send Request#7 (GET BASE_URL/person/{id}/{activity_type}) for the first person you obtained at the beginning (first_person_id) and the last person (last_person_id), and for each activity type from activity_types. If no response has at least one activity the result is ERROR (no data at all) else result is OK. Store one activity_id and one activity_type. ");
			if (atList.size()!=0&&firstAndLast[0]!=-1&&firstAndLast[1]!=-1) {
				a=requestSeven(atList, firstAndLast[0], firstAndLast[1]);
				System.out.println("Successfully stored activity id "+a.getId()+" and activity type "+a.getType().getName()+"\n");
			}
			else
				System.out.println("Step 3.7 cannot be computed without executing step 3.6");
			System.out.println("---------------------------------");
			System.out.println();
		
		//Step 3.8
		
			System.out.println("Send Request#8 (GET BASE_URL/person/{id}/{activity_type}/{activity_id}) for the stored activity_id and activity_type. If the response is 200, result is OK, else is ERROR.");
			if(a!=null) {
				requestEight(a, a.getPerson().getId());
			}
			else
				System.out.println("Step 3.8 cannot be computed without executing step 3.7");
			System.out.println("---------------------------------");
			System.out.println();
		
		//Step 3.9
		
			System.out.println("Choose a activity_type from activity_types and send the request Request#7 (GET BASE_URL/person/{first_person_id}/{activity_type}) and save count value (e.g. 5 measurements). Then send Request#9 (POST BASE_URL/person/{first_person_id}/{activity_type}) with the activity specified below. Follow up with another Request#7 as the first to check the new count value. If it is 1 measure more - print OK, else print ERROR. Remember, first with JSON and then with XML as content-types");
			if(atList.size()>0&&firstAndLast[0]!=-1&&firstAndLast[1]!=-1) {
				requestNine(atList.get(2), firstAndLast[0]);
			}
			else
				System.out.println("Step 3.9 cannot be computed without computing 3.6");
			System.out.println("---------------------------------");
			System.out.println();
		
		//Step 3.10
			System.out.println("Send Request#10 using the {activity_id} or the activity created in the previous step and updating the value at will. Follow up with at Request#6 to check that the value was updated. If it was, result is OK, else is ERROR (Indicate if service not available).");
			if(a!=null) {
				requestTen(a, a.getPerson().getId(), atList);
			}
			else 
				System.out.println("Step 3.10 cannot be computed without computing 3.9 or 3.7");
			System.out.println("---------------------------------");
			System.out.println();
		
		//Step 3.11
		
			System.out.println("Send Request#11 for an activity_type, before and after dates given by your fellow student (who implemented the server). If status is 200 and there is at least one activity in the body, result is OK, else is ERROR (Indicate if service not available).");
			requestEleven(a);
			System.out.println("---------------------------------");
			System.out.println();



	}
	
	private static void printResponse(int requestnumber, String httpmethod, String url,String mediaType, String responseStatus, int httpStatus, String body) {
		System.out.println("");
		System.out.println("Request #"+requestnumber+": "+httpmethod+" "+url+" Accept: " +mediaType+" Content-type: "+mediaType);
		System.out.println("=> Result: "+responseStatus);
		System.out.println("=> HTTP Status: "+httpStatus);
		System.out.println(body);
		System.out.println("");
	}
	
	private static String printPeopleXML(List<Person> people) throws JAXBException {
		JAXBContext jaxbContext=JAXBContext.newInstance(Person.class);
		Marshaller marshaller=jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		StringWriter sw=new StringWriter();
		for(int i=0;i<people.size();i++) {
			marshaller.marshal(people.get(i), sw);
			sw.append('\n');
		}
		return sw.toString();
	}
	private static String printActivityXML(List<Activity> activityList) throws JAXBException {
		JAXBContext jaxbContext=JAXBContext.newInstance(Activity.class);
		Marshaller marshaller=jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		StringWriter sw=new StringWriter();
		for(int i=0;i<activityList.size();i++) {
			marshaller.marshal(activityList.get(i), sw);
			sw.append('\n');
		}
		return sw.toString();
	}
	
	private static String printActivityTypesXML(List<ActivityType> atList) throws JAXBException {
		JAXBContext jaxbContext=JAXBContext.newInstance(ActivityType.class);
		Marshaller marshaller=jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		StringWriter sw=new StringWriter();
		for(int i=0;i<atList.size();i++) {
			marshaller.marshal(atList.get(i), sw);
			sw.append('\n');
		}
		return sw.toString();
	}
	
	
	private static String printPersonJSON(Person person) {
		ObjectMapper mapper=new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		try {
			
			return(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private static String printActivityTypeJSON(ActivityType at) {
		ObjectMapper mapper=new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		try {
			
			return(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(at));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private static String printActivityJSON(Activity a) {
		ObjectMapper mapper=new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		try {
			
			return(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(a));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	//Step 3.1 method
	private static long[] requestOne() throws JAXBException {
		//XML
		Response response=service.path("person").request().accept(MediaType.APPLICATION_XML).get();
		List<Person> people = response.readEntity(new GenericType<List<Person>>() {});
		long[] result=new long[2];
		String body=new String();
		body=printPeopleXML(people);
		if(people.size()>4) {
			printResponse(1, "GET", "/person", "Application/XML", "OK", response.getStatus(), body);
			result[0]=people.get(0).getId();
			result[1]=people.get(people.size()-1).getId();
		}
		else {
			printResponse(1,"GET","/person", "Application/XML", "ERROR", response.getStatus(), body);
			
		}
		response=service.path("person").request().accept(MediaType.APPLICATION_JSON).get();
		people = response.readEntity(new GenericType<List<Person>>() {});
		body=new String();
		for(Person p:people) 
			body+=printPersonJSON(p)+'\n';
		if(people.size()>4) {
			printResponse(1, "GET", "/person", "Application/JSON", "OK", response.getStatus(),  body);
			return result;
		}
		else
			printResponse(1,"GET","/person", "Application/JSON", "ERROR", response.getStatus(), body);
		return null;
	}

	//Step 3.2 method
	private static void requestTwo(long id) throws JAXBException {
		//XML
		Response response=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).get();
		Person person = response.readEntity(Person.class);
		List<Person> people=new ArrayList<Person>();
		people.add(person);
		String body=printPeopleXML(people);
		if(response.getStatus()==202||response.getStatus()==200)
			printResponse(2, "GET", "/person/"+id, "Application/XML", "OK", response.getStatus(),  body);
		else
			printResponse(2, "GET", "/person/"+id, "Application/XML", "ERROR", response.getStatus(),  "");
		//JSON
		response=service.path("person/"+id).request().accept(MediaType.APPLICATION_JSON).get();
		person = response.readEntity(Person.class);
		body=printPersonJSON(person);
		if(response.getStatus()==202||response.getStatus()==200)
			printResponse(2, "GET", "/person/"+id, "Application/JSON", "OK", response.getStatus(),  body);	
		else
			printResponse(2, "GET", "/person/"+id, "Application/JSON", "OK", response.getStatus(),  "");
	}
	
	//Step 3.3 method
	private static void requestThree(long id) throws JAXBException {
		//XML
		Person oldPerson=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).get().readEntity(Person.class);
		String oldName=oldPerson.getFirstname();
		Person newPerson=new Person();
		newPerson.setFirstname("Matteo");
		Response response=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).put(Entity.xml(newPerson));
		oldPerson=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).get().readEntity(Person.class);
		String newName=newPerson.getFirstname();
		List<Person> people=new ArrayList<Person>();
		people.add(oldPerson);
		if(oldName.equals(newName))
			printResponse(3, "PUT", "person/"+id,"Application/XML", "ERROR", response.getStatus(), "<person>\n<firstname>Matteo</firstname>\n</person>");
		else
			printResponse(3, "PUT", "person/"+id, "Application/XML", "OK", response.getStatus(), "<person>\n<firstname>Matteo</firstname>\n</person>");
		//JSON
		oldPerson=service.path("person/"+id).request().accept(MediaType.APPLICATION_JSON).get().readEntity(Person.class);
		oldName=oldPerson.getFirstname();
		newPerson=new Person();
		newPerson.setFirstname("Marco");
		response=service.path("person/"+id).request().accept(MediaType.APPLICATION_JSON).put(Entity.json(newPerson));
		oldPerson=service.path("person/"+id).request().accept(MediaType.APPLICATION_JSON).get().readEntity(Person.class);
		newName=newPerson.getFirstname();
		if(oldName.equals(newName))
			printResponse(3, "PUT", "person/"+id,"Application/JSON", "ERROR", response.getStatus(), "{\n \"firstname\": \"Marco\"\n}");
		else
			printResponse(3, "PUT", "person/"+id, "Application/JSON", "OK", response.getStatus(), "{\n \"firstname\": \"Marco\"\n}");
	}
	
	//Step 3.4 method
	private static long requestFour() throws JAXBException {
		//creating Person
		Person p=new Person();
		//p.setId(53l);
		p.setFirstname("Marco");
		p.setLastname("Canale");
		p.setBirthdate("1994-09-02");
		Activity a=new Activity();
		//a.setId(153l);
		a.setName("Playing LOL");
		a.setDescription("Playing League of Legends");
		a.setPlace("At his home");
		a.setStartdate("22-11-2017 12:00:00.0");
		List<Activity> activities=new ArrayList<Activity>();
		activities.add(a);
		p.setActivities(activities);
		ActivityType at=new ActivityType();
		at.setName("Game");
		a.setType(at);
		//XML
		Response res=service.path("person").request().accept(MediaType.APPLICATION_XML).post(Entity.xml(p));
		System.out.println(res.getStatus());
		List<Person> pppp=new ArrayList<Person>();
		pppp.add(p);
		p=res.readEntity(Person.class);
		Person checkPerson=service.path("person/"+p.getId()).request().accept(MediaType.APPLICATION_XML).get().readEntity(Person.class);
		List<Person> people=new ArrayList<Person>();
		people.add(p);
		if(res.getStatus()!=500) {
			printResponse(4, "POST", "/person", "Application/XML","OK", res.getStatus(), printPeopleXML(people));
			System.out.println("You can check the created person at sdelab/person/"+p.getId()+"\n");
		}
		else
			printResponse(4, "POST", "/person", "Application/XML","ERROR", res.getStatus(), printPeopleXML(people));
		return p.getId();
	}
	
	//Step 3.5 method
	private static void requestFive(long id) {
		Response res=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).delete();
		printResponse(5, "DELETE", "/person/"+id, "Application/XML", res.getStatusInfo().toString(), res.getStatus(), "");
		res=service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).get();
		if(res.getStatus()==404) {
			printResponse(1, "GET", "/person/"+id, "Application/XML", "OK", res.getStatus(), "");
			printResponse(1, "GET", "/person/"+id, "Application/JSON", "OK", res.getStatus(), "");
		}
		else {
			printResponse(1, "GET", "/person/"+id, "Application/XML", "ERROR", res.getStatus(), "");
			printResponse(1, "GET", "/person/"+id, "Application/JSON", "ERROR", res.getStatus(), "");
		}
	}
	
	//Step 3.6 method
	private static List<ActivityType> requestSix() throws JAXBException {
		//XML
		Response res=service.path("activity_types").request().accept(MediaType.APPLICATION_XML).get();
		List<ActivityType> atList = res.readEntity(new GenericType<List<ActivityType>>() {});
		String body=printActivityTypesXML(atList);
		if (atList.size()>2) 
			printResponse(6, "GET", "/activity_types", "Application/XML", "OK", res.getStatus(), body);
		else
			printResponse(6, "GET", "/activity_types", "Application/XML", "ERROR", res.getStatus(), body);
		//JSON
		res=service.path("activity_types").request().accept(MediaType.APPLICATION_JSON).get();
		atList = res.readEntity(new GenericType<List<ActivityType>>() {});
		body=new String();
		for(ActivityType at:atList) 
			body+=printActivityTypeJSON(at)+'\n';
		if(atList.size()>2) {
			printResponse(6, "GET", "/activity_types", "Application/JSON", "OK", res.getStatus(), body);
			return atList;
		}
		else
			printResponse(6, "GET", "/activity_types", "Application/JSON", "ERROR", res.getStatus(), body);
		return null;
		
	}

	//Step 3.7 method
	private static Activity requestSeven(List<ActivityType> atList, long id0, long id1) throws JAXBException {
		Activity returnActivity = null;
		long returnId=-1;
		for(ActivityType at:atList) {
			Response res=service.path("person/"+id0+"/"+at.getName()).request().accept(MediaType.APPLICATION_XML).get();
			List<Activity> activityList=res.readEntity(new GenericType<List<Activity>>() {});
			if(activityList.size()>0) {
				returnActivity=activityList.get(0);
				returnId=id0;
			}
			res=service.path("person/"+id1+"/"+at.getName()).request().accept(MediaType.APPLICATION_XML).get();
			activityList=res.readEntity(new GenericType<List<Activity>>() {});
			if(activityList.size()>0) {
				returnActivity=activityList.get(0);
				returnId=id1;
			}
		}
		if(returnActivity!=null) {
			List<Activity> returnList=new ArrayList<Activity>();
			returnList.add(returnActivity);
			printResponse(7, "GET", "/person/"+returnId+"/"+returnActivity.getType().getName(), "Application/XML", "OK", 200, printActivityXML(returnList));
		}
		else
			printResponse(7, "GET", "/person/"+returnActivity.getPerson().getId()+"/"+returnActivity.getType().getName(), "Application/XML", "ERROR", 200, "");
		//JSON
		for(ActivityType at:atList) {
			Response res=service.path("person/"+id0+"/"+at.getName()).request().accept(MediaType.APPLICATION_JSON).get();
			List<Activity> activityList=res.readEntity(new GenericType<List<Activity>>() {});
			if(activityList.size()>0) {
				returnActivity=activityList.get(0);
				returnId=id0;
			}
			res=service.path("person/"+id1+"/"+at.getName()).request().accept(MediaType.APPLICATION_JSON).get();
			activityList=res.readEntity(new GenericType<List<Activity>>() {});
			if(activityList.size()>0) {
				returnActivity=activityList.get(0);
				returnId=id1;
			}
		}
		if(returnActivity!=null) {
			printResponse(7, "GET", "/person/"+returnId+"/"+returnActivity.getType().getName(), "Application/JSON", "OK", 200, printActivityJSON(returnActivity));
			Person p=new Person();
			p.setId(returnId);
			returnActivity.setPerson(p);
		}
		else
			printResponse(7, "GET", "/person/{id}/{activity_type}", "Application/JSON", "ERROR", 200, "");
		return returnActivity;
	}
	
	//Step 3.8 method
	private static void requestEight(Activity a, long id) throws JAXBException {
		//XML
		Response res=service.path("person/"+id+"/"+a.getType().getName()+"/"+a.getId()).request().accept(MediaType.APPLICATION_XML).get();
		Activity b=res.readEntity(Activity.class);
		List<Activity> aList=new ArrayList<Activity>();
		aList.add(b);
		String body=printActivityXML(aList);
		if(res.getStatus()==200) {
			printResponse(8, "GET", "/person/"+id+"/"+a.getType().getName()+"/"+a.getId(), "Application/XML", "OK",200, body);
		}
		else
			printResponse(8, "GET", "/person/"+id+"/"+a.getType().getName()+"/"+a.getId(), "Application/XML", "ERROR",res.getStatus(), body);
		//JSON
		res=service.path("person/"+id+"/"+a.getType().getName()+"/"+a.getId()).request().accept(MediaType.APPLICATION_JSON).get();
		b=res.readEntity(Activity.class);
		body=printActivityJSON(b);
		if(res.getStatus()==200) {
			printResponse(8, "GET", "/person/"+id+"/"+a.getType().getName()+"/"+a.getId(), "Application/JSON", "OK",200, body);
		}
		else
			printResponse(8, "GET", "/person/"+id+"/"+a.getType().getName()+"/"+a.getId(), "Application/JSON", "ERROR",res.getStatus(), body);
	}
	
	//Step 3.9 method
	private static void requestNine(ActivityType at, long id) throws JAXBException {
		//JSON
		int lengthi;
		int lengthf;
		String body="";
		Response res=service.path("person/"+id+"/"+at.getName()).request().accept(MediaType.APPLICATION_JSON).get();
		List<Activity> atList=res.readEntity(new GenericType<List<Activity>>(){});
		lengthi=atList.size();
		for(Activity a:atList)
			body+=printActivityJSON(a);
		printResponse(7, "GET", "/person/"+id+"/"+at.getName(), "Application/JSON", res.getStatusInfo().toString(), res.getStatus(), body);
		Activity newA=new Activity();
		newA.setName("Swimming");
		newA.setDescription("Swimming in the river");
		newA.setPlace("Adige river");
		newA.setType(at);
		newA.setStartdate("2017-12-28-08:50");
		body=new String();
		body=printActivityJSON(newA);
		res=service.path("person/"+id+"/"+at.getName()).request().accept(MediaType.APPLICATION_JSON).post(Entity.json(newA));
		printResponse(9, "POST", "/person/"+id+"/"+at.getName(), "Application/JSON", res.getStatusInfo().toString(), res.getStatus(), body);
		body=new String();
		res=service.path("person/"+id+"/"+at.getName()).request().accept(MediaType.APPLICATION_JSON).get();
		atList=res.readEntity(new GenericType<List<Activity>>(){});
		lengthf=atList.size();
		for(Activity a:atList)
			body+=printActivityJSON(a);
		if(lengthf==lengthi+1)
			printResponse(7, "GET", "/person/"+id+"/"+at.getName(), "Application/JSON", "OK", res.getStatus(), body);
		else
			printResponse(7, "GET", "/person/"+id+"/"+at.getName(), "Application/JSON", "ERROR", res.getStatus(), body);
		//XML
		body=new String();
		res=service.path("person/"+id+"/"+at.getName()).request().accept(MediaType.APPLICATION_XML).get();
		atList=res.readEntity(new GenericType<List<Activity>>(){});
		lengthi=atList.size();
		body=printActivityXML(atList);
		printResponse(7, "GET", "/person/"+id+"/"+at.getName(), "Application/XML", res.getStatusInfo().toString(), res.getStatus(), body);
		body=new String();
		res=service.path("person/"+id+"/"+at.getName()).request().accept(MediaType.APPLICATION_XML).post(Entity.xml(newA));
		atList=new ArrayList<Activity>();
		atList.add(newA);
		body=printActivityXML(atList);
		printResponse(9, "POST", "/person/"+id+"/"+at.getName(), "Application/XML", res.getStatusInfo().toString(), res.getStatus(), body);
		body=new String();
		res=service.path("person/"+id+"/"+at.getName()).request().accept(MediaType.APPLICATION_XML).get();
		atList=res.readEntity(new GenericType<List<Activity>>(){});
		lengthf=atList.size();
		body=printActivityXML(atList);
		if(lengthf==lengthi+1) {
			printResponse(7, "GET", "/person/"+id+"/"+at.getName(), "Application/XML", "OK", res.getStatus(), body);
			
		}
		else
			printResponse(7, "GET", "/person/"+id+"/"+at.getName(), "Application/XML", "ERROR", res.getStatus(), body);
		
	}
	
	private static void requestTen(Activity a, long id, List<ActivityType> prvsAtList) throws JAXBException {
		Activity newA=new Activity();
		ActivityType newAt=new ActivityType();
		newAt.setName("Step310test");
		newA.setType(newAt);
		Response res=service.path("person/"+id+"/"+a.getType().getName()+"/"+a.getId()).request().accept(MediaType.APPLICATION_XML).put(Entity.xml(newA));
		List<Activity> aList=new ArrayList<Activity>();
		aList.add(newA);
		String body=printActivityXML(aList);
		printResponse(10, "PUT","/person/"+id+"/"+a.getType().getName()+"/"+a.getId(), "Application/XML", res.getStatusInfo().toString(), res.getStatus(), body );
		res=service.path("activity_types").request().accept(MediaType.APPLICATION_XML).get();
		List <ActivityType>atList=res.readEntity(new GenericType<List<ActivityType>>() {});
		body=printActivityTypesXML(atList);
		if(atList.size()==prvsAtList.size()+1)
			printResponse(6, "GET", "/activity_types", "Application/XML", "OK", res.getStatus(), body);
		else
			printResponse(6, "GET", "/activity_types", "Application/XML", "ERROR", res.getStatus(), body);
		//JSON
		newAt=new ActivityType();
		newAt.setName("Step310testJSON");
		newA.setType(newAt);
		res=service.path("person/"+id+"/Step310test/"+a.getId()).request().accept(MediaType.APPLICATION_JSON).put(Entity.json(newA));
		body=printActivityJSON(newA);
		printResponse(10, "PUT","/person/"+id+"/"+a.getType().getName()+"/"+a.getId(), "Application/JSON", res.getStatusInfo().toString(), res.getStatus(), body );
		res=service.path("activity_types").request().accept(MediaType.APPLICATION_JSON).get();
		atList=res.readEntity(new GenericType<List<ActivityType>>() {});
		body=new String();
		for(ActivityType at:atList)
			body+=printActivityTypeJSON(at);
		if(atList.size()==prvsAtList.size()+2)
			printResponse(6, "GET", "/activity_types", "Application/JSON", "OK", res.getStatus(), body);
		else
			printResponse(6, "GET", "/activity_types", "Application/JSON", "ERROR", res.getStatus(), body);	
	}
	
	private static void requestEleven(Activity a) throws JAXBException {
		String before="23-11-2017 10:00:00.0";
		String after="21-11-2017 21:30:02.0";
		//XML
		Response res=service.path("person/"+a.getPerson().getId()+"/Step310testJSON").queryParam("before", before).queryParam("after", after).request().accept(MediaType.APPLICATION_XML).get();
		//Response res=service.path("person/"+a.getPerson().getId()+"/Step310testJSON?before="+before+"&after="+after).request().accept(MediaType.APPLICATION_XML).get();
		List<Activity> aList=res.readEntity(new GenericType<List<Activity>>(){});
		String body=printActivityXML(aList);
		if(aList.size()>0)
			printResponse(11, "GET", "/person/"+a.getPerson().getId()+"/Step310testJSON?before="+before+"&after="+after, "Application/XML", "OK", res.getStatus(), body);
		else
			printResponse(11, "GET", "/person/"+a.getPerson().getId()+"/Step310testJSON?before="+before+"&after="+after, "Application/XML", "ERROR", res.getStatus(), body);
		res=service.path("person/"+a.getPerson().getId()+"/Step310testJSON").queryParam("before", before).queryParam("after", after).request().accept(MediaType.APPLICATION_JSON).get();
		//res=service.path("person/"+a.getPerson().getIdPerson()+"/"+a.getType().getAtName()).queryParam("before", "2017-11-29-00:00", "after", "2017-12-01-23:59").request().accept(MediaType.APPLICATION_JSON).get();
		aList=res.readEntity(new GenericType<List<Activity>>() {});
		body=new String();
		for(Activity b:aList)
			body+=printActivityJSON(b);
		if(aList.size()>0)
			printResponse(11, "GET", "/person/"+a.getPerson().getId()+"/Step310testJSON?before="+before+"&after="+after, "Application/JSON", "OK", res.getStatus(), body);
		else
			printResponse(11, "GET", "/person/"+a.getPerson().getId()+"/Step310testJSON?before="+before+"&after="+after, "Application/JSON", "ERROR", res.getStatus(), body);
			
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://server-as2.herokuapp.com/as2").build();
	}

}