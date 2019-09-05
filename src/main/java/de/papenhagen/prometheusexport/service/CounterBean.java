package de.papenhagen.prometheusexport.service;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import static javax.ejb.LockType.WRITE;

/**
 * An very Basic Counter for an methode
 * @author Jens Papenhagen
 */
@ConcurrencyManagement(CONTAINER)
@Singleton
public class CounterBean {
    private int hits = 1;

    @Lock(WRITE)
    public int getHits() {
        return hits++;
    }
}
