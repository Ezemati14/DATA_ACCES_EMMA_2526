package org.emma2025.fpfa.controller;

import org.emma2025.fpfa.model.entities.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LeerXML {

    public static List<Student> readStudentsXML(String filename) {

        List<Student> list = new ArrayList<>();

        try {
            File xmlFile = new File(filename);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("student");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element el = (Element) node;

                    String id = el.getElementsByTagName("idcard").item(0).getTextContent();
                    String fn = get(el, "firstname");
                    String ln = get(el, "lastname");
                    String email = get(el, "email");
                    String phone = get(el, "phone");

                    list.add(new Student(id, fn, ln, email, phone));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error leyendo XML: " + e.getMessage());
        }

        return list;
    }

    private static String get(Element el, String tag) {
        Node n = el.getElementsByTagName(tag).item(0);
        return (n != null) ? n.getTextContent() : null;
    }
}
