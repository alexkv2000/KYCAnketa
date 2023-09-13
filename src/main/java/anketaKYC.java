
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class anketaKYC {

    public static class KycConnectApplication {

        public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException, SAXException, TransformerException {
            // https://mkyong.com/logging/slf4j-logback-tutorial/
            Logger logger = LoggerFactory.getLogger(KycConnectApplication.class);
            logger.info("Start app");

            String username = "api";
            String pwd = "szuWm7^8S184S05%FNy!";

            String ID_KYC = "29861";
            String URL_Anketa = "https://kyc-compliance.ru/api/download_questionnaires.php?ID=" + ID_KYC;


            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", getBasicAuthenticationHeader(username, pwd))
                    .uri(URI.create(URL_Anketa))
                    .GET()
                    .build();
            System.out.println(request.toString());

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("{}", response.body());
            logger.debug("End APP");

            //

//            Document document = convertStringToXml(response.body());
//            System.out.println(document.toString());
            //
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response.body())));
//
//
//            NodeList nodeList = doc.getElementsByTagName("employee");
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                Element element = (Element) node;
//                if (Node.ELEMENT_NODE == node.getNodeType()) {
//                    //Element element = (Element) node;
//                    if (Node.ELEMENT_NODE == node.getNodeType()) {
//                        //Element element = (Element) node;
//                        if (element.getAttribute("id").equals("2")) {
//                            element.getElementsByTagName("tool").item(0).setTextContent("124562");
//                        }
//                    }
//                }
//            }
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(ID_KYC +".xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, streamResult);

        }

    }

    private static Document convertStringToXml(String xmlString) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            //dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder builder = dbf.newDocumentBuilder();

            return builder.parse(new InputSource(new StringReader(xmlString)));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

    }
    private static String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
