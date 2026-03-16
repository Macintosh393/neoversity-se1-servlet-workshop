package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ErrorResponse;
import model.Note;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotesServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Note> notes = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(notes.values()));
            return;
        }
        String noteId = pathInfo.substring(1);
        Note note = notes.get(noteId);

        if (note == null) {
            ErrorResponse error = new ErrorResponse("Note not found");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, objectMapper.writeValueAsString(error));
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(objectMapper.writeValueAsString(note));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Note note = objectMapper.readValue(body, Note.class);
        if (note.getTitle() == null || note.getTitle().isEmpty()) {
            ErrorResponse error = new ErrorResponse("Title is required");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, objectMapper.writeValueAsString(error));
            return;
        }
        note.setId(UUID.randomUUID().toString());
        notes.put(note.getId(), note);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location", "/notes/" + note.getId());
        resp.getWriter().write(objectMapper.writeValueAsString(note));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String noteId = req.getPathInfo().substring(1);
        Note note = notes.remove(noteId);

        if (note == null) {
            ErrorResponse error = new ErrorResponse("Note not found");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, objectMapper.writeValueAsString(error));
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
