package xapps.xapps2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import xapps.xapps2.modelo.Tarefa;

/**
 * Created by Malcoln on 01/07/2017.
 */

public class TarefaDAO extends SQLiteOpenHelper {

    public TarefaDAO(Context context) {
        super(context, "Listagem", null, 1);
    }

    public static String CONSULT_DESCRI_PEDIDO;
    private int acumQuant;
    private String PEDIDO_FINAL_STRING="";
    private String acumDescriPed;
    public static int consultQtd;
    private String[] colunas = new String[]{"id", "nome", "quant", "observacao", "data"};

    @Override
    public void onCreate(SQLiteDatabase db) { // para criar tabela fazer instrução em SQL

        String sql = "CREATE TABLE Tarefa (id INTEGER PRIMARY KEY, nome VARCHAR, quant INTEGER, observacao TEXT, data TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Pessoa";
        db.execSQL(sql);
        onCreate(db);
    }


    public void insert(Tarefa tarefa) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDaTarefa(tarefa);
        db.insert("Tarefa", null, dados);

    }

    @NonNull
    private ContentValues pegaDadosDaTarefa(Tarefa tarefa) {
        ContentValues dados = new ContentValues();
        dados.put("nome", tarefa.getNome());
        dados.put("quant", tarefa.getQuant());
        dados.put("observacao", tarefa.getObservacao());
        dados.put("data", tarefa.getData());

        return dados;
    }

    public List<Tarefa> buscaTarefas() {

        String sql = "SELECT * FROM Tarefa;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql,null);
        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        while (c.moveToNext()){
            Tarefa tarefa = new Tarefa();
            tarefa.setId(c.getLong(c.getColumnIndex("id")));
            tarefa.setNome(c.getString(c.getColumnIndex("nome")));
            tarefa.setQuant(c.getInt(c.getColumnIndex("quant")));
            tarefa.setObservacao(c.getString(c.getColumnIndex("observacao")));
            tarefa.setData(c.getString(c.getColumnIndex("data")));

            tarefas.add(tarefa);
        }

        c.close();
        return tarefas;
    }

    public void excluir(Tarefa tarefa) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {tarefa.getId().toString()};
        db.delete("Tarefa", "id = ?", params);
    }

    public void alterar(Tarefa tarefa) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDaTarefa(tarefa);
        String[] params = {tarefa.getId().toString()};
        db.update("Tarefa", dados, "id = ?", params );

    }
    public List<Tarefa> lista() {
        List<Tarefa> lista = new ArrayList<Tarefa>();
        SQLiteDatabase dbPedido = getWritableDatabase();
        Cursor c = dbPedido.query("Tarefa", colunas, null, null, null, null, null, null);

        while (c.moveToNext()) {

            Tarefa tarefa = new Tarefa();

            tarefa.setNome(c.getString(c.getColumnIndex("nome")));
            tarefa.setQuant(c.getInt(c.getColumnIndex("quant")));
            tarefa.setObservacao(c.getString(c.getColumnIndex("observacao")));
            tarefa.setData(c.getString(c.getColumnIndex("data")));
            tarefa.setId(c.getLong(c.getColumnIndex("id")));
            lista.add(tarefa);
            int x = c.getInt(c.getColumnIndex("quant"));

            if (x>0){

                acumQuant = acumQuant+(c.getInt(c.getColumnIndex("quant")));

                acumDescriPed = c.getString(c.getColumnIndex("nome"))
                        +c.getString(c.getColumnIndex("observacao"));
                PEDIDO_FINAL_STRING = PEDIDO_FINAL_STRING + acumQuant;
            }

            consultQtd = acumQuant;

            CONSULT_DESCRI_PEDIDO = PEDIDO_FINAL_STRING;
        }

        dbPedido.close();
        return lista;
    }
}
