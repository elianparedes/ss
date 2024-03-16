package ar.edu.itba.ss.input;

public class ArgumentHandler {

    private int m = 0;
    private String dynamicFileName = "";
    private String staticFileName = "";

    public ArgumentHandler(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-M":
                    if (i + 1 < args.length) {
                        this.m = Integer.parseInt(args[i + 1]);
                        i++;
                    } else {
                        throw new RuntimeException("Se esperaba un valor después de -M");
                    }
                    break;
                case "-D":
                    if (i + 1 < args.length) {
                        this.dynamicFileName = args[i + 1];
                        i++;
                    } else {
                        throw new RuntimeException("Se esperaba un valor después de -D");
                    }
                    break;
                case "-S":
                    if (i + 1 < args.length) {
                        this.staticFileName = args[i + 1];
                        i++;
                    } else {
                        throw new RuntimeException("Se esperaba un valor después de -S");
                    }
                    break;
                default:
                    throw new RuntimeException("Opción no reconocida: " + args[i]);
            }
        }
    }

    public int getM() {
        return m;
    }

    public String getDynamicFileName() {
        return dynamicFileName;
    }

    public String getStaticFileName() {
        return staticFileName;
    }
}
