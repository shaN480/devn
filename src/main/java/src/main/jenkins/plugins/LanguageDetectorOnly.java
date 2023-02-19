package src.main.jenkins.plugins;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class LanguageDetectorOnly {
    private static final String LANGUAGE_REGEX = "(?i)#\\s*language:\\s*(\\w+)";

    public void detectAndTrigger(String pipelineJob) throws ClientProtocolException, IOException {
        String codeLanguage = extractLanguage(pipelineJob);
        triggerApi(codeLanguage);
    }

    private String extractLanguage(String pipelineJob) {
        Pattern pattern = Pattern.compile(LANGUAGE_REGEX);
        Matcher matcher = pattern.matcher(pipelineJob);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private void triggerApi(String codeLanguage) throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost("API_URL");

        StringEntity input = new StringEntity("{\"language\":\"" + codeLanguage + "\"}");
        input.setContentType("application/json");
        postRequest.setEntity(input);

        HttpResponse response = httpClient.execute(postRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        }
    }
}
