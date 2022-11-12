package io.github.rezeros.service.v4;


import io.github.rezeros.dao.v4.AccountDao;
import io.github.rezeros.dao.v4.ItemDao;
import io.github.rezeros.dao.v4.MethodDao;
import io.github.rezeros.beans.factory.annotation.Autowired;
import io.github.rezeros.stereotype.Component;

@Component(value="petStore")
public class PetStoreService {
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private ItemDao itemDao;

	private MethodDao methodDao;

	@Autowired
	private void init(MethodDao methodDao){
		this.methodDao = methodDao;
	}

	
	public AccountDao getAccountDao() {
		return accountDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}


	public MethodDao getMethodDao() {
		return methodDao;
	}
}