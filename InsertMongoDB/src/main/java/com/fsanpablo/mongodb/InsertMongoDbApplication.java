package com.fsanpablo.mongodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fsanpablo.mongodb.repository.UsuarioRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

@SpringBootApplication
public class InsertMongoDbApplication  implements CommandLineRunner {
	private UsuarioRepository usuarioRepository;
	private static Logger LOG = LoggerFactory
			.getLogger(InsertMongoDbApplication.class);
	private URL url = null;
	private InputStream inputStream = null; 


	public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	//    public static String TEST_XML_STRING =
	//        "<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";

	public InsertMongoDbApplication (UsuarioRepository usrRepository) {
		this.usuarioRepository = usrRepository;
	}
	public static void main(String[] args) {
		SpringApplication.run(InsertMongoDbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("EXECUTING : command line runner");
		
		try{
			long idExc=System.currentTimeMillis();


			final File carpeta = new File("C:\\Users\\victor.gomez\\Desktop\\32\\3.2");
			//procesarXML(carpeta, idExc );
			queryMongoDB();
			
			System.out.println("Tiempo de ejecucion: " + InsertMongoDbApplication.imprimeTiempo(System.currentTimeMillis()-idExc));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				url = null;
			} catch (IOException ex) {}
		}




		/*
		try {
            JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            System.out.println(jsonPrettyPrintString);
        } catch (JSONException je) {
            System.out.println(je.toString());
        }

		 */


		/*
		Usuario user = new Usuario();
		user.setId("1");
		user.setNombre("Victor");
		user.setApellido("Gomez");

		usuarioRepository.save(user);
		 */
	}

	public void procesarXML(final File carpeta, long idExc) {
		for (final File fileXml : carpeta.listFiles()) {
			if (fileXml.isDirectory()) {
				procesarXML(fileXml, idExc);
			} else {
				System.out.println(fileXml.getName());
				insertMongo(idExc, fileXml);
			}
		}
	}

	public void insertMongo(long idExc, File fileXml) {
		@SuppressWarnings("resource")
		DB db = (new MongoClient("localhost", 27017)).getDB("Conciliacion");
		DBCollection dbCollection = db.getCollection("Usuario");
		try {			
//			url = InsertMongoDbApplication.class.getClassLoader().getResource("F10 11.xml");
//			inputStream = url.openStream();
			inputStream = new FileInputStream(fileXml);
			String xml = IOUtils.toString(inputStream);
			JSONObject xmlJSONObj = XML.toJSONObject(xml);
			String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
			//System.out.println(jsonPrettyPrintString);
			

			Reader inputString = new StringReader(jsonPrettyPrintString);
			BufferedReader reader = new BufferedReader(inputString);

			dbCollection.insert((DBObject) JSON.parse(jsonPrettyPrintString));
			System.out.println("Documento Insertado: " + fileXml.getName());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		
	}
	
	public void queryMongoDB () {
		@SuppressWarnings("resource")
		DB db = (new MongoClient("localhost", 27017)).getDB("Conciliacion");
		DBCollection dbCollection = db.getCollection("Usuario");
//		DBCursor cursor = dbCollection.find();
//		try {
//			while (cursor.hasNext()) {
//			    System.out.println(cursor.next().toString());
//		    }
//		} finally {
//			cursor.close();
//		}
		
		System.out.println("\n2. Find where number = 5");
		
		  
		BasicDBObject whereQuery = new BasicDBObject();
		  whereQuery.put("RequestCFD.Comprobante.Conceptos.Concepto.importe", 139479.32);
		  DBCursor cursor = dbCollection.find(whereQuery);
		   
		  while (cursor.hasNext()) {
		      System.out.println(cursor.next());
		  }
		  
//		  whereQuery.put("_id", "33a52bb7830b8c9b233b4fe6");
//		  DBCursor cursor3 = dbCollection.find(whereQuery);
//		  while (cursor3.hasNext()) {
//			System.out.println(cursor3.next());
//		  }
	}

	public static String imprimeTiempo(long milisegundos) {
		long min=milisegundos/60000%60;
		long seg= milisegundos/1000%60;
		return "Time:"+(milisegundos/3600000)+":"+(min>9?min:"0"+min)+":"+(seg>9?seg:"0"+seg)+":"+(milisegundos%1000)+"ms";
	}

}
