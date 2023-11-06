package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ma.ensa.volley.R;

public class Students extends AppCompatActivity {

    private Button add ;
    private Button show ;
    private Button byfiliere ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student);

        add = findViewById(R.id.add);
        show = findViewById(R.id.show);
        byfiliere = findViewById(R.id.byfiliere);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Students.this, AddStudent.class);
                startActivity(intent);
            }
        });


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Students.this, ShowStudents.class);
                startActivity(intent);
            }
        });


        byfiliere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Students.this, StudentByFiliere.class);
                startActivity(intent);
            }
        });



    }
}