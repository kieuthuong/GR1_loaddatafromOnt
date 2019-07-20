package com.example.demo.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Person;
import com.example.demo.repository.PersonRepository;

@Service
public class PersonServiceImpl {
	static String SOURCE = "http://www.semanticweb.org/admin/ontologies/2019/2/untitled-ontology-20";
	static String NS = SOURCE + "#";

	@Autowired
	private PersonRepository personRepository;

	public ArrayList<Person> findAll() {
		ArrayList<Person> listPerson = new ArrayList<>();
		String ontologyInFile = "E:\\\\HocJava\\\\Java1\\\\demo\\\\person_rdf.owl";
		OntModel model;
		model = ModelFactory.createOntologyModel();
		InputStream ontologyIn = FileManager.get().open(ontologyInFile);
		try {
			model.read(ontologyIn, "N3");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// String ontologyOutFile = "E:\\\\HocJava\\\\Java1\\\\demo\\person_rdf2.owl";
		OntDocumentManager dm = model.getDocumentManager();
		dm.addAltEntry(SOURCE, "file:" + ontologyInFile);
		OntClass person = model.getOntClass(NS + "Person");

		String req = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX :<http://www.semanticweb.org/admin/ontologies/2019/2/untitled-ontology-20#>" + "SELECT *"
				+ "WHERE {" + "?p :Name ?name ." + "OPTIONAL" + "{" + "?p :Age ?age ." + "OPTIONAL" + "{"
				+ "?p :Email ?email " + "}" + "}" + "}";

		Query query = QueryFactory.create(req);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet res = qe.execSelect();
//		ResultSetFormatter.out(System.out, res, query);

		// thực hiện map vào đây
		while (res.hasNext()) {
			String name = "";
			Integer age = 0;
			String email = "";

			QuerySolution soln = res.nextSolution();
			if (soln.get("name").toString() != null)
				name = soln.get("name").toString();
			if (soln.get("name").toString() != null)
				name = soln.get("name").toString();
			if (soln.getLiteral("age") != null)
				age = soln.getLiteral("age").getInt();
			if (soln.get("email") != null)
				email = soln.get("email").toString();
			listPerson.add(new Person(name, age, email));
		}
		return listPerson;
	}

	public void writeDataToOnt() {
		List<Person> listPerson = personRepository.findAll();
		String ontologyInFile = "E:\\HocJava\\Java1\\demo\\person_rdf.owl";
		String ontologyOutFile = "E:\\\\HocJava\\\\Java1\\\\demo\\person_rdf5.owl";
		OntModel model;
		model = ModelFactory.createOntologyModel();
		OntDocumentManager dm = model.getDocumentManager();
		dm.addAltEntry(SOURCE, "file:" + ontologyInFile);
		InputStream ontologyIn = FileManager.get().open(ontologyInFile);
		try {
			model.read(ontologyIn, "N3");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		OntClass ont = model.getOntClass(NS + "Person");
		for (Person person : listPerson) {
			if (person == null) {
				System.out.println("dmcc");
			}
			Individual p4 = ont.createIndividual(NS + person.getId());
			Property name = model.getOntProperty(NS + "Name");
			p4.addProperty(name, person.getName());
			try {
				OutputStream out = new FileOutputStream(ontologyOutFile);
				model.write(out);
				out.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}