package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;
import ma.ensa.volley.api.ApiService;
import ma.ensa.volley.api.ApiServiceFiliere;
import ma.ensa.volley.api.ApiServiceStudent;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AffectationRole extends AppCompatActivity {

    private Spinner studentspinner;
    private Spinner rolespinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affectation_role);

        rolespinner = findViewById(R.id.role);
        studentspinner = findViewById(R.id.student);


        // Création d'une instance Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.14:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Création d'une instance de l'interface ApiService
        ApiService apiService = retrofit.create(ApiService.class);
        ApiServiceStudent apiServiceStudent = retrofit.create(ApiServiceStudent.class);

        // Appel à l'API pour obtenir la liste des rôles
        Call<List<Role>> call = apiService.getRoles();
        Call<List<Student>> calll = apiServiceStudent.getStudents();

        call.enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(Call<List<Role>> call, Response<List<Role>> response) {
                if (response.isSuccessful()) {
                    List<Role> roles = response.body();
                    String[] names = new String[roles.size()];
                    for (int i = 0; i < roles.size(); i++) {
                        names[i] = roles.get(i).getName();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AffectationRole.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    rolespinner.setAdapter(adapter);
                } else {
                    Toast.makeText(AffectationRole.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Role>> call, Throwable t) {
                Toast.makeText(AffectationRole.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
            }
        });



        calll.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    List<Student> students = response.body();
                    String[] names = new String[students.size()];
                    for (int i = 0; i < students.size(); i++) {
                        names[i] = students.get(i).getUsername();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AffectationRole.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    studentspinner.setAdapter(adapter);
                } else {
                    Toast.makeText(AffectationRole.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(AffectationRole.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
            }
        });


    }
}