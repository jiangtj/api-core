package com.jiangtj.utils.apicore.address;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

@DisabledInNativeImage
@SpringBootTest
@AutoConfigureWebTestClient
class AddressGraphQlConfigurationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testGraphQl() {
        WebTestClient client = webTestClient.mutate()
                .baseUrl("/graphql")
                .build();
        HttpGraphQlTester tester = HttpGraphQlTester.create(client);
        String document = """
                {
                     province(code: 11) {
                         name
                     }
                     city(code: 1101) {
                         name
                     }
                     area(code: 110101) {
                         name
                     }
                }
                """;
        tester.document(document)
                .execute()
                .path("province.name", path -> path.entity(String.class).isEqualTo("北京市"))
                .path("city.name", path -> path.entity(String.class).isEqualTo("市辖区"))
                .path("area.name", path -> path.entity(String.class).isEqualTo("东城区"));


        tester.document("""
                {
                     province(code: 11) {
                         cities {
                             name
                         }
                     }
                }
                """)
                .execute()
                .path("province.cities", path -> path.entityList(City.class).hasSize(1))
                .path("province.cities[0].name", path -> path.entity(String.class).isEqualTo("市辖区"));
    }

}