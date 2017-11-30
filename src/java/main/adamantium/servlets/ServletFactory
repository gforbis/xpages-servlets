package adamantium.servlets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.IServletFactory;
import com.ibm.designer.runtime.domino.adapter.ServletMatch;

public class ServletFactory implements IServletFactory {
    private ComponentModule module;
    private final Pattern pattern = Pattern.compile("/xsp/([^/]+)(?:.*)");

    public void init(final ComponentModule compModule) {
        this.module = compModule;
    }

    public ServletMatch getServletMatch(final String contextPath, final String path) throws ServletException {
        try {
            Matcher m = this.pattern.matcher(path);
            String servletName = "error";
            if (m.matches()) {
                servletName = m.group(1);
            }
            ServletConfig serv = ServletConfig.match(servletName);
            try {
                Class<?> clazz = this.module.getModuleClassLoader().loadClass(serv.getClassPath());
                return new ServletMatch(this.module.createServlet(clazz.getName(), servletName, null), "", path);
            } catch (ClassNotFoundException e) {
                System.err.println(e.toString());
                return null;
            }
        } catch (ServletException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private enum ServletConfig {
        error("Error Servlet", "adamantium.servlets.ErrorServlet"),
        test("Test Servlet", "v2.itd.servlets.TestServlet");

        private final String classPath;
        private final String title;

        private ServletConfig(final String title, final String classPath) {
            this.classPath = classPath;
            this.title = title;
        }

        public String getClassPath() {
            return this.classPath;
        }

        public String getTitle() {
            return this.title;
        }

        static public ServletConfig matchPath(final String path) {
            for (ServletConfig serv : values()) {
                if (path.equals(serv.name())) {
                    return serv;
                }
            }
            return error;
        }

        @Override
        public String toString() {
            return name() + "[" + this.classPath + "]" + " (" + this.title + ")";
        }
    }
}
