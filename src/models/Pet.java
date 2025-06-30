package models;

public class Pet {
    // Atributos
    private int id;
    private String name;
    private String species;
    private String race;
    private String gender;
    private int age;
    private int idClient;

    // Constructor con ID (para cargar desde BD)
    public Pet(int id, String name, String species, String race, String gender, int age, int idClient) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.race = race;
        this.gender = gender;
        this.age = age;
        this.idClient = idClient;
    }

    // Constructor sin ID (para crear nuevas mascotas)
    public Pet(String name, String species, String race, String gender, int age, int idClient) {
        this.name = name;
        this.species = species;
        this.race = race;
        this.gender = gender;
        this.age = age;
        this.idClient = idClient;
    }

    // Getters y Setters
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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", nombre:'" + name + '\'' +
                ", especie:'" + species + '\'' +
                ", raza:'" + race + '\'' +
                ", genero:'" + gender + '\'' +
                ", edad:" + age +
                ", id cliente:" + idClient +
                '}';
    }
}