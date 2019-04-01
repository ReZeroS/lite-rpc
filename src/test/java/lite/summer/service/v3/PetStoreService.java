package lite.summer.service.v3;

import lite.summer.dao.v3.AccountDao;
import lite.summer.dao.v3.ItemDao;

/**
 * @Author: ReZero
 * @Date: 4/1/19 5:00 PM
 * @Version 1.0
 */
public class PetStoreService {

    private AccountDao accountDao;
    private ItemDao  itemDao;
    private int version;

    public PetStoreService(AccountDao accountDao, ItemDao itemDao){
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.version = -1;
    }
    public PetStoreService(AccountDao accountDao, ItemDao itemDao,int version){
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.version = version;
    }
    public int getVersion() {
        return version;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }



}
