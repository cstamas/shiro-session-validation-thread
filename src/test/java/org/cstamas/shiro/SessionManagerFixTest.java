package org.cstamas.shiro;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.junit.Test;

/**
 * Test that shows the problem might be fixed with DCL.
 * 
 * @author cstamas
 */
public class SessionManagerFixTest
    extends SessionManagerTestSupport
{
    @Test
    public void sessionValidationThreadTest()
        throws Exception
    {
        logger.info( "## Started sessionValidationThreadTest()" );
        final long sessionValidationInterval = TimeUnit.SECONDS.toMillis( 5 );
        final DefaultSessionManager sessionManager = new DefaultSessionManager()
        {
            @Override
            protected void enableSessionValidation()
            {
                SessionValidationScheduler scheduler = getSessionValidationScheduler();
                if ( scheduler == null )
                {
                    synchronized ( this )
                    {
                        scheduler = getSessionValidationScheduler();
                        if ( scheduler == null )
                        {
                            super.enableSessionValidation();
                        }
                    }
                }
            }
        };
        sessionManager.setSessionValidationInterval( sessionValidationInterval );

        acquireSessionsInParallel( sessionManager );

        System.out.println( Arrays.toString( ManagementFactory.getThreadMXBean().dumpAllThreads( true, true ) ) );

        logger.info( "## Finished sessionValidationThreadTest()" );
    }
}
