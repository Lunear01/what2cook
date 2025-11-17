package use_case.note;

public interface NoteDataAccessInterface {
    String getNote(String username);
    void saveNote(String username, String note);
}

