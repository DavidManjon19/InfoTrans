package com.example.infotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;

public class Pedidos_main extends AppCompatActivity {

    private RecyclerView mRecycleView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebase;

    DatabaseReference refDBp;

    DatabaseReference refDB;

    FirebaseUser currentUser;

    Bundle bundle;

    private FirebaseAuth mAuth;

    protected ArrayList<Pedidos> pedidos;

    LayoutInflater li;

    String log, Tipo;

    ArrayList<Registro_usuario> logins;

    FloatingActionButton boton;

    String logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_main);
        bundle = getIntent().getExtras();
        inic_components();
        Llenado();

    }

    @SuppressLint("RestrictedApi")
    public void inic_components() {

        log = bundle.getString("Nombre");

        Tipo = bundle.getString("Tipo");

        boton = findViewById(R.id.boton);

        if (!Tipo.equals("Empresas")) {
            boton.setEnabled(false);
            boton.setVisibility(INVISIBLE);
        }

        mRecycleView = findViewById(R.id.my_recycler_view);

        mRecycleView.setHasFixedSize(true);

        firebase = FirebaseDatabase.getInstance();

        refDB = firebase.getReference("Usuarios");

        refDBp = firebase.getReference("Pedidos");

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        li = LayoutInflater.from(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mystyle));
            actionBar.setTitle("Pedidos");
        }

    }

    public void Llenado() {

        refDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logins = new ArrayList<Registro_usuario>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        Registro_usuario logi = obj.getValue(Registro_usuario.class);
                        logins.add(logi);
                    }
                }
                Llenar_peds();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Llenar_peds() {

        if (Tipo.equals("Empresas")) {
            refDBp.child(log).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pedidos = new ArrayList<Pedidos>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot obj : dataSnapshot.getChildren()) {
                            Pedidos ped = obj.getValue(Pedidos.class);
                            pedidos.add(ped);
                        }
                    }


                    layoutManager = new LinearLayoutManager(Pedidos_main.this);

                    mRecycleView.setLayoutManager(layoutManager);

                    mAdapter = new MyAdapter(Pedidos_main.this, pedidos);

                    mRecycleView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(Pedidos_main.this, databaseError.getCode() + "", Toast.LENGTH_LONG).show();

                }

            });
        } else {

            for (int i = 0; i < logins.size(); i++) {
                logs = logins.get(i).getLogin();
                if (i == 0) {
                    pedidos = new ArrayList<Pedidos>();
                }
                refDBp.child(logins.get(i).getLogin()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                Pedidos ped = obj.getValue(Pedidos.class);
                                if (ped.getNom_cliente().equals(log)) {
                                    pedidos.add(ped);
                                }
                            }
                        }


                        layoutManager = new LinearLayoutManager(Pedidos_main.this);

                        mRecycleView.setLayoutManager(layoutManager);

                        mAdapter = new MyAdapter(Pedidos_main.this, pedidos);

                        mRecycleView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(Pedidos_main.this, databaseError.getCode() + "", Toast.LENGTH_LONG).show();

                    }

                });
            }
        }
    }

    public void añadir_Pedidos(View view) {
        Intent ped = new Intent(this, A_Pedidos.class);

        Bundle nom = new Bundle();

        nom.putString("Nombre", log);

        nom.putString("Tipo", Tipo);

        ped.putExtras(bundle);

        startActivity(ped);

    }

    /**
     * Adapter específico para mi RecyclerView
     */

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Pedidos> pedidos;
        private Context context;

        public MyAdapter(Context context, ArrayList<Pedidos> pedidos) {
            this.pedidos = pedidos;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PedidosViewHolder(inflateResource(parent));
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
            final PedidosViewHolder pedidosViewHolder = (PedidosViewHolder) holder;
            final Pedidos ped = pedidos.get(position);

            pedidosViewHolder.clv.setText(ped.getClave_ref());
            pedidosViewHolder.name.setText(ped.getProducto());
            pedidosViewHolder.cant.setText(String.valueOf(ped.getCantidad()));
            pedidosViewHolder.container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bundle.getString("Tipo").equals("Clientes")) {
                        Informacion(ped);
                    }
                }
            });

            if (bundle.getString("Tipo").equals("Empresas")) {
                pedidosViewHolder.desp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popup = new PopupMenu(context, ((PedidosViewHolder) holder).desp);
                        popup.getMenuInflater().inflate(R.menu.desplegable, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String itemTitle = item.getTitle().toString();
                                switch (itemTitle) {
                                    case "Borrado":
                                        Borrado(pedidosViewHolder.clv);
                                        break;
                                    case "Modificar estado del pedido":
                                        Modificacion(pedidosViewHolder.clv);
                                        break;

                                    case "Informacion del pedido":
                                        Informacion(ped);
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                });
            } else {
                pedidosViewHolder.desp.setVisibility(INVISIBLE);
                pedidosViewHolder.desp.setEnabled(false);
            }
        }

        public void Borrado(TextView tv) {
            final TextView textView = tv;
            AlertDialog.Builder dialogo = new AlertDialog.Builder(Pedidos_main.this);

            dialogo.setTitle("Importante");

            dialogo.setMessage(" Si borras el pedido se perderá toda la información de la base de datos \n ¿Estás seguro de que quieres hacerlo?");
            dialogo.setCancelable(false);

            dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    refDBp.child(bundle.getString("Nombre")).child(textView.getText().toString()).removeValue();
                    Toast.makeText(Pedidos_main.this, "Pedido borrado", Toast.LENGTH_SHORT).show();
                }
            });
            dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dialogo.show();
        }

        public void Modificacion(final TextView tv) {
            final TextView textView = tv;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.mypersdialog);
            dialog.setTitle("Modificacion de estado:");
            final EditText editText = dialog.findViewById(R.id.editEstado);
            Button button = dialog.findViewById(R.id.ButtonModificar);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().isEmpty()) {
                        Toast.makeText(Pedidos_main.this, "El estado no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        refDBp.child(log).child(textView.getText().toString()).child("estado").setValue(editText.getText().toString());
                        Toast.makeText(Pedidos_main.this, "Estado modificado", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        }

        public void Informacion(Pedidos ped) {
            final Dialog dialog = new Dialog(context);

            dialog.setContentView(R.layout.infodialog);

            dialog.setTitle("Información del pedido");

            TextView clave, nom_prod, estado, fecha, nom_empresa, cantidad;

            clave = dialog.findViewById(R.id.clRef);
            clave.setText(ped.getClave_ref());
            clave.setEnabled(false);

            nom_prod = dialog.findViewById(R.id.nom_prod);
            nom_prod.setText(ped.getProducto());
            nom_prod.setEnabled(false);

            estado = dialog.findViewById(R.id.ped_est);
            estado.setText(ped.getEstado());
            estado.setEnabled(false);

            fecha = dialog.findViewById(R.id.fech_rel);
            fecha.setText(ped.getFecha_realización());
            fecha.setEnabled(false);

            nom_empresa = dialog.findViewById(R.id.nom_Emp);
            nom_empresa.setText(ped.getNom_empresa());
            nom_empresa.setEnabled(false);

            cantidad = dialog.findViewById(R.id.cant);
            cantidad.setText(String.valueOf(ped.getCantidad()));
            cantidad.setEnabled(false);

            Button button = dialog.findViewById(R.id.ifo_but);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        @Override
        public int getItemCount() {
            return pedidos.size();
        }

        private View inflateResource(ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.dialog, parent, false);
        }

        class PedidosViewHolder extends RecyclerView.ViewHolder {
            TextView clv, name, cant;
            ConstraintLayout container;
            ImageButton desp;


            public PedidosViewHolder(@NonNull View itemView) {
                super(itemView);
                container = itemView.findViewById(R.id.mLayout);
                clv = itemView.findViewById(R.id.clv);
                name = itemView.findViewById(R.id.name);
                cant = itemView.findViewById(R.id.cant);
                desp = itemView.findViewById(R.id.desp);
            }
        }
    }
}