package trackManager.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SubTaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {

        try{
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {

                    break;
                } case "POST": {

                    break;
                } case "DELETE": {

                    break;
                } default: {
                    System.out.println("Неправельный запрос");
                    httpExchange.sendResponseHeaders(405,0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }
}
