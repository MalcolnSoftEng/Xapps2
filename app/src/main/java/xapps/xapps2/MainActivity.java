package xapps.xapps2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import xapps.xapps2.dao.TarefaDAO;
import xapps.xapps2.modelo.Tarefa;

public class MainActivity extends AppCompatActivity {
    private EditText campoDescr;
    private ImageButton campoData;
    private CheckBox campoChecked;
    private TextView novaData;

    Button froatButton;
    private ListView ltw;

    Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ltw = (ListView)findViewById(R.id.ltw);
        final TarefaDAO dao = new TarefaDAO(this);
        tarefa = new Tarefa();
        campoDescr = (EditText) findViewById(R.id.formulario_descricao);

        campoData = (ImageButton) findViewById(R.id.formulario_data);

        novaData = (TextView)findViewById(R.id.novaData);
        campoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View calendarView = li.inflate(R.layout.calendar_view,null);
                final DatePicker dp = calendarView.findViewById(R.id.datePicker);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(calendarView);
                alert.setTitle("Data Atual:" + String.valueOf(dp.getDayOfMonth()+"/"+ String.valueOf(dp.getMonth() + 1) + "/" + String.valueOf(dp.getYear())));
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String data = String.valueOf(dp.getDayOfMonth()+"/"+ String.valueOf(dp.getMonth() + 1) + "/" + String.valueOf(dp.getYear()));
                        tarefa.setData(data);
                        Toast.makeText(getApplicationContext(),"Data Escolhida: "+ data,Toast.LENGTH_SHORT).show();
                        novaData.setText(data);
                    }
                });

                //alert.setTitle("Atenção");

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });

        campoChecked = (CheckBox) findViewById(R.id.formulario_checked);

        froatButton = (Button)findViewById(R.id.listagem_FAB_BTN);
        froatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campoDescr.getText().toString().equals("") ) {
                    Toast.makeText(getBaseContext(), "Por favor preencha corretamente a tarefa", Toast.LENGTH_SHORT).show();
                }else if (novaData.getText().toString().equals("00/00/0000")){
                    Toast.makeText(getBaseContext(), "Por favor preencha corretamente a data", Toast.LENGTH_SHORT).show();

                }else {
                    tarefa.setNome(campoDescr.getText().toString());
                    //tarefa.setData(campoData.getText().toString());
                    if (campoChecked.isChecked()){
                        tarefa.setObservacao("Concluída");
                    } else {
                        tarefa.setObservacao("PENDENTE");
                    }
                    dao.insert(tarefa);
                }
                dao.close();
                carregaLista();
            }
        });
        registerForContextMenu(ltw);
    }


    private void carregaLista() {
        TarefaDAO dao = new TarefaDAO(this);
        //final List<Tarefa> tarefas = dao.buscaTarefas();
        dao.close();

        //TarefasAdapter<> arrayAdapter = new ArrayAdapter<Tarefa>(this,android.R.layout.simple_list_item_1,tarefas);
        TarefasAdapter adapter = new TarefasAdapter(getBaseContext(),dao.lista());
        ltw.setAdapter(adapter);

        /*
        ltw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Tarefa tarefa = (Tarefa) ltw.getItemAtPosition(position);

                String item = String.valueOf(tarefas.get(position));
                Intent itemLista = new Intent(getBaseContext(), MainActivity.class);
                itemLista.putExtra("tarefa", tarefa);
                /*


                startActivity(itemLista);
                Toast.makeText(MainActivity.this, tarefa.getNome(),Toast.LENGTH_SHORT).show();
            }
        }
        ); */
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Tarefa tarefa = (Tarefa) ltw.getItemAtPosition(info.position);



        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TarefaDAO dao = new TarefaDAO(MainActivity.this);
                dao.excluir(tarefa);
                dao.close();
                Toast.makeText(MainActivity.this, "Deletou a : "+ tarefa.getNome(),Toast.LENGTH_SHORT).show();
                carregaLista();
                return false;
            }
        });
    }


}

