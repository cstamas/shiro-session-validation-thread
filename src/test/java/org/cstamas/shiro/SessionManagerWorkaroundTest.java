package org.cstamas.shiro;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.junit.Test;

public class SessionManagerWorkaroundTest
    extends SessionManagerTestSupport
{
    @Test
    public void sessionValidationThreadWithWorkaroundTest()
        throws Exception
    {
        logger.info( "## Started sessionValidationThreadWithWorkaroundTest()" );
        final long sessionValidationInterval = TimeUnit.SECONDS.toMillis( 5 );
        final DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationInterval( sessionValidationInterval );

        // workaround:
        // 1) pre-install the instance of session validation scheduler
        // 2) set interval on it to be same as used by sessionManager
        // 3) enable it
        ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler =
            new ExecutorServiceSessionValidationScheduler( sessionManager );
        executorServiceSessionValidationScheduler.setInterval( sessionManager.getSessionValidationInterval() );
        executorServiceSessionValidationScheduler.enableSessionValidation();
        sessionManager.setSessionValidationScheduler( executorServiceSessionValidationScheduler );

        acquireSessionsInParallel( sessionManager );

        System.out.println( Arrays.toString( ManagementFactory.getThreadMXBean().dumpAllThreads( true, true ) ) );

        logger.info( "## Finished sessionValidationThreadWithWorkaroundTest()" );
    }
}
