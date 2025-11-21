package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import use_case.note.NoteDataAccessInterface;
import data_access.note.InMemoryNoteDAO;

/**
 * An application where we can view and add to a note stored by a user.
 *
 * <p>This is a minimal example demonstrating a simple note-editing application.
 * The note is stored using the remote user API from lab 5, meaning all devices
 * will see the same saved note when refreshed.</p>
 */
public class MainNoteApplication {

    /**
     * Entry point of the application.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            NoteAppBuilder builder = new NoteAppBuilder();

            // Correct DAO location after refactor
            NoteDataAccessInterface noteDAO = new InMemoryNoteDAO();

            builder
                    .addNoteDAO(noteDAO)
                    .addNoteView()
                    .addNoteUseCase()
                    .addCookingListUseCase();

            JFrame frame = builder.build();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}