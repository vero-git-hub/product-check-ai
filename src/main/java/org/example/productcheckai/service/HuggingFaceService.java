package org.example.productcheckai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class HuggingFaceService {

    private final WebClient webClient;

    @Value("${huggingface.api.key}")
    private String apiKey;

    public HuggingFaceService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api-inference.huggingface.co").build();
    }

    public Mono<String> generateText(String inputText) {
        System.out.println("We send a request with the text:" + inputText);

        return webClient.post()
                .uri("/models/gpt2")
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(Map.of("inputs", inputText))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("Error from Hugging Face: " + errorBody);
                                    return Mono.error(new RuntimeException("Error: " + errorBody));
                                })
                )
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Reply from Hugging Face: " + response));
    }

}
