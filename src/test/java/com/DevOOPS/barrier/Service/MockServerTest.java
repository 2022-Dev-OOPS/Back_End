package com.DevOOPS.barrier.Service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServerTest {

    @Test
        void createNaverRankingPageServer(){

            new MockServerClient("localhost", PORT)
                    .when(
                            request()
                                    .withMethod("GET")
                                    .withPath("/")
                    )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody("Test용")
                    );
        }
    private static final int PORT = 9000;
    private static ClientAndServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = ClientAndServer.startClientAndServer(PORT);
        System.out.println("mock server start");
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
        System.out.println("mock server stop");
    }
}