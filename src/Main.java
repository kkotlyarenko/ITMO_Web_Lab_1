import com.fastcgi.FCGIInterface;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;


public class Main {
    public static void main(String[] args) {
        var fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            try {
                LocalTime startTime = LocalTime.now();
                var queryParams = System.getProperties().getProperty("QUERY_STRING");
                var coordinates = new Coordinates(queryParams);
                boolean isValid = validateCoordinates(coordinates.getX(), coordinates.getY(), coordinates.getR());

                var currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                long executionTime = Duration.between(startTime, LocalTime.now()).toMillis();

                var jsonResponse = String.format(Headers.RESULT_JSON, isValid, currentTime, executionTime / 1000);
                var httpResponse = buildHttpResponse(jsonResponse);
                System.out.println(httpResponse);

            } catch (Exception ex) {
                var errorResponse = buildErrorResponse(ex.getMessage());
                System.out.println(errorResponse);
            }
        }
    }

    private static boolean validateCoordinates(float x, float y, float r) {
        if (x <= 0 && y <= 0)
            return (x * x + y * y) <= (r / 2) * (r / 2);

        if (x <= 0 && y >= 0)
            return y <= (r + x);

        if (x >= 0 && y >= 0)
            return (x <= r / 2) && (y <= r);

        return false;
    }

    private static String buildHttpResponse(String json) {
        var contentLength = json.getBytes(StandardCharsets.UTF_8).length + 2;
        return String.format(Headers.HTTP_RESPONSE, contentLength, json);
    }

    private static String buildErrorResponse(String errorMessage) {
        var json = String.format(Headers.ERROR_JSON, errorMessage);
        var contentLength = json.getBytes(StandardCharsets.UTF_8).length + 2;
        return String.format(Headers.HTTP_ERROR, contentLength, json);
    }
}