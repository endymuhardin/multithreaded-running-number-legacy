/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multithreaded.service.impl;

import java.util.Date;
import multithreaded.dao.RunningNumberDao;
import multithreaded.model.RunningNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import multithreaded.service.RunningNumberGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author endy
 */
@Service("runningNumberGeneratorService") @Transactional
public class RunningNumberGeneratorServiceImpl implements RunningNumberGeneratorService {

    @Autowired private RunningNumberDao runningNumberDao;

    @Transactional
    public RunningNumber generateRunningNumber(Date currentDate) {
        return runningNumberDao.generateRunningNumber(currentDate);
    }

}
