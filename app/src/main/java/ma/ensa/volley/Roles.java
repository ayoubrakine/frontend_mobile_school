package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Roles extends AppCompatActivity {

    private Button add ;
    private Button show ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_role);

        add = findViewById(R.id.add);
        show = findViewById(R.id.show);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Roles.this, AddRole.class);
                startActivity(intent);
            }
        });


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Roles.this, ShowRole.class);
                startActivity(intent);
            }
        });



    }
}