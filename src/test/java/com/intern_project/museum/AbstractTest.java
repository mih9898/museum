package com.intern_project.museum;

import com.intern_project.museum_of_interesting_things.configuration.MyConfig;
import com.intern_project.museum_of_interesting_things.configuration.MyWebInitializer;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * The type Abstract test.
 */
@ExtendWith(SpringExtension.class)
//@ExtendWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MyConfig.class, MyWebInitializer.class})
@ActiveProfiles("dev")
@WebAppConfiguration
public abstract class AbstractTest {

    /**
     * The Context.
     */
    WebApplicationContext context;
    MockMvc mockMvc;



    /**
     * Sets context.
     *
     * @param context the context
     */
    @Autowired
    public void setContext(WebApplicationContext context) {
        this.context = context;
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}