package io.github.rezeros.service.v2;

import io.github.rezeros.dao.v2.AccountDao;
import io.github.rezeros.dao.v2.ItemDao;

/**
 * @Author: ReZero
 * @Date: 3/19/19 9:25 PM
 * @Version 1.0
 */
public class PetStoreService {

    private AccountDao accountDao;

    private ItemDao itemDao;

    private String owner;

    private int version;

    private boolean choice;

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }


}
