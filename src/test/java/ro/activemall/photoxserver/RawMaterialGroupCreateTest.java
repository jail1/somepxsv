package ro.activemall.photoxserver;

/*
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
 import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
 */
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoxApplication.class)
@WebAppConfiguration
@DirtiesContext
public class RawMaterialGroupCreateTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void postsATooShort() throws Exception {

		HttpSession session = mockMvc
				.perform(
						post("/login").param("photox_username", "admin").param(
								"photox_password", "4dm1n1str4"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn()
				.getRequest().getSession();

		Assert.assertNotNull(session);

		this.mockMvc.perform(post(
				"/private/API/v1/createOrUpdateRawMaterialGroup")
				.session((MockHttpSession) session)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"A name\"}".getBytes())
				.accept(MediaType.APPLICATION_JSON));
		/**
		 * .andExpect(content().string( allOf(
		 * containsString("Form contains errors. Please try again."),
		 * containsString
		 * ("&quot;John&quot; is too short. Should not be shorter than 5") ) )
		 * );
		 **/
	}
}
