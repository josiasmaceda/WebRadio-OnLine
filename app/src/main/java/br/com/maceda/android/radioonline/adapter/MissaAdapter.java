package br.com.maceda.android.radioonline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.vo.Missa;

/**
 * Created by josias on 26/01/2016.
 */
public class MissaAdapter extends RecyclerView.Adapter<MissaAdapter.MissasViewHolder> {

    protected static final String TAG = "MissaAdapter";
    private Context context;
    private List<Missa> missas;

    public MissaAdapter(Context context, List<Missa> missas) {
        this.context = context;
        this.missas = missas;
    }

    @Override
    public MissaAdapter.MissasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_generico, parent, false);
        MissasViewHolder holder = new MissasViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MissaAdapter.MissasViewHolder holder, final int position) {
        Missa missa = missas.get(position);
        holder.txtNome.setText(missa.getDescricao());
        holder.txtHorario.setText(missa.getHorario());
    }

    @Override
    public int getItemCount() {
        return missas != null ? missas.size() : 0;
    }


    public static class MissasViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNome;
        private TextView txtHorario;

        public MissasViewHolder(View itemView) {
            super(itemView);
            txtNome = (TextView) itemView.findViewById(R.id.txtTitle);
            txtHorario = (TextView) itemView.findViewById(R.id.txtSubTitle);
        }
    }
}
