package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.api.ApiService;
import ma.ensa.volley.api.ApiServiceFiliere;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddStudent extends AppCompatActivity implements View.OnClickListener {

    List<Filiere> filieres;
    List<Role> roles;
    private Spinner filierespinner;
    private Spinner rolespinner;

    private EditText username, name, phone, email, password;
    private Button bnAdd;
    private RequestQueue requestQueue;
    private String insertUrl = "http://192.168.1.13:8080/api/student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        filierespinner = findViewById(R.id.filiere);
        rolespinner = findViewById(R.id.role);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        bnAdd = findViewById(R.id.bnAdd);

        bnAdd.setOnClickListener(this);

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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddStudent.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    rolespinner.setAdapter(adapter);
                } else {
                    Toast.makeText(AddStudent.this, "Erreur de chargement des rôles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Role>> call, Throwable t) {
                Toast.makeText(AddStudent.this, "Erreur de chargement des rôles", Toast.LENGTH_SHORT).show();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddStudent.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filierespinner.setAdapter(adapter);
                } else {
                    Toast.makeText(AddStudent.this, "Erreur de chargement des filières", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Filiere>> call, Throwable t) {
                Toast.makeText(AddStudent.this, "Erreur de chargement des filières", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (validateFields()) {
            JSONObject jsonBody = new JSONObject();
            try {
                JSONObject filiereObject = new JSONObject();
                long selectedFiliereId = filieres.get(filierespinner.getSelectedItemPosition()).getId();
                filiereObject.put("id", selectedFiliereId);

                JSONArray rolesArray = new JSONArray();
                JSONObject roleObject = new JSONObject();
                long selectedRoleId = roles.get(rolespinner.getSelectedItemPosition()).getId();
                roleObject.put("id", selectedRoleId);
                rolesArray.put(roleObject);

                jsonBody.put("username", username.getText().toString());
                jsonBody.put("password", password.getText().toString());
                jsonBody.put("name", name.getText().toString());
                jsonBody.put("email", email.getText().toString());
                jsonBody.put("phone", phone.getText().toString());
                jsonBody.put("filiere", filiereObject);
                jsonBody.put("roles", rolesArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    insertUrl, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("resultat", response + "");
                    // Créez un AlertDialog de succès
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddStudent.this);
                    builder.setTitle("Succès");
                    builder.setMessage("Ajout réussi");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Fermez le dialogue si l'utilisateur appuie sur OK
                            dialog.dismiss();

                            // Réinitialisez les champs d'entrée
                            username.setText("");
                            name.setText("");
                            email.setText("");
                            password.setText("");
                            username.setText("");
                            phone.setText("");

                        }
                    });

                    // Affichez le dialogue
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Erreur", error.toString());
                }
            });
            requestQueue.add(request);
        } else {
            Toast.makeText(AddStudent.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        return !username.getText().toString().isEmpty()
                && !password.getText().toString().isEmpty()
                && !name.getText().toString().isEmpty()
                && !email.getText().toString().isEmpty()
                && !password.getText().toString().isEmpty()
                && !phone.getText().toString().isEmpty();
    }
}