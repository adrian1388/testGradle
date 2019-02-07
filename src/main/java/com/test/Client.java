package com.test;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.test.type.CustomType;

import okhttp3.OkHttpClient;

@Configuration
public class Client {

    private static final String BASE_URL = "http://localhost:8063/graphql";
    
    private static final String SUBSCRIPTION_BASE_URL = "ws://localhost:8063/subscriptions";

    private CustomTypeAdapter<UUID> uuidCustomTypeAdapter;

    @Bean
    public ApolloClient apolloClient() {

    	// UUID type adapter
        uuidCustomTypeAdapter = new CustomTypeAdapter<UUID>() {
            @Override public UUID decode(
                @SuppressWarnings("rawtypes")
                CustomTypeValue value
            ) {
                return UUID.fromString(value.value.toString());
            }

            @SuppressWarnings("rawtypes")
            @Override public CustomTypeValue encode(UUID value) {
                return new CustomTypeValue.GraphQLString(value.toString());
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //TODO add logging
//            .addInterceptor(logging)
            .build();

        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .addCustomTypeAdapter(CustomType.UUID, uuidCustomTypeAdapter)
            //TODO add cache
//            .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
            .build();
    }
}
