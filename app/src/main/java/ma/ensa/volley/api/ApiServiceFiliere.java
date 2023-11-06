package ma.ensa.volley.api;

import java.util.List;

import ma.ensa.volley.beans.Filiere;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceFiliere {

    @GET("api/v1/filieres")
    Call<List<Filiere>> getFilieres();
}
