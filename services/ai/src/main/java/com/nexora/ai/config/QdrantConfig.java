package com.nexora.ai.config;


import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QdrantConfig {

    @Bean("productVectorStore")
    @Primary
    public VectorStore productVectorStore(
            QdrantClient qdrantClient,
            EmbeddingModel embeddingModel) {

        return QdrantVectorStore.builder(qdrantClient, embeddingModel)
                .collectionName("products")
                .initializeSchema(true)
                .build();
    }

    @Bean("policyVectorStore")
    public VectorStore policyVectorStore(
            QdrantClient qdrantClient,
            EmbeddingModel embeddingModel) {

        return QdrantVectorStore.builder(qdrantClient, embeddingModel)
                .collectionName("policies")
                .initializeSchema(true)
                .build();
    }

    @Bean
    public QdrantClient qdrantClient() {
        QdrantGrpcClient.Builder builder =
                QdrantGrpcClient.newBuilder("localhost", 6334, false);

        return new QdrantClient(builder.build());
    }
}
