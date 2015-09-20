package ca.canvac.webstore.dao.ibatis;

import ca.canvac.webstore.dao.AccountDao;
import ca.canvac.webstore.domain.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


import java.util.List;

public class SqlMapAccountDao extends SqlMapClientDaoSupport implements AccountDao {

    public Account getAccount(String userId) throws DataAccessException {
        return (Account) getSqlMapClientTemplate().queryForObject("getAccountByUsername", userId);
    }

    public Account getAccount(String userId, String password) throws DataAccessException {
        Account account = new Account();
        account.setUserId(userId);
        account.setPassword(password);
        return (Account) getSqlMapClientTemplate().queryForObject("getAccountByUsernameAndPassword", account);
    }

    public List getUsernameList() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("getUsernameList", null);
    }

    public void updateAccount(Account account) throws DataAccessException {
        getSqlMapClientTemplate().update("updateAccount", account);
        if (account.getPassword() != null && account.getPassword().length() > 0) {
            getSqlMapClientTemplate().update("updateSignon", account);
        }
    }

}
