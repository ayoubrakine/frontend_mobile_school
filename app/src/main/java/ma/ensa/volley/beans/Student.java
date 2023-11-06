package ma.ensa.volley.beans;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private int id ;
    private String username;
    private String name;
    private int phone;
    private String email;
    private String password;
    private Filiere filiere;
    private List<Role> roles;






    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }


    public List<Role> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>(); // Initialiser la liste si elle est null
        }
        return roles;
    }


    public void setRoles(List<Role> roles) {
        if (this.roles == null) {
            this.roles = new ArrayList<>(); // Initialiser la liste si elle est null
        } else {
            this.roles.clear(); // Vider la liste existante
        }
        this.roles.addAll(roles); // Ajouter les nouveaux rôles
    }
}
