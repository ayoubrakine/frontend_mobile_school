package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.StudentAdapter;
import ma.ensa.volley.adapter.StudentByFiliereAdapter;
import ma.ensa.volley.api.ApiService;
import ma.ensa.volley.api.ApiServiceFiliere;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentByFiliere extends AppCompatActivity {

    Spinner filierespinner;
    private ListView listView;
    private List<Student> students;
    List<Filiere> filieres;

    private StudentByFiliereAdapter studentAdapter;
    private RequestQueue requestQueue;

    private String loadUrl = "http://192.168.1.13:8080/api/student";
    String insertUrl = "http://192.168.1.13:8080/api/student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_by_filiere);

        filierespinner = findViewById(R.id.byfiliere);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.13:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServiceFiliere apiServiceFiliere = retrofit.create(ApiServiceFiliere.class);

        Call<List<Filiere>> callFilieres = apiServiceFiliere.getFilieres();


        callFilieres.enqueue(new Callback<List<Filiere>>() {
            @Override
            public void onResponse(Call<List<Filiere>> call, retrofit2.Response<List<Filiere>> response) {
                if (response.isSuccessful()) {
                    filieres = response.body();
                    String[] names = new String[filieres.size()];
                    for (int i = 0; i < filieres.size(); i++) {
                        names[i] = filieres.get(i).getLibelle();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentByFiliere.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filierespinner.setAdapter(adapter);
                } else {
                    Toast.makeText(StudentByFiliere.this, "Erreur de chargement des filières", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Filiere>> call, Throwable t) {
                Toast.makeText(StudentByFiliere.this, "Erreur de chargement des filières", Toast.LENGTH_SHORT).show();
            }
        });


        listView = findViewById(R.id.list);
        students = new ArrayList<>();
        studentAdapter = new StudentByFiliereAdapter(students, this);
        listView.setAdapter(studentAdapter);

        requestQueue = Volley.newRequestQueue(this);
        //
        //int selectedFilierePosition = filierespinner.getSelectedItemPosition();
        //Filiere selectedFiliere = filieres.get(selectedFilierePosition);
        //long selectedFiliereId = selectedFiliere.getId();
        //
        //String loadbyfUrl = "http://192.168.1.13:8080/api/student?id="+selectedFiliereId;

        filierespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Filiere selectedFiliere = filieres.get(position);
                long selectedFiliereId = selectedFiliere.getId();

                String loadbyfUrl = "http://192.168.1.13:8080/api/student/filiere/" + selectedFiliereId;

                JsonArrayRequest request = new JsonArrayRequest(
                        Request.Method.GET, loadbyfUrl, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                students.clear();

                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject studentJson = response.getJSONObject(i);
                                        ma.ensa.volley.beans.Student student = new Student();
                                        student.setId(studentJson.getInt("id"));
                                        student.setUsername(studentJson.getString("username"));
                                        student.setName(studentJson.getString("name"));
                                        student.setEmail(studentJson.getString("email"));
                                        student.setPhone(studentJson.getInt("phone"));

                                        // Create Filiere object and set its details
                                        JSONObject filiereJson = studentJson.getJSONObject("filiere");
                                        Filiere filiere = new Filiere();
                                        filiere.setId(filiereJson.getInt("id"));
                                        filiere.setCode(filiereJson.getString("code"));
                                        filiere.setLibelle(filiereJson.getString("libelle"));
                                        student.setFiliere(filiere);

                                        // Assuming roles are a JSON array inside each student object
                                        JSONArray rolesArray = studentJson.getJSONArray("roles");
                                        List<Role> roles = new ArrayList<>();
                                        for (int j = 0; j < rolesArray.length(); j++) {
                                            JSONObject roleJson = rolesArray.getJSONObject(j);
                                            Role role = new Role();
                                            role.setId(roleJson.getInt("id"));
                                            role.setName(roleJson.getString("name"));

                                            roles.add(role);
                                        }
                                        student.setRoles(roles);

                                        students.add(student);
                                    }


                                    studentAdapter.notifyDataSetChanged();
                                    Log.d("List_Students", "Données chargées avec succès. Nombre d'étudiants : " + students.size());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("List_Students", "Erreur lors de la récupération des données JSON : " + e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(StudentByFiliere.this, "Erreur de chargement des étudiants", Toast.LENGTH_SHORT).show();
                                Log.e("List_Students", "Erreur de réseau : " + error.getMessage());
                            }
                        });

                requestQueue.add(request);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(StudentByFiliere.this, "Pas de student dans la filiere", Toast.LENGTH_SHORT).show();
            }
            });
    }

}


