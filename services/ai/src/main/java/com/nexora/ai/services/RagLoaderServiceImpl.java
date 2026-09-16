package com.nexora.ai.services;

import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.feign.ProductClient;
import io.qdrant.client.QdrantClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RagLoaderServiceImpl implements RagLoaderService {


    private final ProductClient productClient;
    private final VectorStore vectorStore;
    private final VectorStore policyVectorStore;

    public RagLoaderServiceImpl(
            ProductClient productClient,
            @Qualifier("productVectorStore") VectorStore vectorStore,
            @Qualifier("policyVectorStore") VectorStore policyVectorStore
    ) {
        this.productClient = productClient;
        this.vectorStore = vectorStore;
        this.policyVectorStore = policyVectorStore;
    }

    @Value("classpath:templates/ai/policy.st")
    private Resource policyTemplate;

    private final TokenTextSplitter textSplitter =
            TokenTextSplitter.builder()
                    .withChunkSize(1000)
                    .withMinChunkSizeChars(400)
                    .withMinChunkLengthToEmbed(20)
                    .build();

    @Override
    @Scheduled(fixedRate = 120000)
    public void loadProductsIntoVectorStore() {

        log.info("Starting periodic product RAG indexing...");

        List<ProductResponse> products =
                productClient.getAllProducts().getBody();

        if (products == null || products.isEmpty()) {
            log.warn("No products found for RAG indexing.");
            return;
        }

        List<Document> documents = products.stream()
                .filter(product ->
                        product.description() != null &&
                                !product.description().isBlank())
                .map(product -> new Document(
                        product.uid(),
                        """
                                Product Name: %s
                                Brand: %s
                                Description: %s
                                """.formatted(
                                product.name(),
                                product.brand(),
                                product.description()
                        ),
                        Map.of(
                                "productUid", product.uid()
                        )
                ))
                .toList();


        List<String> productIds = documents.stream()
                .map(Document::getId)
                .toList();

        if (!productIds.isEmpty()) {
            vectorStore.delete(productIds);
        }


        vectorStore.add(documents);

        log.info(
                "Successfully synchronized {} products into product RAG.",
                documents.size()
        );
    }

    @Override
    public void loadPolicies() {
        loadPolicyData();
    }

    @PostConstruct
    public void loadPolicyData() {
        try {

            log.info("Clearing existing policy data...");

            Filter.Expression filter =
                    new Filter.Expression(
                            Filter.ExpressionType.EQ,
                            new Filter.Key("type"),
                            new Filter.Value("POLICY")
                    );

            policyVectorStore.delete(filter);

            log.info("Existing policy data deleted.");

            String policyContent = StreamUtils.copyToString(
                    policyTemplate.getInputStream(),
                    StandardCharsets.UTF_8
            );

            log.info(
                    "Policy loaded. Length: {}",
                    policyContent.length()
            );

            Document document = new Document(
                    policyContent,
                    Map.of("type", "POLICY")
            );

            List<Document> chunks = textSplitter.apply(
                    List.of(document)
            );

            log.info(
                    "Created {} policy chunks",
                    chunks.size()
            );

            policyVectorStore.add(chunks);

            log.info("Policy chunks added to Qdrant.");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load policy file",
                    e
            );
        }
    }
}
