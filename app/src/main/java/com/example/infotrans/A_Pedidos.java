package com.example.infotrans;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class A_Pedidos extends AppCompatActivity {

    EditText eCv,eN,eNC,eCant;

    FirebaseDatabase firebase;

    DatabaseReference refDBp;

    Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__pedidos);

        inic_components();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void inic_components(){

        eCv = findViewById(R.id.cv_ref);

        eN = findViewById(R.id.N_Producto);

        eNC = findViewById(R.id.N_Cliente);

        eCant = findViewById(R.id.C_Pedido);

        bundle = getIntent().getExtras();

        firebase = FirebaseDatabase.getInstance();

        refDBp = firebase.getReference("Pedidos").child(Objects.requireNonNull(bundle.getString("Nombre")));
    }
    public void aña_Pedidos(View view) {

        if (comprobacion().trim().isEmpty()) {
            Date fecha = new Date(Calendar.getInstance().getTimeInMillis());

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

            String fec_ped = formato.format(fecha);

            Pedidos p = new Pedidos(eCv.getText().toString(), eN.getText().toString(), eNC.getText().toString(),
                    bundle.getString("Nombre"), fec_ped, Integer.parseInt(eCant.getText().toString()),"Tramitando el pedido");

            refDBp.child(p.getClave_ref()).setValue(p);

            Intent back=new Intent(this,Pedidos_main.class);
            Bundle bun=new Bundle();
            bun.putString("Nombre",bundle.getString("Nombre"));
            bun.putString("Tipo",bundle.getString("Tipo"));
            back.putExtras(bun);
            startActivity(back);

        } else {
            Toast.makeText(A_Pedidos.this, comprobacion(), Toast.LENGTH_LONG).show();
        }

    }

    public String comprobacion() {
        String msg = "";
        if (eCv.getText().toString().isEmpty()) {
            msg += "Clave de referencia obligatoria";
        }
        if (eN.getText().toString().isEmpty()) {
            msg += "\n Nombre de producto obligatorio";
        }
        if (eNC.getText().toString().isEmpty()) {
            msg += "\n Nombre de cliente obligatorio";
        }
        try {
            int cant = Integer.parseInt(eCant.getText().toString());
            if (cant <= 0) msg += "\nCantidad no válida";
        } catch (NumberFormatException e) {
            msg += "\nCantidad no válida";
        }
        return msg;
    }
}
