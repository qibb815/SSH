package com.springmvc.test;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

public class MyOpenSessionInViewFilter extends OpenSessionInViewFilter {

	@Override
    protected Session openSession(SessionFactory arg0)
            throws DataAccessResourceFailureException {
        Session session=super.openSession(arg0);
        session.setHibernateFlushMode(FlushMode.COMMIT);
        return session;
    }
}
