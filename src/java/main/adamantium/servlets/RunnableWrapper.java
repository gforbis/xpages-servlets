package adamantium.servlets;

import lotus.domino.Session;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.SessionCloner;

public class RunnableWrapper implements Runnable {
    private final SessionCloner sessionCloner;
    private final NSFComponentModule module;
    private final SessionRunnable delegate;

    private RunnableWrapper(final SessionRunnable job) {
        this.module = NotesContext.getCurrent().getModule();
        this.sessionCloner = SessionCloner.getSessionCloner();
        this.delegate = job;
    }

    static public Runnable wrap(final SessionRunnable job) {
        return new RunnableWrapper(job);
    }

    public void run() {
        try {
            NotesContext context = new NotesContext(this.module);
            NotesContext.initThread(context);
            Session session = this.sessionCloner.getSession();
            this.delegate.run(session);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            NotesContext.termThread();
            try {
                this.sessionCloner.recycle();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
