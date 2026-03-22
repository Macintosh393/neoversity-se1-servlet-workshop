package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.StatsResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class StatsServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        Long startTime = (Long) context.getAttribute("startTime");
        Long upTime = (System.currentTimeMillis() - startTime) / 1000;

        AtomicLong requestCounter = (AtomicLong) context.getAttribute("requestCounter");
        int mapSize = ((Map) context.getAttribute("notes")).size();

        resp.getWriter().write(objectMapper.writeValueAsString(new StatsResponse(upTime, requestCounter.get(), mapSize)));
    }
}
