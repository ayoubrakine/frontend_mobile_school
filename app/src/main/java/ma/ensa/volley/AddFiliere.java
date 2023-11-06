package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddFiliere extends AppCompatActivity implements View.OnClickListener {


    private EditText code, libelle;
    private Button bnAdd;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:8080/api/v1/filieres";
    //192.168.1.14
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_filiere);

        code = findViewById(R.id.code);
        libelle = findViewById(R.id.libelle);
        bnAdd = findViewById(R.id.bnAdd);

        bnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("code", code.getText().toString() );
            jsonBody.put("libelle", libelle.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response+"");
                // Créez un AlertDialog de succès
                AlertDialog.Builder builder = new AlertDialog.Builder(AddFiliere.this);
                builder.setTitle("Succès");
                builder.setMessage("Ajout réussi");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Fermez le dialogue si l'utilisateur appuie sur OK
                        dialog.dismiss();

                        // Réinitialisez les champs d'entrée
                        code.setText("");
                        libelle.setText("");

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

    }
}

