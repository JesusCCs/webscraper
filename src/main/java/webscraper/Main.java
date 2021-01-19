package webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    static final String URL = "https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice";
    static URL url;

    static final String ARCHIVO = "IBEX.txt";

    static final int TIEMPO_ESPERA = 30; // segundos

    static {
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        while (true) {
            final String linea = leerTablaJSOUP();
            escribirLog(linea);
            Thread.sleep(TIEMPO_ESPERA * 1000);
        }

    }

    private static void escribirLog(String linea) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO,true))) {
                bw.write(linea + "\n");
                System.out.println("Línea escrita");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método donde tomamos el HTML con la clase URL y a partir de la búsqueda de ciertas cadenas de texto clave,
     * vamos fitrando y obteniendo los datos que queremos
     * @return La línea a escribir en el fichero
     */
    private static String leerTablaURL() {
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

            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy,HH:mm:ss"));

            return fechaHora + "," + "IBEX35" + "," + ultimo
                    + ". Datos de tabla -> " + " Anterior: " + anterior + ". Último: " + ultimo + ". Diferencia: " + dif +
                    ". Máximo: " + maximo + ". Mínimo: " + minimo + ". Fecha: " + fecha + ". Hora: " + hora + ". Diferencia año: " + dif_anyo;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método equivalente al anterior, solo que usando JSOUP, de forma que podemos recorrer el docuemnto HTML
     * con selectores css y obtener los datos de una forma mucho más limpia
     * @return La linea a escribir en el fichero
     */
    private static String leerTablaJSOUP() {
        try {
            Document document = Jsoup.connect(URL).get();

            // Accedemos con el id de la tabla al contenido y con la pseudo clase last-child
            // podemos quedarnos directamente con la última tr, que es la que tiene lo que interesa.
            Elements tr = document.body().select("#ctl00_Contenido_tblÍndice tbody tr:last-child");

            final String[] datosTabla = tr.text().split(" ");

            final String anterior = datosTabla[2],
                    ultimo = datosTabla[3],
                    dif = datosTabla[4],
                    maximo = datosTabla[5],
                    minimo = datosTabla[6],
                    fecha = datosTabla[7],
                    hora = datosTabla[8],
                    dif_anyo = datosTabla[9],
                    fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy,HH:mm:ss"));

            return fechaHora + "," + "IBEX35" + "," + ultimo
                    + ". Datos de tabla -> " + " Anterior: " + anterior + ". Último: " + ultimo + ". Diferencia: " + dif +
                    ". Máximo: " + maximo + ". Mínimo: " + minimo + ". Fecha: " + fecha + ". Hora: " + hora + ". Diferencia año: " + dif_anyo;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
