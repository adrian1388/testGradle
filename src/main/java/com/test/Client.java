package com.test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import com.test.type.CustomType;

import okhttp3.OkHttpClient;

@Configuration
public class Client {

    private static final String BASE_URL = "http://localhost:8063/coordinator/register";
    
    private static final String SUBSCRIPTION_BASE_URL = "ws://localhost:8063/subscriptions";

    private CustomTypeAdapter<UUID> uuidCustomTypeAdapter;
    private CustomTypeAdapter<LocalDateTime> localDateTimeCustomTypeAdapter;
    private CustomTypeAdapter<BigDecimal> bigDecimalCustomTypeAdapter;
    private CustomTypeAdapter<Instant> instantCustomTypeAdapter;

    @Bean
    public ApolloClient apolloClient() {

    	// UUID type adapter
        uuidCustomTypeAdapter = new CustomTypeAdapter<UUID>() {
            @Override
            public UUID decode(
                @SuppressWarnings("rawtypes")
                CustomTypeValue value
            ) {
                return UUID.fromString(value.value.toString());
            }

            @SuppressWarnings("rawtypes")
            @Override
            public CustomTypeValue encode(UUID value) {
                return new CustomTypeValue.GraphQLString(value.toString());
            }
        };

    	// LocalDateTime type adapter
        localDateTimeCustomTypeAdapter = new CustomTypeAdapter<LocalDateTime>() {
		    @Override
		    public LocalDateTime decode(
		        @SuppressWarnings("rawtypes")
		        CustomTypeValue value
		    ) {
//		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		        return LocalDateTime.parse(value.value.toString());
			}

			@SuppressWarnings("rawtypes")
			@Override
			public CustomTypeValue encode(LocalDateTime value) {
                return new CustomTypeValue.GraphQLString(value.toString());
			}
        };

    	// BigDecimal type adapter
        bigDecimalCustomTypeAdapter = new CustomTypeAdapter<BigDecimal>() {
		    @Override
		    public BigDecimal decode(
		        @SuppressWarnings("rawtypes")
		        CustomTypeValue value
		    ) {
		        return new BigDecimal(value.value.toString());
			}

			@SuppressWarnings("rawtypes")
			@Override
			public CustomTypeValue encode(BigDecimal value) {
                return new CustomTypeValue.GraphQLString(value.toString());
			}
        };

    	// BigDecimal type adapter Instant
        instantCustomTypeAdapter = new CustomTypeAdapter<Instant>() {
		    @Override
		    public Instant decode(
		        @SuppressWarnings("rawtypes")
		        CustomTypeValue value
		    ) {
//		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		        return Instant.parse(value.value.toString());
			}

			@SuppressWarnings("rawtypes")
			@Override
			public CustomTypeValue encode(Instant value) {
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
            .addCustomTypeAdapter(CustomType.LOCALDATETIME, localDateTimeCustomTypeAdapter)
            .addCustomTypeAdapter(CustomType.BIGDECIMAL, bigDecimalCustomTypeAdapter)
            .addCustomTypeAdapter(CustomType.INSTANT, instantCustomTypeAdapter)
            //TODO add cache
//            .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
            .subscriptionTransportFactory(new WebSocketSubscriptionTransport.Factory(SUBSCRIPTION_BASE_URL, okHttpClient))
            .build();
    }
}
