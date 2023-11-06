package ma.ensa.volley.beans;


import com.google.gson.annotations.SerializedName;

public class Role {
    //@SerializedName("id")
    private long id;

    //@SerializedName("name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id){this.id=id;}
    public void setName(String name){this.name=name;}

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // Pour afficher le nom dans le Spinner
    }
}
