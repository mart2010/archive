package ca.canvac.webstore.dao;

import ca.canvac.webstore.domain.Account;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AccountDao {

    Account getAccount(String userId) throws DataAccessException;

    Account getAccount(String userId, String password) throws DataAccessException;

    List getUsernameList() throws DataAccessException;

    void updateAccount(Account account) throws DataAccessException;

}
