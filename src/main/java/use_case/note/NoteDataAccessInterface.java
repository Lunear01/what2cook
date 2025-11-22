package use_case.note;

public interface NoteDataAccessInterface {
    /**
     * Retrieves the saved note for the given username.
     *
     * @param username the username whose note is being retrieved.
     * @return the saved note text, or {@code null} if no note exists.
     */
    String getNote(String username);

    /**
     * Saves the given note for the specified user.
     *
     * @param username the username for whom the note is saved.
     * @param note the note text to save.
     */
    void saveNote(String username, String note);
}

