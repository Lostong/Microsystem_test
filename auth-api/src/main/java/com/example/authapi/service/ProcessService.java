package com.example.authapi.service;

import com.example.authapi.entity.ProcessingLog;
import com.example.authapi.entity.User;
import com.example.authapi.repository.ProcessingLogRepository;
import com.example.authapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ProcessService {

    private final ProcessingLogRepository logRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${data-api.url}")
    private String dataApiUrl;

    @Value("${internal.token}")
    private String internalToken;

    public ProcessService(ProcessingLogRepository logRepository,
                          UserRepository userRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    public String process(String email, String text) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalToken);

        Map<String, String> body = Map.of("text", text);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                dataApiUrl + "/api/transform",
                HttpMethod.POST,
                entity,
                Map.class
        );

        String result = (String) response.getBody().get("result");

        ProcessingLog log = new ProcessingLog();
        log.setUser(user);
        log.setInputText(text);
        log.setOutputText(result);
        logRepository.save(log);

        return result;
    }
}