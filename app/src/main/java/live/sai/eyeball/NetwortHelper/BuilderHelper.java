package live.sai.eyeball.NetwortHelper;

import android.net.Uri;

public class BuilderHelper {
    private static final String BASE_URL = "http://192.168.68.125/sari-sari/api/";
    public static String UrlBuilder(final String API_NAME){
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
        builder.appendPath(API_NAME);
        final String END_POINT = builder.build().toString();

        return END_POINT;
    }
}
