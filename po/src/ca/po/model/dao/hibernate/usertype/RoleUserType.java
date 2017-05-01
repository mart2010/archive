package ca.po.model.dao.hibernate.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;
import ca.po.model.type.PoStatusCode;
import ca.po.model.type.Role;


/**
 * @author MOUELLET
 * Hibernate mapping type to persist role to a database column.
 * 
 */

public class RoleUserType implements UserType {

	private static final int[] SQL_TYPES = {Types.VARCHAR};

	public int[] sqlTypes() { return SQL_TYPES; }
	public Class returnedClass() { return PoStatusCode.class; }
	public boolean equals(Object x, Object y) { return x == y; }
	public Object deepCopy(Object value) { return value; }
	public boolean isMutable() { return false; }

	public Object nullSafeGet(ResultSet resultSet,
							  String[] names,
							  Object owner)
			throws HibernateException, SQLException {

	  String name = resultSet.getString(names[0]);
	  return resultSet.wasNull() ? null : Role.GET_ROLE(name);
	}

	public void nullSafeSet(PreparedStatement statement,
							Object value,
							int index)
			throws HibernateException, SQLException {

		if (value == null) {
			statement.setNull(index, Types.VARCHAR);
		} else {
			statement.setString(index, value.toString());
		}
	}

    

}
