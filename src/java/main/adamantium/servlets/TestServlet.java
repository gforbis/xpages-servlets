package adamantium.servlets;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Session;

import com.ibm.commons.util.io.json.JsonJavaObject;

public class TestServlet extends XSPServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        out.write(Job.queryStatus().getBytes("utf-8"));
        out.flush();
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {

        TaskManager taskManager = (TaskManager) facesContext.getApplication().getVariableResolver().resolveVariable(
                facesContext, "TaskManager");
        Runnable job = RunnableWrapper.wrap(new Job(facesContext));
        taskManager.execute(job, 20, TimeUnit.SECONDS);
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");
        out.write(Job.queryStatus().getBytes("utf-8"));
        out.flush();
    }

    private static class Job implements SessionRunnable {
        private final FacesContext facesContext;

        public Job(final FacesContext context) {
            this.facesContext = context;
        }

        public void run(final Session session) {
            try {
                updateStatus("running");
                // Simulated code
                for (int n = 0; n <= 10; n++) {
                    updateStatus(String.valueOf(n * 10) + "%");
                    Thread.sleep(500L);
                }
            } catch (Throwable t) {
                updateStatus("error");
            }
            updateStatus("complete");
        }

        @SuppressWarnings("unchecked")
        private void updateStatus(final String status) {
            JsonJavaObject jsonObject = new JsonJavaObject();
            Map<String, Object> sessionMap = (Map<String, Object>) this.facesContext.getApplication()
                    .getVariableResolver().resolveVariable(this.facesContext, "sessionScope");
            jsonObject.putString("status", status);
            sessionMap.put("TestServlet.status", jsonObject);
        }

        @SuppressWarnings("unchecked")
        static public String queryStatus() {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = (Map<String, Object>) context.getApplication().getVariableResolver()
                    .resolveVariable(context, "sessionScope");
            JsonJavaObject jsonObject = (JsonJavaObject) sessionMap.get("TestServlet.status");
            if (null == jsonObject) {
                jsonObject = new JsonJavaObject();
                jsonObject.putString("status", "ready");
                sessionMap.put("TestServlet.status", jsonObject);
            }
            return jsonObject.toString();
        }
    }
}
