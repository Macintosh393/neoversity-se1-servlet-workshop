package listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Note;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebListener
public class AppInitializer implements ServletContextListener {

    private final Map<String, Note> notes = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("startTime", System.currentTimeMillis());
        context.setAttribute("notes", notes);
        context.setAttribute("requestCounter", new AtomicLong(0));
    }
}
