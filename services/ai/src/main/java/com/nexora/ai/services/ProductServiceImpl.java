package com.nexora.ai.services;

import com.nexora.ai.dto.orders.OrderHistoryResponse;
import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.dto.users.UserPreferenceResponse;
import com.nexora.ai.feign.OrderClient;
import com.nexora.ai.feign.ProductClient;
import com.nexora.ai.feign.UserClient;
import com.nexora.ai.utility.GlobalUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MarkdownCodeBlockCleaner;
import org.springframework.ai.converter.ResponseTextCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductClient productClient;

    private final UserClient userClient;

    private final OrderClient orderClient;

    @Value("classpath:templates/ai/productDescription.st")
    Resource productDescriptionTemplate;

    @Value("classpath:templates/ai/productSearch.st")
    Resource productSearchTemplate;

    @Value("classpath:templates/ai/productRecommendation.st")
    Resource productRecommendationTemplate;


    @Autowired
    private ChatClient chatClient;

    @Override
    public String generateProductDescription(String productName) {
        return chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec
                        .text(productDescriptionTemplate)
                        .param("productName", productName)).call().content();
    }

    private List<ProductResponse> fetchProduceList() {
        List<ProductResponse> products =
                productClient.getAllProducts().getBody();

        if (products == null || products.isEmpty()) {
            return List.of();
        }

        return products;
    }

    @Override
    public List<ProductResponse> productSmartSearch(String productSearchQuery) {

        List<ProductResponse> products = fetchProduceList();

        BeanOutputConverter<List<ProductResponse>> converter =
                new BeanOutputConverter<>(
                        new ParameterizedTypeReference<List<ProductResponse>>() {
                        }
                );

        return chatClient.prompt()
                .user(p -> p
                        .text(productSearchTemplate)
                        .params(Map.of(
                                "products", products,
                                "query", productSearchQuery,
                                "format", converter.getFormat()
                        )))
                .call()
                .entity(converter);
    }

    @Override
    public List<ProductResponse> productRecommendation() {

        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();

        String profileUid = userClient.fetchUserProfileUid(userUid).getBody();
        if (profileUid == null) {
            return List.of();
        }

        List<OrderHistoryResponse> orders =
                orderClient.getAllOrderHistory(profileUid).getBody();

        if (orders == null || orders.isEmpty()) {
            return List.of();
        }


        List<String> orderUids = orders.stream()
                .map(OrderHistoryResponse::orderUid)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<String> purchasedUids =
                orderClient.fetchProductUidByOrderUids(orderUids).getBody();

        if (purchasedUids == null || purchasedUids.isEmpty()) {
            return List.of();
        }

        List<ProductResponse> purchased =
                productClient.fetchProductByProductUidList(purchasedUids).getBody();

        List<ProductResponse> products = fetchProduceList();

        if (purchased == null || products.isEmpty()) {
            return List.of();
        }

        ParameterizedTypeReference<List<String>> type =
                new ParameterizedTypeReference<>() {
                };

        BeanOutputConverter<List<String>> converter =
                new BeanOutputConverter<>(type);

        List<String> recommendedUids = chatClient.prompt()
                .user(prompt -> prompt
                        .text(productRecommendationTemplate)
                        .params(Map.of(
                                "purchaseHistory", purchased,
                                "products", products,
                                "format", converter.getFormat()
                        )))
                .call()
                .entity(converter);

        return products.stream()
                .filter(product -> recommendedUids.contains(product.uid()))
                .toList();
    }

    @Override
    public String chat(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }


}
