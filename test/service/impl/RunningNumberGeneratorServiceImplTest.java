/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import multithreaded.model.RunningNumber;
import multithreaded.service.RunningNumberGeneratorService;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.CannotAcquireLockException;

/**
 *
 * @author endy
 */
public class RunningNumberGeneratorServiceImplTest {

    private static AbstractApplicationContext applicationContext;
    private static DataSource dataSource;
    private static RunningNumberGeneratorService service;

    @BeforeClass
    public static void initContext() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(
        //        "classpath:multithreaded/config/client-httpinvoker-ctx.xml"
                "classpath:multithreaded/config/applicationContext.xml"
        );
        dataSource = (DataSource) applicationContext.getBean("dataSource");
        service = (RunningNumberGeneratorService) applicationContext.getBean("runningNumberGeneratorService");

        exportSchema();

    }

    private static void exportSchema() throws Exception {
        Connection connection = dataSource.getConnection();
        Configuration configuration = new AnnotationConfiguration().configure("multithreaded/config/hibernate.cfg.xml");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
        new SchemaExport(configuration, connection).create(true, true);
        connection.close();
    }

    @Test
    public void testMultithreaded() throws Exception {
        int numThread = 20;
        final int numLoop = 100;

        final List<Long> generatedNumbers = 
                Collections.synchronizedList(new ArrayList<Long>());

        final List<GeneratorThread> generators = 
                Collections.synchronizedList(new ArrayList<GeneratorThread>());

        // create threads
        for (int i = 0; i < numThread; i++) {
           generators.add(new GeneratorThread(generatedNumbers, numLoop, i));
        }

        // run threads
        for (GeneratorThread generatorThread : generators) {
            new Thread(generatorThread).start();
        }

        // check thread completion
        boolean completed = false;
        while(!completed){
            for (GeneratorThread generatorThread : generators) {
                completed = generatorThread.getCompleted();
                System.out.println("Thread : "+generatorThread.getThreadNo() +" "+(completed?"completed":"still running"));
            }

            Thread.sleep(10);
        }

        // all completed, now check result
        Set<Long> checker = new HashSet<Long>();
        for (Long result : generatedNumbers) {
            if(!checker.add(result)){
                Assert.fail(result+" sudah ada");
            }
        }

        // all OK, check number of successful number generation:
        System.out.println("# of number generated : "+checker.size());
    }

    private static class GeneratorThread implements Runnable {

        private List<Long> generatedNumbers;
        private Integer numLoops;
        private Integer threadNo;
        private Boolean completed = false;

        public Boolean getCompleted() {
            return completed;
        }

        public Integer getThreadNo() {
            return threadNo;
        }

        

        public GeneratorThread(List<Long> generatedNumbers, Integer numLoops, Integer threadNo) {
            this.generatedNumbers = generatedNumbers;
            this.numLoops = numLoops;
            this.threadNo = threadNo;
        }

        public void run() {
            for (int j = 0; j < numLoops; j++) {
                try {
                    RunningNumber num = service.generateRunningNumber(new Date());
                    System.out.println("Thread " + threadNo + " : Current Number : " + num.getCurrentNumber());
                    generatedNumbers.add(num.getCurrentNumber());
                } catch(CannotAcquireLockException err) {
                    System.out.println("Thread " + threadNo +" berebut lock, no problem, lanjut saja");
                } catch (LockAcquisitionException err){
                    System.out.println("Thread " + threadNo +" berebut lock, no problem, lanjut saja");
                }
            }
            completed = true;
        }
    }
}
