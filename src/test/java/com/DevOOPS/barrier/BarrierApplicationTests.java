package com.DevOOPS.barrier;

import com.DevOOPS.barrier.DTO.dto;
import com.DevOOPS.barrier.Mapper.AdminMapper;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackages = {"com.DevOOPS.barrier.Mapper"})
class BarrierApplicationTests {
@Autowired
	private AdminMapper aMapper;
	private ClientAndServer mockServer;

	@Test
	void contextLoads() {
		dto dt = new dto();
		dt.setAdminId("testID");
		dt.setAdminPassword("testPassword");
		aMapper.createAdmin(dt);
//		aMapper.deleteAdmin(dt.getAdminId());
	}

}
