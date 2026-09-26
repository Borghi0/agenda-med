package br.edu.utfpr.joaoborghi.agendamed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MedicamentoAdapter extends BaseAdapter {
    private Context context;
    private List<Medicamento> medicamentos;
    private String[] vias;

    private static class MedicamentoHolder{
        public TextView textViewValorNome;
        public TextView textViewValorTipo;
        public TextView textViewValorVia;
        public TextView textViewValorUsoContinuo;
    }

    public MedicamentoAdapter(Context context, List<Medicamento> medicamentos) {
        this.context = context;
        this.medicamentos = medicamentos;
        vias = context.getResources().getStringArray(R.array.vias);
    }

    @Override
    public int getCount() {
        return medicamentos.size();
    }

    @Override
    public Object getItem(int i) {
        return medicamentos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MedicamentoHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_medicamentos, viewGroup, false);

            holder = new MedicamentoHolder();
            holder.textViewValorNome = view.findViewById(R.id.textViewValorNome);
            holder.textViewValorTipo = view.findViewById(R.id.textViewValorTipo);
            holder.textViewValorVia = view.findViewById(R.id.textViewValorVia);
            holder.textViewValorUsoContinuo = view.findViewById(R.id.textViewValorUsoContinuo);

            view.setTag(holder);
        } else {
            holder = (MedicamentoHolder) view.getTag();
        }

        Medicamento medicamento = medicamentos.get(i);

        holder.textViewValorNome.setText(medicamento.getNome());

        switch(medicamento.getTipo()){
            case Capsula:
                holder.textViewValorTipo.setText(R.string.capsula);
                break;
            case Comprimido:
                holder.textViewValorTipo.setText(R.string.comprimido);
                break;
            case Liquido:
                holder.textViewValorTipo.setText(R.string.liquido);
                break;
        }

        holder.textViewValorVia.setText(vias[medicamento.getVia()]);
        holder.textViewValorUsoContinuo.setText(medicamento.isUsoContinuo() ? R.string.uso_continuo : R.string.uso_nao_continuo);

        return view;
    }
}
