package io.github.rezeros.service.v5;


import io.github.rezeros.dao.v5.AccountDao;
import io.github.rezeros.dao.v5.ItemDao;
import io.github.rezeros.beans.factory.annotation.Autowired;
import io.github.rezeros.stereotype.Component;
import io.github.rezeros.util.MessageTracker;

@Component(value="petStore")
public class PetStoreService {		
	@Autowired
	AccountDao accountDao;
	@Autowired
	ItemDao itemDao;
	
	public PetStoreService() {		
		
	}
	
	public ItemDao getItemDao() {
		return itemDao;
	}

	public AccountDao getAccountDao() {
		return accountDao;
	}
	
	public void placeOrder(){
		System.out.println("place order");
		MessageTracker.addMsg("place order");
		
	}

	public void placeOrderWithException(){
		throw new NullPointerException();
	}
}
