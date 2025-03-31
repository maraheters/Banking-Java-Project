package example.banking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-containers")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BankingApplicationTests {

	@Test
	void contextLoads() {
	}

}
