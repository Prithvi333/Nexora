package com.nexora.ai.services;

import com.nexora.ai.dto.orders.OrderHistoryResponse;
import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.dto.product.ReviewResponse;
import com.nexora.ai.dto.product.ReviewSummaryResponse;
import com.nexora.ai.feign.OrderClient;
import com.nexora.ai.feign.ProductClient;
import com.nexora.ai.feign.UserClient;
import com.nexora.ai.utility.GlobalUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClient productClient;
    private final UserClient userClient;
    private final OrderClient orderClient;
    private final VectorStore vectorStore;

    @Value("classpath:templates/ai/productDescription.st")
    private Resource productDescriptionTemplate;

    @Value("classpath:templates/ai/productSearch.st")
    private Resource productSearchTemplate;

    @Value("classpath:templates/ai/productRecommendation.st")
    private Resource productRecommendationTemplate;

    @Value("classpath:templates/ai/productReview.st")
    private Resource productReviewTemplate;


    @Qualifier("policyVectorStore")
    private final VectorStore policyVectorStore;

    @Autowired
    private ChatClient chatClient;

    @Override
    public String generateProductDescription(String productName) {
        log.info("Generating product description for productName: {}", productName);

        String description = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec
                        .text(productDescriptionTemplate)
                        .param("productName", productName))
                .call()
                .content();

        log.debug("Generated product description for {}: {}", productName, description);
        return description;
    }

    private List<ProductResponse> fetchProductList(List<String> purchasedUids, String profileUid) {
        Set<String> purchasedUidSet = new HashSet<>(purchasedUids);

        Set<String> candidateUids = new HashSet<>();
        List<ProductResponse> purchased = productClient.fetchProductByProductUidList(purchasedUids).getBody();
        for (ProductResponse purchasedProduct : purchased) {

            if (purchasedProduct.description() == null ||
                    purchasedProduct.description().isBlank()) {
                continue;
            }

            List<Document> documents =
                    vectorStore.similaritySearch(
                            SearchRequest.builder()
                                    .query(purchasedProduct.description())
                                    .topK(10)
                                    .build()
                    );

            documents.stream()
                    .map(document -> document.getMetadata().get("productUid"))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .filter(uid -> !purchasedUidSet.contains(uid))
                    .forEach(candidateUids::add);
        }

        if (candidateUids.isEmpty()) {
            log.info("No recommendation candidates found for profileUid: {}", profileUid);
            return List.of();
        }


        return productClient.fetchProductByProductUidList(
                new ArrayList<>(candidateUids)
        ).getBody();
    }

    private List<ProductResponse> fetchRelevantProducts(String productSearchQuery) {

        List<Document> documents =
                vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(productSearchQuery)
                                .topK(2)
                                .build()
                );

        if (documents.isEmpty()) {
            log.warn("No relevant products found for query: {}", productSearchQuery);
            return List.of();
        }

        List<String> productUids = documents.stream()
                .map(document -> document.getMetadata().get("productUid"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();

        return productClient.fetchProductByProductUidList(productUids).getBody();


    }

    @Override
    public List<ProductResponse> productSmartSearch(String productSearchQuery) {
        log.info("Initiating smart search for query: '{}'", productSearchQuery);

        List<ProductResponse> products = fetchRelevantProducts(productSearchQuery);

        if (products.isEmpty()) {
            log.warn("Smart search aborted: No relevant products found.");
            return List.of();
        }

        log.info(
                "Smart search completed for query '{}'. Found {} relevant products.",
                productSearchQuery,
                products.size()
        );

        return products;
    }

    @Override
    public List<ProductResponse> productRecommendation() {
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        log.info("Initiating product recommendations for userUid: {}", userUid);

        String profileUid = userClient.fetchUserProfileUid(userUid).getBody();
        if (profileUid == null) {
            log.warn("Unable to fetch profileUid for userUid: {}. Returning empty recommendations.", userUid);
            return List.of();
        }

        List<OrderHistoryResponse> orders = orderClient.getAllOrderHistory(profileUid).getBody();
        if (orders == null || orders.isEmpty()) {
            log.info("No order history found for profileUid: {}. Returning empty recommendations.", profileUid);
            return List.of();
        }

        List<String> orderUids = orders.stream()
                .map(OrderHistoryResponse::orderUid)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<String> purchasedUids = orderClient.fetchProductUidByOrderUids(orderUids).getBody();
        if (purchasedUids == null || purchasedUids.isEmpty()) {
            log.warn("No purchased product UIDs found for order UIDs: {}", orderUids);
            return List.of();
        }

        List<ProductResponse> products = fetchProductList(purchasedUids, profileUid);

        if (products.isEmpty()) {
            return List.of();
        }

        log.info(
                "Successfully fetched {} product recommendations for userUid: {}",
                products.size(),
                userUid
        );

        return products;
    }

    @Override
    public ReviewSummaryResponse summarizeReviews(String productUid) {
        log.info("Initiating review summarization for productUid: {}", productUid);

        List<ReviewResponse> reviews = productClient.getReviewsByProduct(productUid).getBody();
        if (reviews == null || reviews.isEmpty()) {
            log.error("Failed to summarize reviews: No reviews found for productUid: {}", productUid);
            throw new IllegalArgumentException("No reviews found for product: " + productUid);
        }

        ProductResponse product = productClient.findProductByUid(productUid).getBody();
        if (product == null) {
            log.error("Failed to summarize reviews: Product not found for productUid: {}", productUid);
            throw new IllegalArgumentException("Product not found: " + productUid);
        }

        log.debug("Summarizing {} reviews for product: {}", reviews.size(), product.name());

        BeanOutputConverter<ReviewSummaryResponse> converter =
                new BeanOutputConverter<>(ReviewSummaryResponse.class);

        ReviewSummaryResponse summaryResponse = chatClient.prompt()
                .user(prompt -> prompt
                        .text(productReviewTemplate)
                        .params(Map.of(
                                "product", product,
                                "reviews", reviews,
                                "format", converter.getFormat()
                        )))
                .call()
                .entity(converter);

        log.info("Successfully generated review summary for productUid: {}", productUid);
        return summaryResponse;
    }

    @Override
    public String chat(String prompt) {

        log.info(
                "Processing generic chat request with prompt length: {} characters",
                prompt != null ? prompt.length() : 0
        );

        SearchRequest searchRequest = SearchRequest.builder()
                .topK(5)
                .query(prompt)
                .build();

        List<Document> policyDocuments =
                policyVectorStore.similaritySearch(searchRequest);

        log.info("Retrieved {} policy documents", policyDocuments.size());

        policyDocuments.forEach(document ->
                log.info(
                        "========== RETRIEVED POLICY ==========\n{}",
                        document.getText()
                )
        );

        String policyContext = policyDocuments.stream()
                .map(Document::getText)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n"));

        log.info("Policy context length: {}", policyContext.length());

        String systemPrompt = """
            You are the Nexora AI assistant.

            Use the following information as reference data.
            Do not invent anything yourself.

            Context:
            %s
            """.formatted(policyContext);

        log.info("System prompt length: {}", systemPrompt.length());

        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .content();
    }
}