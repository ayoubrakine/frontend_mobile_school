package ma.ensa.volley;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddRole extends AppCompatActivity implements View.OnClickListener {


    private EditText namerole;
    private Button bnAdd;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2:8080/api/v1/roles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);

        namerole = findViewById(R.id.role);

        bnAdd = findViewById(R.id.bnAdd);

        bnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", namerole.getText().toString());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddRole.this);
                builder.setTitle("Succès");
                builder.setMessage("Ajout réussi");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Fermez le dialogue si l'utilisateur appuie sur OK
                        dialog.dismiss();

                        // Réinitialisez les champs d'entrée
                        namerole.setText("");

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