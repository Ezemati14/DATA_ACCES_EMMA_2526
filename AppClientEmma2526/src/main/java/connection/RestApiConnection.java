package connection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class RestApiConnection {

    //Aca construimos la URL base, que siempre es la misma
    //Luego en el lendingCliente eso va cambiando segun lo que se haga
    private final String URL_BASE = "https://localhost:8080";

    //Credenciales para la autenticacion Basic con la API (Spring Security)
    private final String USUARIO = "library";
    private final String PASSWORD = "library";

    //En desarrollo el certificado es autofirmado, asi que le decimos a Java
    //que confie en cualquier certificado y en cualquier hostname
    static {
        confiarEnTodosLosCertificados();
    }

    //Esta funciona al usarse en el lendingClient,
    //quedaria algo asi https://localhost:8080/lending/lend?isbn=0141189207445&userCode=A786543
    public HttpResponse sendPost(String endpoint, String parametros) {

        try {
            //Aca armamos la URL completa, que fue lo que nos llego del lendingClient
            //y la url que ya estaria armada en una variable fija
            URL url = new URL(URL_BASE + endpoint + "?" + parametros);
            System.out.println(url);

            //Con esto abrimos conexion con la API
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            //Una vez que abrimos conexion, le decimos que el metodo que vamos a usar es POST
            conexion.setRequestMethod("POST");
            //No queremos que siga redirecciones (login, etc.) automaticamente
            conexion.setInstanceFollowRedirects(false);
            //Le decimos a Spring que no nos mande HTML, asi nos responde con texto plano o JSON
            conexion.setRequestProperty("Accept", "application/json, text/plain, */*");
            //Le pasamos las credenciales con autenticacion Basic
            String credenciales = USUARIO + ":" + PASSWORD;
            String credencialesBase64 = Base64.getEncoder().encodeToString(credenciales.getBytes());
            conexion.setRequestProperty("Authorization", "Basic " + credencialesBase64);
            //Pedimos a Spring Security que no nos mande la pagina de login si fallamos
            conexion.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //Esto nos va a devolver o un 200, o 404, o 500, etc
            int status = conexion.getResponseCode();

            //Si el status es 400 o mayor, leemos del errorStream
            //Si no, leemos del inputStream normal
            InputStream stream;
            if (status >= 400) {
                stream = conexion.getErrorStream();
            } else {
                stream = conexion.getInputStream();
            }

            StringBuilder respuesta = new StringBuilder();
            if (stream != null) {
                BufferedReader lector = new BufferedReader(new InputStreamReader(stream));
                String linea;
                while ((linea = lector.readLine()) != null) {
                    respuesta.append(linea);
                }
                lector.close();
            }

            return new HttpResponse(status, respuesta.toString());
        } catch (Exception e) {
            return new HttpResponse(-1, "Error: " + e.getMessage());
        }
    }

    //Manda un POST con cuerpo JSON. Lo usamos para enviar libros a la API.
    public HttpResponse sendPostJson(String endpoint, String jsonBody) {

        try {
            URL url = new URL(URL_BASE + endpoint);
            System.out.println(url);

            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setInstanceFollowRedirects(false);
            //Le decimos que el body es JSON
            conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conexion.setRequestProperty("Accept", "application/json, text/plain, */*");
            //Credenciales Basic
            String credenciales = USUARIO + ":" + PASSWORD;
            String credencialesBase64 = Base64.getEncoder().encodeToString(credenciales.getBytes());
            conexion.setRequestProperty("Authorization", "Basic " + credencialesBase64);
            conexion.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //Habilitamos la salida y escribimos el JSON
            conexion.setDoOutput(true);
            byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(bytes);
            }

            int status = conexion.getResponseCode();
            InputStream stream;
            if (status >= 400) {
                stream = conexion.getErrorStream();
            } else {
                stream = conexion.getInputStream();
            }

            StringBuilder respuesta = new StringBuilder();
            if (stream != null) {
                BufferedReader lector = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                String linea;
                while ((linea = lector.readLine()) != null) {
                    respuesta.append(linea);
                }
                lector.close();
            }

            return new HttpResponse(status, respuesta.toString());
        } catch (Exception e) {
            return new HttpResponse(-1, "Error: " + e.getMessage());
        }
    }

    //Esto solo se usa en desarrollo para aceptar certificados autofirmados
    private static void confiarEnTodosLosCertificados() {
        try {
            TrustManager[] trustAll = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAll, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier aceptarTodos = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(aceptarTodos);
        } catch (Exception e) {
            System.out.println("No se pudo configurar SSL: " + e.getMessage());
        }
    }
}
