package webscraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    static final String URL = "https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice";
    static URL url;

    static {
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        while (true) {

        }

    }

    private static String leerIBEX() {
        try(InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String lineaIBEX = br.lines().filter(linea -> linea.contains("IBEX 35") && linea.contains("DifFlBj"))
                    .findFirst().orElse(null);

            return lineaIBEX != null ? lineaIBEX.split("</td><td>")[1] : null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
