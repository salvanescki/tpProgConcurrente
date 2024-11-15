package org.example;

public class ResultadoGlobalTest extends ResultadoGlobal {
    byte tag;

    public ResultadoGlobalTest(int k, byte tag) {
        super(k);
        this.tag = tag;
    }

    @Override
    protected void mostrarResultadoFinal() {
        super.mostrarResultadoFinal();
        System.out.println("Resultado esperado: " + tag);
    }
}
