package User;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private int id;
    private String name;
    private String dateOfBirth;
    private String universityLocation;
    private String field;
    private String workplace;
    private List<String> specialties;
    private Set<Integer> connections;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUniversityLocation() {
        return universityLocation;
    }

    public void setUniversityLocation(String universityLocation) {
        this.universityLocation = universityLocation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public Set<Integer> getConnections() {
        return connections;
    }

    public void setConnections(Set<Integer> connections) {
        this.connections = connections;
    }

    public User(int id, String name, String dateOfBirth, String universityLocation, String field, String workplace, List<String> specialties, Set<Integer> connections) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.universityLocation = universityLocation;
        this.field = field;
        this.workplace = workplace;
        this.specialties = specialties;
        this.connections = new HashSet<>(connections);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", universityLocation='" + universityLocation + '\'' +
                ", field='" + field + '\'' +
                ", workplace='" + workplace + '\'' +
                ", specialties=" + specialties +
                ", connections=" + connections +
                '}';
    }

    public void addNewConnection(User user, AdjMapGraph<User, Integer> graph, JsonFileHandler fileHandler, Table<Integer, User> table) {
        this.connections.add(user.id);
        user.connections.add(this.id);
        graph.insertEdge(this, user, JsonFileHandler.totalEdgesCounter++);
        fileHandler.writeConnectionInJson(getId(), user.getId());
        table.delete(this.id);
        table.delete(user.id);
        table.insert(this.id, this);
        table.insert(user.id, user);
    }

    public void deleteExistingConnection(User user, AdjMapGraph<User, Integer> graph, JsonFileHandler fileHandler, Table<Integer, User> table) {
        this.connections.remove(user.id);
        user.connections.remove(this.id);
        graph.removeEdge(graph.getEdge(this, user));
        fileHandler.deleteConnectionInJson(getId(), user.getId());
        table.delete(this.id);
        table.delete(user.id);
    }
}
