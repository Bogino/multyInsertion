import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.text.SimpleDateFormat;
;

public class XMLHandler extends DefaultHandler {

    private Voter voter;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
   // private Map<Voter, Integer> voterCounts;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


        try {
            int countVoter = 0;

            if (qName.equals("voter") && countVoter < 1){
                //Date birthDay = birthDayFormat.parse(attributes.getValue("birthDay"));
                DBConnection.countVoter(attributes.getValue("name"), attributes.getValue("birthDay"));
                //voter = new Voter(attributes.getValue("name"), birthDay);
                countVoter++;
            }
//            else if (qName.equals("visit") && countVisit < 1){
//                int count = voterCounts.getOrDefault(voter, 0);
//                voterCounts.put(voter, count + 1);
//            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals("voter")){
            voter = null;
        }
    }

//    public void printDuplicatedVoters(){
//        voterCounts.entrySet().stream()
//                .filter(voterIntegerEntry -> voterIntegerEntry.getValue() >= 2)
//                .forEach(voterIntegerEntry -> System.out.printf("Нарушитель: %s - %d%n",voterIntegerEntry.getKey(),voterIntegerEntry.getValue()));
//
////            }
////        }
//    }
}
