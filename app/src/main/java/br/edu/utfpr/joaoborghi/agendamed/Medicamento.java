package br.edu.utfpr.joaoborghi.agendamed;

import java.util.Comparator;

public class Medicamento {
    public static Comparator<Medicamento> oredenacaoCrescente = new Comparator<Medicamento>() {
        @Override
        public int compare(Medicamento m1, Medicamento m2) {
            return m1.getNome().compareToIgnoreCase(m2.getNome());
        }
    };

    private String nome;
    private TipoMedicamento tipo;
    private int via;
    private boolean usoContinuo;

    public Medicamento(String nome, TipoMedicamento tipo, int via, boolean usoContinuo) {
        this.nome = nome;
        this.tipo = tipo;
        this.via = via;
        this.usoContinuo = usoContinuo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoMedicamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMedicamento tipo) {
        this.tipo = tipo;
    }

    public int getVia() {
        return via;
    }

    public void setVia(int via) {
        this.via = via;
    }

    public boolean isUsoContinuo() {
        return usoContinuo;
    }

    public void setUsoContinuo(boolean usoContinuo) {
        this.usoContinuo = usoContinuo;
    }

    @Override
    public String toString() {
        return nome + "\n" +
                tipo + "\n" +
                via + "\n" +
                usoContinuo;
    }
}
