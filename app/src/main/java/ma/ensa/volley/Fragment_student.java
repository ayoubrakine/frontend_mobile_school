package ma.ensa.volley;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Fragment_student extends Fragment {

    private Button add ;
    private Button show ;
    private Button byfiliere ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        add = view.findViewById(R.id.add);
        show = view.findViewById(R.id.show);
        byfiliere = view.findViewById(R.id.byfiliere);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddStudent.class); // Utilisation de getActivity() pour obtenir le contexte de l'activité
                startActivity(intent);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShowStudents.class); // Utilisation de getActivity() pour obtenir le contexte de l'activité
                startActivity(intent);
            }
        });

        byfiliere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudentByFiliere.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
