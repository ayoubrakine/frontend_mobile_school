package ma.ensa.volley.api;

import java.util.List;
import ma.ensa.volley.beans.Student;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceStudent {

    @GET("api/student")
    Call<List<Student>> getStudents();

}
