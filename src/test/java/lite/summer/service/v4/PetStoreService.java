package lite.summer.service.v4;


import lite.summer.beans.factory.annotation.Autowired;
import lite.summer.dao.v4.AccountDao;
import lite.summer.dao.v4.ItemDao;
import lite.summer.stereotype.Component;

@Component(value="petStore")
public class PetStoreService {
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private ItemDao itemDao;
	
	public AccountDao getAccountDao() {
		return accountDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}
	
	
}