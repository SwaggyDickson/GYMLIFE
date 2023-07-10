package tw.gymlife.member.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReCaptchaService {

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String SECRET_KEY = "6LdzSg4nAAAAANTOzQy_aRaBJujyU8SVs-pXUI45";

    private final WebClient webClient;

    public ReCaptchaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(GOOGLE_RECAPTCHA_VERIFY_URL).build();
    }

    public boolean verify(String recaptchaToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", SECRET_KEY);
        formData.add("response", recaptchaToken);

        RecaptchaResponse responseBody = webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(RecaptchaResponse.class)
                .block();

        return responseBody.isSuccess();
    }

    static class RecaptchaResponse {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}

	




	

