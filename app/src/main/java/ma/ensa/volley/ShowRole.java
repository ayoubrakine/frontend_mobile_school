package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

import ma.ensa.volley.adapter.RoleAdapter;
import ma.ensa.volley.beans.Role;


public class ShowRole extends AppCompatActivity {

    private List<Role> roles;
    private ListView listView;
    private RoleAdapter roleAdapter;
    private RequestQueue requestQueue;
    private String loadUrl = "http://192.168.1.13:8080/api/v1/roles";

    String insertUrl = "http://10.0.2.2:8080/api/v1/roles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_role);

        listView = findViewById(R.id.list);
        roles = new ArrayList<>();
        roleAdapter = new RoleAdapter(roles, this);
        listView.setAdapter(roleAdapter);

        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, loadUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject roleJson = response.getJSONObject(i);
                                Role role = new Role();
                                role.setId(roleJson.getInt("id"));
                                role.setName(roleJson.getString("name")); // Ajout du nom du rôle

                                roles.add(role);
                            }

                            roleAdapter.notifyDataSetChanged(); // Notification du changement des données

                            // Logs de débogage
                            Log.d("List_Roles", "Données chargées avec succès. Nombre de rôles : " + roles.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Logs de débogage en cas d'erreur
                            Log.e("List_Roles", "Erreur lors de la récupération des données JSON : " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowRole.this, "Erreur de chargement des rôles", Toast.LENGTH_SHORT).show();
                        // Logs de débogage en cas d'erreur
                        Log.e("List_Roles", "Erreur de réseau : " + error.getMessage());
                    }
                });

        requestQueue.add(request);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Role role = roles.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowRole.this);
                builder.setTitle("Options pour le role");
                builder.setMessage("Que voulez-vous faire avec cet role ?");

                // Bouton "Modifier"
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder modifyBuilder = new AlertDialog.Builder(ShowRole.this);
                        modifyBuilder.setTitle("Modification");
                        View dialogView = getLayoutInflater().inflate(R.layout.update_role, null);
                        modifyBuilder.setView(dialogView);

                        TextView idTextView = dialogView.findViewById(R.id.idrole);
                        EditText nameEditText = dialogView.findViewById(R.id.name);

                        idTextView.setText(String.valueOf(role.getId()));
                        nameEditText.setText(role.getName());

                        modifyBuilder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newNom = nameEditText.getText().toString();
                                long idd = role.getId(); // Récupération de l'ID

                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("id", idd);
                                    jsonBody.put("name", newNom);
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

                                                // Mettez à jour l'adaptateur de la liste pour refléter les modifications
                                                role.setName(newNom);
                                                roleAdapter.notifyDataSetChanged();
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
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(ShowRole.this);
                        confirmBuilder.setTitle("Confirmation de suppression");
                        confirmBuilder.setMessage("Voulez-vous vraiment supprimer cet étudiant ?");

                        confirmBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                long etudiantId = role.getId();
                                String deleteUrl = "http://192.168.1.13:8080/api/v1/roles/" + etudiantId;

                                //demande POST pour supprimer l'étudiant
                                StringRequest deleteRequest = new StringRequest(
                                        Request.Method.DELETE,
                                        deleteUrl,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Suppression réussie, supprimez également de la liste locale
                                                roles.remove(role);

                                                // Mettez à jour l'adaptateur de la liste

                                                RoleAdapter roleadapter = new RoleAdapter(roles, ShowRole.this);
                                                listView.setAdapter(roleadapter);
                                                roleadapter.notifyDataSetChanged();

                                                Toast.makeText(ShowRole.this, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ShowRole.this, "Erreur lors de la suppression de l'étudiant", Toast.LENGTH_SHORT).show();
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
