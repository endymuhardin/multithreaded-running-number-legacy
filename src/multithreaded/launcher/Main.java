/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multithreaded.launcher;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author endy
 */
public class Main {
    public static void main(String[] args) {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{
            "classpath:multithreaded/config/applicationContext.xml",
            "classpath:multithreaded/config/server-httpinvoker-ctx.xml"
        });
        ctx.registerShutdownHook();
    }
}
