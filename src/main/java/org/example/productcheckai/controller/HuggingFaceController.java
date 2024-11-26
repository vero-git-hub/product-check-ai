package org.example.productcheckai.controller;

import org.example.productcheckai.service.HuggingFaceService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
public class HuggingFaceController {

    private final HuggingFaceService huggingFaceService;

    public HuggingFaceController(HuggingFaceService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    @PostMapping("/generate")
    public Mono<String> generateText(@RequestBody String input) {
        return huggingFaceService.generateText(input);
    }

}