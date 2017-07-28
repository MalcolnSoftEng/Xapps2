package xapps.xapps2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import xapps.xapps2.dao.TarefaDAO;
import xapps.xapps2.modelo.Tarefa;

/**
 * Created by Malcoln on 06/01/2017.
 */

public class TarefasAdapter extends BaseAdapter {
    private Context ctx;
    private List<Tarefa> lista;

    public TarefasAdapter(Context context, List<Tarefa> tarefas) {

        this.ctx = context;
        this.lista = tarefas;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Tarefa tarefa = lista.get(position);


        TarefasAdapter.ViewHolder viewHolder = null;
        if (convertView == null){

            convertView = LayoutInflater.from(ctx).inflate(R.layout.lista_tarefa, null);
            viewHolder = new TarefasAdapter.ViewHolder();

            viewHolder.txt2 = (TextView)convertView.findViewById(R.id.textView2);
            viewHolder.txt2.setText(String.valueOf(tarefa.getNome()));

            viewHolder.txtData = (TextView)convertView.findViewById(R.id.dataTarefa);
            viewHolder.txtData.setText(String.valueOf(tarefa.getData()));

            viewHolder.txtObservacao = (TextView)convertView.findViewById(R.id.txtObservacao);
            viewHolder.txtObservacao.setText(String.valueOf(tarefa.getObservacao()));
            if (String.valueOf(tarefa.getObservacao()).equals("PENDENTE")){
                convertView.setBackgroundColor(Color.RED);

            }
            viewHolder.btnExclusao = (Button)convertView.findViewById(R.id.btnExclusao);

            viewHolder.btnEditar = (Button)convertView.findViewById(R.id.btnEdicao);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TarefasAdapter.ViewHolder) convertView.getTag();
        }

        final TarefasAdapter.ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnExclusao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tarefa tarefa = lista.get(position);
                TarefaDAO dao = new TarefaDAO(ctx);
                dao.excluir(tarefa);
                dao.lista();

                Toast.makeText(ctx, "Foram retiradas: "+tarefa.getNome()+" Item(s) da lista de tarefas"
                        , Toast.LENGTH_SHORT).show();

                if (lista.size()==1) {
                    dao.consultQtd=0;

                    dao.CONSULT_DESCRI_PEDIDO="";
                    dao.lista();
                    notifyDataSetChanged();
                }
                notifyDataSetChanged();
                v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));

            }
        });
        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Função PARA EDIÇÃO a ser implementada", Toast.LENGTH_SHORT).show();
            }
        });




        return convertView;
    }
    public class ViewHolder{
        TextView txt2;
        TextView txtData;
        TextView txtObservacao;
        Button btnExclusao;
        Button btnEditar;

    }

}

