import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.infotrans.Pedidos;
import com.example.infotrans.R;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Pedidos> pedidos;
    private Context context;

    public MyAdapter(Context context, ArrayList<Pedidos> pedidos){
        this.pedidos=pedidos;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PedidosViewHolder(inflateResource(parent));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PedidosViewHolder pedidosViewHolder=(PedidosViewHolder) holder;
        final Pedidos ped=pedidos.get(position);

        pedidosViewHolder.clv.setText(ped.getClave_ref().toUpperCase());
        pedidosViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    private View inflateResource(ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.dialog,parent,false);
    }

    static class PedidosViewHolder extends RecyclerView.ViewHolder{
        TextView clv;
        ConstraintLayout container;


        public PedidosViewHolder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.mLayout);
            clv=itemView.findViewById(R.id.clv);
        }
    }
}

