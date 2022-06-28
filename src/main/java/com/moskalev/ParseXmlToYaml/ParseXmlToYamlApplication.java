package com.moskalev.ParseXmlToYaml;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import com.github.underscore.lodash.Xml;
import java.io.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;
import com.github.underscore.Xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@SpringBootApplication
public class ParseXmlToYamlApplication {

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
		SpringApplication.run(ParseXmlToYamlApplication.class, args);

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		InputStream inputStream = new FileInputStream(new File("sample.xml"));
		Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
		StringWriter stw = new StringWriter();
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.transform(new DOMSource(doc), new StreamResult(stw));


		String[] s =stw.toString().split("<\\D?tns:TechRegs>");//разбиваю на части по тегу <tns:TechRegs> и <\tns:TechRegs>

		String newstring=s[0]+s[2];

		newstring.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1"); //удаление пустых строк



		Object result = Xml.fromXml(newstring);
		Yaml yaml = new Yaml();
		StringWriter stringWriter = new StringWriter();
		yaml.dump(result, stringWriter);
		String yamll = stringWriter.toString();

		File output=new File("output.yml");
		FileWriter writer = new FileWriter(output);

		writer.write(yamll);
		writer.flush();
		writer.close();
	}

}
