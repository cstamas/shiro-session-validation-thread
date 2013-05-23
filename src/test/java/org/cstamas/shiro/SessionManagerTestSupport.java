package org.cstamas.shiro;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.apache.shiro.session.mgt.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SessionManagerTestSupport
{
    protected Logger logger = LoggerFactory.getLogger( "TEST" );

    @SuppressWarnings( "unused" )
    protected void acquireSessionsInParallel( final SessionManager sessionManager )
        throws Exception
    {
        final Callable<Session> c1 = new Callable<Session>()
        {
            public Session call()
            {
                return sessionManager.start( new DefaultSessionContext() );
            }
        };
        final Callable<Session> c2 = new Callable<Session>()
        {
            public Session call()
            {
                return sessionManager.start( new DefaultSessionContext() );
            }
        };
        final Callable<Session> c3 = new Callable<Session>()
        {
            public Session call()
            {
                return sessionManager.start( new DefaultSessionContext() );
            }
        };

        final ExecutorService es = Executors.newFixedThreadPool( 3 );
        final Future<Session> fs1 = es.submit( c1 );
        final Future<Session> fs2 = es.submit( c2 );
        final Future<Session> fs3 = es.submit( c3 );

        es.shutdown();

        es.awaitTermination( 5, TimeUnit.SECONDS );
    }
}
