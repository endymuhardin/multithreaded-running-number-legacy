/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreaded.dao;

import java.util.Date;
import multithreaded.model.RunningNumber;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author endy
 */
@Repository
public class RunningNumberDao {

    @Autowired
    private SessionFactory sessionFactory;

    public RunningNumber generateRunningNumber(Date currentDate) {
        RunningNumber runningNumber = runningNumber = (RunningNumber) sessionFactory.getCurrentSession()
                    .createQuery("from RunningNumber r where date(r.currentDate) = date(:date)")
                    .setLockMode("r", LockMode.UPGRADE)
                    .setParameter("date", currentDate)
                    .uniqueResult();
        if (runningNumber == null) {
            runningNumber = new RunningNumber();
            runningNumber.setCurrentDate(currentDate);
            sessionFactory.getCurrentSession().saveOrUpdate(runningNumber);
        } else {
            runningNumber.setCurrentNumber(runningNumber.getCurrentNumber() + 1);
            sessionFactory.getCurrentSession().saveOrUpdate(runningNumber);
        }
        return runningNumber;
    }

    public void save(RunningNumber runningNumber) {
        sessionFactory.getCurrentSession().saveOrUpdate(runningNumber);
    }
}
