package adamantium.servlets;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.xsp.webapp.DesignerFacesServlet;

public class XSPServlet extends DesignerFacesServlet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        ServletOutputStream out = res.getOutputStream();
        FacesContext facesContext = this.getFacesContext(req, res);

        try {
            String method = request.getMethod();
            if ("POST".equals(method)) {
                doPost(request, response, facesContext, out);
            } else if ("GET".equals(method)) {
                doGet(request, response, facesContext, out);
            } else if ("PUT".equals(method)) {
                doPut(request, response, facesContext, out);
            } else if ("DELETE".equals(method)) {
                doDelete(request, response, facesContext, out);
            } else if ("PATCH".equals(method)) {
                doPatch(request, response, facesContext, out);
            } else {
                doUnsupported(request, response, facesContext, out);
            }
        } catch (Exception e) {
            e.printStackTrace(new PrintStream(out));
        } finally {
            out.flush();
            out.close();
            if (null != facesContext) {
                facesContext.responseComplete();
                facesContext.release();
            }
        }
    }

    protected void doPost(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        doUnsupported(request, response, facesContext, out);
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        doUnsupported(request, response, facesContext, out);
    }

    protected void doPut(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        doUnsupported(request, response, facesContext, out);
    }

    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        doUnsupported(request, response, facesContext, out);
    }

    protected void doPatch(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        doUnsupported(request, response, facesContext, out);
    }

    protected void doUnsupported(final HttpServletRequest request, final HttpServletResponse response,
            final FacesContext facesContext, final ServletOutputStream out) throws Exception {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
