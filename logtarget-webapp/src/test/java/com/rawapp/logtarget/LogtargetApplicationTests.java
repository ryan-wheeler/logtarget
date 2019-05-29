package com.rawapp.logtarget;

import com.logtarget.webapp.LogtargetApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		LogtargetApplication.class
})
public class LogtargetApplicationTests {

	@Test
	public void contextLoads() {
	}

}
