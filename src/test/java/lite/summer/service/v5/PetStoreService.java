package lite.summer.service.v5;


import lite.summer.beans.factory.annotation.Autowired;
import lite.summer.dao.v5.AccountDao;
import lite.summer.dao.v5.ItemDao;
import lite.summer.stereotype.Component;
import lite.summer.util.MessageTracker;

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
}
