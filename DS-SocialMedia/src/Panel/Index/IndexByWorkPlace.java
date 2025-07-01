package Panel.Index;

import DataStructures.Table.Table;
import User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IndexByWorkPlace {
    private final Map<String, User> initialMap = new HashMap<>();
    JSONParser jsonParser = new JSONParser();


    public IndexByWorkPlace() {
        generateUsers();
    }

    public Map<String, User> getInitialMap() {
        return initialMap;
    }

    private void generateUsers() {
        try {
            JSONArray array = (JSONArray) jsonParser.parse(new FileReader("users.json"));
            for (Object userInFile : array) {
                JSONObject user = (JSONObject) userInFile;
                String id = (String) user.get("id");
                String name = (String) user.get("name");
                String dateOfBirth = (String) user.get("dateOfBirth");
                String universityLocation = (String) user.get("universityLocation");
                String field = (String) user.get("field");
                String workplace = (String) user.get("workplace");
                ArrayList<String> listOfSpecialities = new ArrayList<>();
                JSONArray specialties = (JSONArray) user.get("specialties");
                for (Object speciality : specialties) {
                    listOfSpecialities.add((String) speciality);
                }
                Set<Integer> setOfIDs = new HashSet<>();
                JSONArray connectionId = (JSONArray) user.get("connectionId");
                for (Object cID : connectionId) {
                    String connectionID = String.valueOf(cID);
                    setOfIDs.add(Integer.parseInt(connectionID));
                }
                initialMap.put(id, new User(Integer.parseInt(id), name, dateOfBirth, universityLocation, field, workplace, listOfSpecialities, setOfIDs));
            }
        } catch (IOException | ParseException e) {
            System.out.println("error in opening json");
        }
    }

    public void constructCustomTable(Table<String, User> table) {
        for (User user : this.initialMap.values()) {
            table.insert(user.getWorkplace(), user);
        }
    }


}
