package src.main.jenkins.plugins;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Node;
import hudson.Extension;
import hudson.slaves.AbstractCloudSlave;
import hudson.slaves.DumbSlave;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.Launcher;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageDetector extends Builder {

    private final String apiLink;
    private final String codeLanguage;
    private final String authToken;

    @DataBoundConstructor
    public LanguageDetector(String apiLink, String codeLanguage, String authToken) {
        this.apiLink = apiLink;
        this.codeLanguage = codeLanguage;
        this.authToken = authToken;
    }
    
    public String getApiLink() {
    return apiLink;
    }

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        String code = build.getWorkspace().toString();
        String language = detectLanguage(code);
        if (!language.equals(codeLanguage)) {
            listener.getLogger().println("Language of code does not match the specified language. Expected: " + codeLanguage + ", Found: " + language);
            return false;
        }
        listener.getLogger().println("Language of code matches the specified language: " + codeLanguage);
        return true;
    }

    private String detectLanguage(String code) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(apiLink);
        post.setHeader("Authorization", "Token " + authToken);
        post.setEntity(new StringEntity(code));
        HttpResponse response = client.execute(post);

        // Read response content into a string
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder responseContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }

        // Parse the response string and extract the detected language
        JSONObject responseJson = JSONObject.fromObject(responseContent.toString());
        String detectedLanguage = responseJson.getString("detectedLanguage");

        return detectedLanguage;
}



    @Extension
    public static final class DevOpsBuilderDescriptor extends BuildStepDescriptor<Builder> {

        public DevOpsBuilderDescriptor() {
            load();
        }

        public FormValidation doCheckApiLink(@QueryParameter String value) {
            if (value.isEmpty()) {
                return FormValidation.error("Please enter API link");
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckAuthToken(@QueryParameter String value) {
            if (value.isEmpty()) {
                return FormValidation.error("Please enter authentication token");
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Language Detector";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindJSON(this, formData);
            save();
            return super.configure(req, formData);
        }
    }

}