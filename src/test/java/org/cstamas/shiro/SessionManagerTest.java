package org.cstamas.shiro;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.junit.Test;

public class SessionManagerTest
    extends SessionManagerTestSupport
{
    @Test
    public void sessionValidationThreadTest()
        throws Exception
    {
        logger.info( "## Started sessionValidationThreadTest()" );
        final long sessionValidationInterval = TimeUnit.SECONDS.toMillis( 5 );
        final DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationInterval( sessionValidationInterval );

        acquireSessionsInParallel( sessionManager );

        System.out.println( Arrays.toString( ManagementFactory.getThreadMXBean().dumpAllThreads( true, true ) ) );

        logger.info( "## Finished sessionValidationThreadTest()" );
    }
}
