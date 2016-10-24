package br.com.virtz.www.locavirtzapp.rest;

/**
 * Classe que representa um parametro rest.
 */
public class ParamRest {

    private String chave;
    private String valor;
    private boolean queryString;

    public ParamRest(String chave, String valor, boolean queryString) {
        this.chave = chave;
        this.valor = valor;
        this.queryString = queryString;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isQueryString() {
        return queryString;
    }

    public void setQueryString(boolean queryString) {
        this.queryString = queryString;
    }
}
