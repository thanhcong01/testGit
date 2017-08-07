package com.ftu.admin.consumer;

import java.util.ArrayList;
import java.util.List;

import com.ftu.admin.consumer.entity.AuthenticationOutput;
import com.ftu.admin.consumer.entity.IdentityEntity;
import com.ftu.admin.consumer.entity.TransEntity;

public class ConsumerTest {

	public static void main(String[] args) {
		TransEntity transEntity = new TransEntity();
		transEntity.setTransId("BD43B0F0ADE2D9CFCB80726AA6B50719");
		transEntity.setUsername("admin");
		List<IdentityEntity> lstRs = new ArrayList<IdentityEntity>();
		AuthenticationOutput output;
		try {
			long a = System.currentTimeMillis();
			output = AuthenticationConsumer.login(transEntity, "haitx", "QL0AFWMIX8NRZTKeof9cXsvbvu8=", "VETC.SEC");
			System.out.println((System.currentTimeMillis() - a));
			System.out.println(output.getIdentityEntity().getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
