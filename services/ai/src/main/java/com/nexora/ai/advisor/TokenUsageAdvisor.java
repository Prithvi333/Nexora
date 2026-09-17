package com.nexora.ai.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenUsageAdvisor implements CallAdvisor {

    @Override
    public ChatClientResponse adviseCall(
            ChatClientRequest request,
            CallAdvisorChain chain) {

        ChatClientResponse response = chain.nextCall(request);

        if (response.chatResponse() != null) {

            var usage = response.chatResponse()
                    .getMetadata()
                    .getUsage();

            log.info(
                    "AI TOKEN USAGE | inputTokens={} | outputTokens={}",
                    usage.getPromptTokens(),
                    usage.getCompletionTokens()
            );
        }

        return response;
    }

    @Override
    public String getName() {
        return "TokenUsageAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }


}
