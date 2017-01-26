package com.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.api.HelloWorldRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration test to verify that expected endpoints are either restricted or unrestricted
 */
@RunWith(SpringRunner.class)
@WebMvcTest(HelloWorldRestController.class)
public class OAuth2ResourceApplicationTests {

	@SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
	private MockMvc mockMvc;

	@Test
	public void verifyUnrestrictedAccess() throws Exception {
		mockMvc
				.perform(get("/unrestricted").accept(MediaType.ALL))
				.andExpect(status().isOk());
	}

    @Test
    public void verifyUnrestrictedRootAccess() throws Exception {
        mockMvc
                .perform(get("/").accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

	@Test
	public void verifyRestrictedAccessUnauthorized() throws Exception {
		mockMvc
				.perform(get("/restricted").accept(MediaType.ALL))
				.andExpect(status().isUnauthorized());
	}

	@WithMockUser
    @Test
    public void verifyRestrictedAccessAuthorized() throws Exception {
        mockMvc
                .perform(get("/restricted").accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

}
