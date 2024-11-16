package org.example;

public class ResultadoGlobalTest extends ResultadoGlobal {
    byte tag;

    public ResultadoGlobalTest(int k, byte tag) {
        super(k);
        this.tag = tag;
    }

    @Override
    public int acierto() {
        if (tagGanador() == tag) {
            return 1;
        } else {
            return 0;
        }
    }
}
