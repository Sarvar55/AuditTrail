package com.codems.audittrail;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@ActiveProfiles("test")
class AuditTrailApplicationTests {

    private static final String VERSION = "X-API-Version";

    private final WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    AuditTrailApplicationTests(WebApplicationContext context) {
        this.context = context;
    }

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void loginIsPublic() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .header(VERSION, "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody("demo@example.com", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    void tasksRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/tasks").header(VERSION, "1.0"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.code").value("UNAUTHORIZED"));
    }

    @Test
    void usersCannotReadAnotherUsersTask() throws Exception {
        String userToken = login("demo@example.com", "password");
        String adminToken = login("admin@audittrail.local", "change-me-admin-password");

        String body = mockMvc.perform(post("/api/tasks")
                        .header(VERSION, "1.0")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Owned task\",\"description\":\"private\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number taskId = JsonPath.read(body, "$.data.id");
        mockMvc.perform(get("/api/tasks/{id}", taskId.longValue())
                        .header(VERSION, "1.0")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.code").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void adminCanFilterAuditEvents() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .header(VERSION, "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody("demo@example.com", "wrong-password")))
                .andExpect(status().isUnauthorized());

        String token = login("admin@audittrail.local", "change-me-admin-password");

        mockMvc.perform(get("/api/audit-events/admin")
                        .param("actionType", "LOGIN_FAILED")
                        .header(VERSION, "1.0")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].actionType").value("LOGIN_FAILED"));
    }

    private String login(String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .header(VERSION, "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody(email, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.data.accessToken");
    }

    private String loginBody(String email, String password) {
        return "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password);
    }

}
