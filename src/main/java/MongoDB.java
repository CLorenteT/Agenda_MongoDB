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

	public static void main(String args[]) {

		MongoClient mongo = new MongoClient("localhost", 27017);
		Scanner s = new Scanner(System.in);

		MongoCredential credential = MongoCredential.createCredential("sampleUser", "agenda", "password".toCharArray());

		MongoDatabase database = mongo.getDatabase("agenda");
		MongoCollection<Document> collection = database.getCollection("agenda");

		boolean cont = true;

		while (cont) {

			System.out.println("Quina acció voldrieu realitzar? (consultar, afegir, modificar o eliminar): ");
			String accio = s.next();

			if (accio.equals("consultar")) {
				System.out.print(
						"Voleu visualitzar un llistat dels contactes o voleu visualitzar les dades d'un contacte en concret? (general / concret): ");
				String llistat = s.next();

				if (llistat.equals("general")) {
					FindIterable<Document> iterDoc = collection.find();
					int i = 1;

					Iterator it = iterDoc.iterator();

					System.out.println(
							"+----------------------------+\n|         CONTACTES          |\n+-----+----------------------+\n|  Nº |         NOM          |");
					while (it.hasNext()) {
						Document d = (Document) it.next();
						System.out.printf("+-----+----------------------+\n| %3d | %20s |\n", i, d.get("nom"));
						i++;
					}
					System.out.println("+-----+----------------------+");
				} else if (llistat.equals("concret")) {
					BasicDBObject whereQuery = new BasicDBObject();

					System.out.print("Introdueix el telefon del contacte: ");
					int telefon = s.nextInt();

					whereQuery.put("telefon", telefon);
					FindIterable<Document> doc = collection.find(whereQuery);

					if (doc.iterator().hasNext()) {
						System.out.printf(
								"+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n|         NOM          |            DIRECCIÓ            |                 CORREU ELECTRÒNIC                  |               TELEFONS              |\n+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n| %20s | %30s | %50s | %35s |\n+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+",
								doc.iterator().next().get("nom"), doc.iterator().next().get("direccio"),
								doc.iterator().next().get("email"), doc.iterator().next().get("telefon"));
					} else
						System.out.println("El contacte no existeix o el paràmetre és incorrecte.");
				}
			} else if (accio.equals("afegir")) {
				System.out.println("Nom: ");
				String nom = s.next();
				System.out.println("Direcció: ");
				String direccio = s.next();

				Document contacte = new Document("nom", nom).append("direccio", direccio);

				System.out.println("Quants emails vols afegir? ");
				int n_emails = s.nextInt();

				if (n_emails > 0) {
					List<String> emails = new ArrayList<String>();

					for (int i = 0; i < n_emails; i++) {
						System.out.println("Email: ");
						emails.add(s.next());
					}
					contacte.append("email", emails);
				} else
					System.out.println("Nombre d'emails no acceptat.");

				System.out.println("Quants telèfons vols afegir? ");
				int n_telefons = s.nextInt();

				if (n_telefons > 0) {
					List<Integer> telefons = new ArrayList<Integer>();

					for (int i = 0; i < n_telefons; i++) {
						System.out.println("Telèfon: ");
						telefons.add(s.nextInt());
					}
					contacte.append("telefon", telefons);
				} else
					System.out.println("Nombre de telèfons no acceptat.");

				collection.insertOne(contacte);
				System.out.println("Contacte afegit correctament!");

			} else if (accio.equals("modificar")) {
				BasicDBObject whereQuery = new BasicDBObject();

				System.out.print("Introdueix el telefon del contacte: ");
				int telefon = s.nextInt();

				whereQuery.put("telefon", telefon);
				FindIterable<Document> doc = collection.find(whereQuery);

				if (doc.iterator().hasNext()) {
					System.out.printf(
							"+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n|         NOM          |            DIRECCIÓ            |                 CORREU ELECTRÒNIC                  |               TELEFONS              |\n+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n| %20s | %30s | %50s | %35s |\n+----------------------+--------------------------------+----------------------------------------------------+-------------------------------------+\n",
							doc.iterator().next().get("nom"), doc.iterator().next().get("direccio"),
							doc.iterator().next().get("email"), doc.iterator().next().get("telefon"));

					System.out.println("Introdueix el nou nom: (Escriu null si no voleu modificar la nom)");
					String nom = s.next();

					collection.updateOne(Filters.eq("telefon", telefon), Updates.set("nom", nom));

					System.out.println("Direcció: (Escriu null si no voleu modificar la direcció)");
					String direccio = s.next();

					collection.updateOne(Filters.eq("telefon", telefon), Updates.set("direccio", direccio));

					System.out.println("Quants emails vols afegir? ");
					int n_emails = s.nextInt();

					if (n_emails > 0) {
						List<String> emails = new ArrayList<String>();

						for (int i = 0; i < n_emails; i++) {
							System.out.println("Email: ");
							emails.add(s.next());
						}

						collection.updateOne(Filters.eq("telefon", telefon), Updates.set("email", emails));

					} else if (n_emails == 0)
						System.out.println("Els emails no han estat modificats");

					else
						System.out.println("Nombre d'emails no acceptat.");

					System.out.println("Quants telèfons vols afegir? ");
					int n_telefons = s.nextInt();

					if (n_telefons > 0) {
						List<Integer> telefons = new ArrayList<Integer>();

						for (int i = 0; i < n_telefons; i++) {
							System.out.println("Telèfon: ");
							telefons.add(s.nextInt());
						}

						collection.updateOne(Filters.eq("telefon", telefon), Updates.set("telefon", telefons));

					} else if (n_telefons == 0)
						System.out.println("Els telefons no han estat modificats");

					else
						System.out.println("Nombre de telèfons no acceptat.");

					System.out.println("Contacte modificat correctament!");

				} else
					System.out.println("El contacte no existeix o el paràmetre és incorrecte.");

			} else if (accio.equals("eliminar")) {
				System.out.print("Introdueix el telèfon del contacte: ");
				int telefon = s.nextInt();
				System.out.println("Estas segur d'eliminar el contacte amb telèfon " + telefon + " ?");
				String conf = s.next();

				if (conf.equals("si")) {
					collection.deleteOne(Filters.eq("telefon", telefon));
					System.out.println("Contacte eliminat correctament!");
				} else if (conf.equals("no"))
					System.out.println("El contacte no s'ha eliminat!");

				else
					System.out.println("Error durant la confirmació, el contacte no s'ha eliminat!");
			}
			
			System.out.println("Voleu continuar?");
			String conf = s.next();
			
			if (conf.equals("si")) 
				cont = true;
			
			else 
				cont = false;
		}
	}
}