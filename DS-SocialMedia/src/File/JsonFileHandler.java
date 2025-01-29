package File;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonFileHandler {
    private final Map<Integer, User> initialMap = new HashMap<>();
    JSONParser jsonParser = new JSONParser();
    public static int totalEdgesCounter;

    public JsonFileHandler() {
        generateUsers();
        totalEdgesCounter = 0;
    }

    public Map<Integer, User> getInitialMap() {
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
                    String connectionID = (String) cID;
                    setOfIDs.add(Integer.parseInt(connectionID));
                }
                initialMap.put(Integer.parseInt(id), new User(Integer.parseInt(id), name, dateOfBirth, universityLocation, field, workplace, listOfSpecialities, setOfIDs));
            }
        } catch (IOException | ParseException e) {
            System.out.println("error in opening json");
        }
    }

    public void constructDefaultTable(Table<Integer, User> table) {
        for (Map.Entry<Integer, User> entry : this.initialMap.entrySet()) {
            table.insert(entry.getKey(), entry.getValue());
        }
    }

    public void constructDefaultGraph(AdjMapGraph<User, Integer> graph) {
        for (Map.Entry<Integer, User> entry : this.initialMap.entrySet()) {
            graph.insertVertex(entry.getValue());
        }
        for (User user : graph.vertices()) {
            for (Integer IDs : user.getConnections()) {
                if (graph.getEdge(user, this.initialMap.get(IDs)) == null)
                    graph.insertEdge(user, this.initialMap.get(IDs), totalEdgesCounter++);
            }
        }
    }

    public void addUserToJson(User newUser) {
        JSONObject object = new JSONObject();
        object.put("id", String.valueOf(newUser.getId()));
        object.put("name", newUser.getName());
        object.put("dateOfBirth", newUser.getDateOfBirth());
        object.put("universityLocation", newUser.getUniversityLocation());
        object.put("field", newUser.getField());
        object.put("workplace", newUser.getWorkplace());
        JSONArray specialties = new JSONArray();
        specialties.addAll(newUser.getSpecialties());
        JSONArray connectionIDs = new JSONArray();
        connectionIDs.addAll(newUser.getConnections());
        object.put("specialties", specialties);
        object.put("connectionId", connectionIDs);

        JSONArray usersArray = new JSONArray();
        try (FileReader fileReader = new FileReader("users.json")) {
            Object obj = jsonParser.parse(fileReader);
            if (obj instanceof JSONArray) {
                usersArray = (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading existing user data");
        }
        usersArray.add(object);
        try (FileWriter fileWriter = new FileWriter("users.json");) {
            fileWriter.write(usersArray.toJSONString());
        } catch (IOException e) {
            System.out.println("error in writing new user's info in the json file");
        }
    }

    public void writeConnectionInJson(int firstID, int secondID) {
        try {
            JSONArray array = (JSONArray) jsonParser.parse(new FileReader("users.json"));
            for (Object userInFile : array) {
                JSONObject user = (JSONObject) userInFile;
                if (String.valueOf(user.get("id")).equals(String.valueOf(firstID)) || String.valueOf(user.get("id")).equals(String.valueOf(secondID))) {
                    JSONArray connectionId = (JSONArray) user.get("connectionId");
                    Set<Integer> IDsSet = new HashSet<>();
                    for (Object IDs : connectionId) {
                        IDsSet.add(Integer.parseInt(IDs.toString()));
                    }
                    if (String.valueOf(user.get("id")).equals(String.valueOf(firstID))) {
                        IDsSet.add(secondID);
                    }
                    if (String.valueOf(user.get("id")).equals(String.valueOf(secondID))) {
                        IDsSet.add(firstID);
                    }
                    JSONArray newConIDs = new JSONArray();
                    newConIDs.addAll(IDsSet);
                    user.put("connectionId", newConIDs);
                }
            }
            try (FileWriter fileWriter = new FileWriter("users.json")) {
                fileWriter.write(array.toJSONString());
                fileWriter.flush();
            }
        } catch (Exception e) {
            System.out.println("error in writing new connection in json");
        }
    }

    public void deleteConnectionInJson(int firstID, int secondID) {
        try {
            JSONArray array = (JSONArray) jsonParser.parse(new FileReader("users.json"));
            for (Object userInFile : array) {
                JSONObject user = (JSONObject) userInFile;
                if (String.valueOf(user.get("id")).equals(String.valueOf(firstID)) || String.valueOf(user.get("id")).equals(String.valueOf(secondID))) {
                    JSONArray connectionId = (JSONArray) user.get("connectionId");
                    Set<Integer> IDsSet = new HashSet<>();
                    for (Object IDs : connectionId) {
                        IDsSet.add(Integer.parseInt(IDs.toString()));
                    }
                    if (String.valueOf(user.get("id")).equals(String.valueOf(firstID))) {
                        IDsSet.remove(secondID);
                    }
                    if (String.valueOf(user.get("id")).equals(String.valueOf(secondID))) {
                        IDsSet.remove(firstID);
                    }
                    JSONArray newConIDs = new JSONArray();
                    newConIDs.addAll(IDsSet);
                    user.put("connectionId", newConIDs);
                }
            }
            try (FileWriter fileWriter = new FileWriter("users.json")) {
                fileWriter.write(array.toJSONString());
                fileWriter.flush();
            }
        } catch (Exception e) {
            System.out.println("error in writing new connection in json");
        }
    }
}
