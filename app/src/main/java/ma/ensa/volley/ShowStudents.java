package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.FiliereAdapter;
import ma.ensa.volley.adapter.StudentAdapter;
import ma.ensa.volley.api.ApiService;
import ma.ensa.volley.api.ApiServiceFiliere;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowStudents extends AppCompatActivity {

    List<Filiere> filieres;
    List<Role> roles;
    private Spinner filiereSpinner;
    private Spinner roleSpinner;

    private List<Student> students;
    private ListView listView;
    private StudentAdapter studentAdapter;
    private RequestQueue requestQueue;
    private String loadUrl = "http://192.168.1.13:8080/api/student";
    //String insertUrl = "http://10.0.2.2:8080/api/student";

    //String insertUrl = "http://10.0.2.2:8080/api/v1/roles";
    String insertUrl = "http://192.168.1.13:8080/api/student";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students);

        listView = findViewById(R.id.list);
        students = new ArrayList<>();
        studentAdapter = new StudentAdapter(students, this);
        listView.setAdapter(studentAdapter);

        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, loadUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject studentJson = response.getJSONObject(i);
                                Student student = new Student();
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
                        Toast.makeText(ShowStudents.this, "Erreur de chargement des étudiants", Toast.LENGTH_SHORT).show();
                        Log.e("List_Students", "Erreur de réseau : " + error.getMessage());
                    }
                });

        requestQueue.add(request);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Student student = students.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowStudents.this);
                builder.setTitle("Options pour l'étudiant");
                builder.setMessage("Que voulez-vous faire avec cet étudiant ?");

                // Bouton "Modifier"
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder modifyBuilder = new AlertDialog.Builder(ShowStudents.this);
                        modifyBuilder.setTitle("Modification");
                        View dialogView = getLayoutInflater().inflate(R.layout.update_student, null);
                        modifyBuilder.setView(dialogView);

                        filiereSpinner = dialogView.findViewById(R.id.filiere);
                        roleSpinner = dialogView.findViewById(R.id.role);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://192.168.1.13:8080/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ApiServiceFiliere apiServiceFiliere = retrofit.create(ApiServiceFiliere.class);
                        ApiService apiService = retrofit.create(ApiService.class);

                        Call<List<Role>> callRoles = apiService.getRoles();
                        Call<List<Filiere>> callFilieres = apiServiceFiliere.getFilieres();

                        callRoles.enqueue(new Callback<List<Role>>() {
                            @Override
                            public void onResponse(Call<List<Role>> call, retrofit2.Response<List<Role>> response) {
                                if (response.isSuccessful()) {
                                    roles = response.body();
                                    String[] names = new String[roles.size()];
                                    for (int i = 0; i < roles.size(); i++) {
                                        names[i] = roles.get(i).getName();
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowStudents.this, android.R.layout.simple_spinner_item, names);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    roleSpinner.setAdapter(adapter);
                                } else {
                                    Toast.makeText(ShowStudents.this, "Erreur de chargement des rôles", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Role>> call, Throwable t) {
                                Toast.makeText(ShowStudents.this, "Erreur de chargement des rôles", Toast.LENGTH_SHORT).show();
                            }
                        });

                        callFilieres.enqueue(new Callback<List<Filiere>>() {
                            @Override
                            public void onResponse(Call<List<Filiere>> call, retrofit2.Response<List<Filiere>> response) {
                                if (response.isSuccessful()) {
                                    filieres = response.body();
                                    String[] names = new String[filieres.size()];
                                    for (int i = 0; i < filieres.size(); i++) {
                                        names[i] = filieres.get(i).getLibelle();
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ShowStudents.this, android.R.layout.simple_spinner_item, names);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    filiereSpinner.setAdapter(adapter);
                                } else {
                                    Toast.makeText(ShowStudents.this, "Erreur de chargement des filières", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Filiere>> call, Throwable t) {
                                Toast.makeText(ShowStudents.this, "Erreur de chargement des filières", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Récupération des éléments de la vue pour la modification
                        TextView idTextView = dialogView.findViewById(R.id.idstudent);
                        EditText usernameEditText = dialogView.findViewById(R.id.username);
                        EditText nameEditText = dialogView.findViewById(R.id.name);
                        EditText emailEditText = dialogView.findViewById(R.id.email);
                        EditText phoneEditText = dialogView.findViewById(R.id.phone);
                        EditText passwordEditText = dialogView.findViewById(R.id.password);

                        idTextView.setText(String.valueOf(student.getId()));
                        usernameEditText.setText(student.getUsername());
                        nameEditText.setText(student.getName());
                        emailEditText.setText(student.getEmail());
                        phoneEditText.setText(String.valueOf(student.getPhone()));
                        passwordEditText.setText(student.getPassword());

                        modifyBuilder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newUsername = usernameEditText.getText().toString();
                                String newName = nameEditText.getText().toString();
                                String newEmail = emailEditText.getText().toString();
                                String newPhone = phoneEditText.getText().toString();
                                String newPassword = passwordEditText.getText().toString();
                                long idd = student.getId();

                                //int selectedRolePosition = roleSpinner.getSelectedItemPosition();
                                //Role selectedRole = roles.get(selectedRolePosition);
                               // long selectedRoleId = selectedRole.getId();

                                //int selectedFilierePosition = filiereSpinner.getSelectedItemPosition();
                                //Filiere selectedFiliere = filieres.get(selectedFilierePosition);
                                //long selectedFiliereId = selectedFiliere.getId();
                                ///



                                ////
                                // Mise à jour des données de l'étudiant

                                JSONObject jsonBody = new JSONObject();
                                try {
                                    JSONObject filiereObject = new JSONObject();
                                    int selectedFilierePosition = filiereSpinner.getSelectedItemPosition();
                                    Filiere selectedFiliere = filieres.get(selectedFilierePosition);
                                    long selectedFiliereId = selectedFiliere.getId();
                                    filiereObject.put("id", selectedFiliereId);

                                    JSONArray rolesArray = new JSONArray();
                                    JSONObject roleObject = new JSONObject();
                                    int selectedRolePosition = roleSpinner.getSelectedItemPosition();
                                    Role selectedRole = roles.get(selectedRolePosition);
                                    long selectedRoleId = selectedRole.getId();
                                    roleObject.put("id", selectedRoleId);
                                    rolesArray.put(roleObject);


                                    jsonBody.put("id", idd);
                                    jsonBody.put("username", newUsername);
                                    jsonBody.put("name", newName);
                                    jsonBody.put("email", newEmail);
                                    jsonBody.put("phone", newPhone);
                                    jsonBody.put("password", newPassword);

                                    jsonBody.put("filiere", filiereObject);
                                    jsonBody.put("roles", rolesArray);

                                    students.set(position, student); // Mettre à jour l'étudiant modifié dans la liste 'students'


                                    studentAdapter.notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JsonObjectRequest request = new JsonObjectRequest(
                                        Request.Method.PUT,
                                        insertUrl + "/" + idd, // URL pour la modification
                                        jsonBody,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("Modification", "Modification réussie : " + response.toString());


                                                studentAdapter.notifyDataSetChanged();
                                                Toast.makeText(ShowStudents.this, "Étudiant modifié avec succès", Toast.LENGTH_SHORT).show();


                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("Modification", "Erreur lors de la modification : " + error.toString());
                                                // Gérer les erreurs
                                            }
                                        }
                                );

                                requestQueue.add(request);
                                dialog.dismiss();
                            }
                        });

                        modifyBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        modifyBuilder.show();
                    }
                });



                // Bouton "Supprimer"
                builder.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Afficher un autre AlertDialog pour la confirmation de suppression
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(ShowStudents.this);
                        confirmBuilder.setTitle("Confirmation de suppression");
                        confirmBuilder.setMessage("Voulez-vous vraiment supprimer cet étudiant ?");

                        confirmBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                long studentID = student.getId();
                                String deleteUrl = "http://192.168.1.13:8080/api/student/" + studentID;

                                //demande POST pour supprimer l'étudiant
                                StringRequest deleteRequest = new StringRequest(
                                        Request.Method.DELETE,
                                        deleteUrl,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Suppression réussie, supprimez également de la liste locale
                                                students.remove(student);

                                                // Mettez à jour l'adaptateur de la liste

                                                StudentAdapter studentAdapter = new StudentAdapter(students, ShowStudents.this);
                                                listView.setAdapter(studentAdapter);
                                                studentAdapter.notifyDataSetChanged();

                                                Toast.makeText(ShowStudents.this, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ShowStudents.this, "Erreur lors de la suppression de l'étudiant", Toast.LENGTH_SHORT).show();
                                                Log.e("List_Students", "Erreur de réseau lors de la suppression : " + error.getMessage());
                                            }
                                        }
                                );

                                // Ajouter la demande de suppression à la file d'attente de Volley
                                requestQueue.add(deleteRequest);
                            }
                        });

                        confirmBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        confirmBuilder.show();
                    }
                });

                builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
