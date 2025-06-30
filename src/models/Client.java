package models;

public class Client {
    // Atributos
    private int id;
    private String name;
    private String lastName;
    protected String phone;
    protected String email;

    // Constructor con ID (para cargar desde BD)
    public Client(int id, String name, String lastName, String phone, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    // Constructor sin ID (para crear nuevos clientes)
    public Client(String name, String lastName, String phone, String email) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método toString para representación fácil
    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", nombre: '" + name + '\'' +
                ", apellido: '" + lastName + '\'' +
                ", telefono: '" + phone + '\'' +
                ", email: '" + email + '\'' +
                '}';
    }
}