package com.nexora.ai.config;

import com.nexora.ai.advisor.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class AiClientConfig {


    @Value("classpath:templates/ai/systemMessage.st")
    private Resource systemMessage;

    @Bean
    public ChatClient registerAwsBedrockChatClient(ChatClient.Builder clientBuilder, TokenUsageAdvisor tokenUsageAdvisor) {
        return clientBuilder
                .defaultAdvisors(List.of(tokenUsageAdvisor))
                .defaultSystem(systemMessage)
                .build();
    }


}
