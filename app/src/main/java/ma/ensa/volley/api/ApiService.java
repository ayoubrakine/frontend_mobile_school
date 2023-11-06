package ma.ensa.volley.api;

import java.util.List;

import ma.ensa.volley.beans.Role;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/v1/roles")
    Call<List<Role>> getRoles();
}
