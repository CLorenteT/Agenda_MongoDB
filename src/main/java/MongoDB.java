import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential;  
public class MongoDB { 
   
   public static void main( String args[] ) {  
      
      MongoClient mongo = new MongoClient("localhost" , 27017 );
      Scanner s = new Scanner(System.in);

      MongoCredential credential = MongoCredential.createCredential("sampleUser", "agenda",
                                                    "password".toCharArray());
      
      MongoDatabase database = mongo.getDatabase("agenda");
      MongoCollection<Document> collection = database.getCollection("agenda");

      System.out.println("Quina acció voldrieu realitzar? (consultar, afegir, modificar o eliminar): ");
      String accio = s.next();

      if (accio.equals("consultar")) {
    	  System.out.print("Voleu visualitzar un llistat dels contactes o voleu visualitzar les dades d'un contacte en concret? (general / concret): ");
    	  String llistat = s.next();
    	  
    	  if (llistat.equals("general")) {
    		  FindIterable<Document> iterDoc = collection.find();
    	  	  int i = 1;     

    	      Iterator it = iterDoc.iterator();
    	      
    	      System.out.println("+----------------------------+\n|         CONTACTES          |\n+-----+----------------------+\n|  Nº |         NOM          |");
    	  	  while (it.hasNext()) {
    	  		  Document d=(Document)it.next();
    	  		  System.out.printf("+-----+----------------------+\n| %3d | %20s |\n",
    	  				  i, d.get("nom"));
    	  		  i++;
    	  	  }
    	  	  System.out.println("+-----+----------------------+");
    	  } 
    	  else if (llistat.equals("concret")) {
    		  BasicDBObject whereQuery = new BasicDBObject();
    		  
    		  System.out.print("Introdueix el telefon del contacte: ");
    		  int telefon = s.nextInt();
    		  
    	  	  whereQuery.put("telefon",telefon);
    	      FindIterable<Document> doc = collection.find(whereQuery);

    		  if(doc.iterator().hasNext()) {
    			  System.out.printf("+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n|         NOM          |            DIRECCIÓ            |                 CORREU ELECTRÒNIC                  |               TELEFONS              |\n+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n| %20s | %30s | %50s | %35s |\n+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+",
    					  doc.iterator().next().get("nom"), doc.iterator().next().get("direccio"), doc.iterator().next().get("email"), doc.iterator().next().get("telefon"));
    		  }
    		  else 
    			  System.out.println("El contacte no existeix o el paràmetre és incorrecte.");
    	  }
      }
      else if (accio.equals("afegir")) {
    	  System.out.println("Nom: ");
    	  String nom = s.next();
    	  System.out.println("Direcció: ");
    	  String direccio = s.next();
    	  
    	  Document contacte = new Document("nom",nom).append("direccio", direccio);
    	  
    	  System.out.println("Quants emails vols afegir? ");
    	  int n_emails = s.nextInt();
    	  
    	  if (n_emails > 0) {
    		  String[] emails = new String[n_emails];
    		  
    		  for(int i = 0; i < emails.length; i++) {
    			  System.out.println("Email: ");
    			  emails[i] = s.next();
    		  }
    		  contacte.append("email", Arrays.toString(emails));
    	  }
    	  else 
    		  System.out.println("Nombre d'emails no acceptat.");
    	  
    	  System.out.println("Quants telèfons vols afegir? ");
    	  int n_telefons = s.nextInt();
    	  
    	  if (n_telefons > 0) {
    		  int[] telefons = new int[n_telefons];
    		  
    		  for(int i = 0; i < telefons.length; i++) {
    			  System.out.println("Telèfon: ");
    			  telefons[i] = s.nextInt();
    		  }
    		  contacte.append("telefon", Arrays.toString(telefons));
    	  }
    	  else 
    		  System.out.println("Nombre de telèfons no acceptat.");


    	  collection.insertOne(contacte);
      }


      /*
  	  //.....................................
  	  // Obtener un documento concreto
  	  /*BasicDBObject whereQuery = new BasicDBObject();
  	  whereQuery.put("Titulo","MongoDB");
      FindIterable<Document> doc = collection.find(whereQuery);  
	  if(doc.iterator().hasNext())
		  System.out.println("Autor del documento encontrado:"+((Document)doc.iterator().next()).get("autor"));
	  else 
		  System.out.println("Documento no encontrado");	 

  	  //.....................................
      // Actualizar un campo de un documento 
      collection.updateOne(Filters.eq("Titulo","MongoDB"),Updates.set("numero",150));    
      
  	  //.....................................
      // Eliminar un documento 
      collection.deleteOne(Filters.eq("Titulo", "Java"));*/ 
   } 
}