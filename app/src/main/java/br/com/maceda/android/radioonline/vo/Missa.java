package br.com.maceda.android.radioonline.vo;

import java.io.Serializable;

/**
 * Created by josias on 26/04/2016.
 */
public class Missa implements Serializable {

    private String descricao;
    private String horario;

    public Missa() {

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
