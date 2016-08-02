/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multithreaded.service;

import java.util.Date;
import multithreaded.model.RunningNumber;

/**
 *
 * @author endy
 */
public interface RunningNumberGeneratorService {
    public RunningNumber generateRunningNumber(Date currentDate);
}
