package com.unibell.testtask;

import com.unibell.testtask.controller.ClientController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestTaskApplicationTests {

	@Autowired
	ClientController clientController;

	@Test
	void contextLoads() {
	}

}
