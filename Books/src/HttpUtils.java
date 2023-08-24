import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    public static Map<String, List<String>> parseQuery(String query) {
        Map<String, List<String>> queryParams = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = decodeUrlComponent(keyValue[0]);
                    String value = decodeUrlComponent(keyValue[1]);
                    queryParams.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
                }
            }
        }
        return queryParams;
    }

    private static String decodeUrlComponent(String component) {
        try {
            return URLDecoder.decode(component, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return component;
        }
    }
}
