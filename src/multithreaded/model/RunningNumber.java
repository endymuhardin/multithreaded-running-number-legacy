/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multithreaded.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author endy
 */
@Entity @Table(name="running_number")
public class RunningNumber implements Serializable {
    @Id @GeneratedValue
    private Long id;

    @Column(name="running_date") @Temporal(TemporalType.DATE)
    private Date currentDate;

    @Column(name="current_number")
    private Long currentNumber = 1L;

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
