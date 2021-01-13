package webscraper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Main {

    static final String URL = "https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice";
    static URL url;

    static final String ARCHIVO = "IBEX.txt";

    static {
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        leerTablaIBEX();

    }

    private static void escribirLog() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO,true))) {


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String leerTablaIBEX() {
        try(InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String lineaIBEX = br.lines().filter(linea -> linea.contains("IBEX 35") && linea.contains("</td>"))
                    .findFirst().orElse(null);

            assert lineaIBEX != null;
            final String[] datosTabla = lineaIBEX.split("</td><td>");

            String anterior = datosTabla[1],
                    ultimo = datosTabla[2].split("</td>")[0],
                    dif = datosTabla[2].split(">")[2],
                    maximo = datosTabla[3];

            final String[] ultimaLinea = datosTabla[4].split("</td>");

            String minimo = ultimaLinea[0],
                    fecha = ultimaLinea[1].split(">")[1],
                    hora = ultimaLinea[2].split(">")[1],
                    dif_anyo = ultimaLinea[3].split(">")[1];



            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
