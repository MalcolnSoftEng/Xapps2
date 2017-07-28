package xapps.xapps2.modelo;

import java.io.Serializable;

/**
 * Created by Malcoln on 01/07/2017.
 */

public class Tarefa implements Serializable {

    private Long id;
    private String nome;
    private int quant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuant(int quant) {
        this.quant= quant;
    }
    public int getQuant() {
        return quant;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String observacao;
    private String data;



    @Override
    public String toString() {
        return getId() + "-" + getNome();
    }
}
