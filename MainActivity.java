package com.christine.intrusiondetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PRODUCT_URL = "http://<ip_address>/MyApi/api.php";

    RecyclerView recyclerView;
    ProductAdapter adapter;

    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding some items to our list
      loadProducts();


    }

    private void loadProducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray products =  new JSONArray(response);

                            for(int i=0; i<products.length(); i++){
                                JSONObject productObject = products.getJSONObject(i);

                                int id = productObject.getInt("id");
                                String title = productObject.getString("title");
                                String shortdesc = productObject.getString("shortdesc");
//                                double price = productObject.getDouble("price"); rating, price,
//                                double rating = productObject.getDouble("rating");
                                String image = productObject.getString("image");

                                Product product = new Product(id, title,shortdesc,image);
                                productList.add(product);
                            }

                            adapter = new ProductAdapter(MainActivity.this, productList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
    });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
