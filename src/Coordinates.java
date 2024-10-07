import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;

class Coordinates {
    private final int x;
    private final float y;
    private final float r;

    public Coordinates(String parameters) {
        if (parameters == null || parameters.trim().isEmpty()) {
            throw new IllegalArgumentException("The query string is missing or empty.");
        }

        var paramMap = parseParameters(parameters);
        validate(paramMap);

        this.x = Integer.parseInt(paramMap.get("x"));
        this.y = Float.parseFloat(paramMap.get("y"));
        this.r = Float.parseFloat(paramMap.get("r"));
    }

    private Map<String, String> parseParameters(String params) {
        var keyValueMap = new HashMap<String, String>();
        var pairs = params.split("&");

        for (var pair : pairs) {
            var delimiterPosition = pair.indexOf("=");
            if (delimiterPosition != -1) {
                var key = URLDecoder.decode(pair.substring(0, delimiterPosition), StandardCharsets.UTF_8);
                var value = URLDecoder.decode(pair.substring(delimiterPosition + 1), StandardCharsets.UTF_8);
                keyValueMap.put(key, value);
            }
        }
        return keyValueMap;
    }

    private void validate(Map<String, String> paramMap) {
        String xParam = paramMap.get("x");
        String yParam = paramMap.get("y");
        String rParam = paramMap.get("r");

        if (xParam == null) {
            throw new IllegalArgumentException("The 'x' parameter is missing.");
        }
        if (yParam == null) {
            throw new IllegalArgumentException("The 'y' parameter is missing.");
        }
        if (rParam == null) {
            throw new IllegalArgumentException("The 'r' parameter is missing.");
        }

        if (!isValidInteger(xParam, -3, 5)) {
            throw new IllegalArgumentException("Invalid or out-of-bounds 'x' parameter.");
        }
        if (!isValidFloat(yParam, -5, 3)) {
            throw new IllegalArgumentException("Invalid or out-of-bounds 'y' parameter.");
        }
        if (!isValidFloat(rParam, 2, 5)) {
            throw new IllegalArgumentException("Invalid or out-of-bounds 'r' parameter.");
        }
    }

    private boolean isValidInteger(String value, int min, int max) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= min && intValue <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidFloat(String value, float min, float max) {
        try {
            float floatValue = Float.parseFloat(value);
            return floatValue >= min && floatValue <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getR() {
        return this.r;
    }
}