package com.btmatthews.atlas.opencmis;

import org.apache.chemistry.opencmis.client.api.Session;

/**
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
public class SessionHolder {
    private static ThreadLocal<Session> SESSION = new ThreadLocal<Session>();

    public static void clearSession() {
        SESSION.set(null);
    }

    public static Session getSession() {
        return SESSION.get();
    }

    public static void setSession(final Session session) {
        SESSION.set(session);
    }
}
