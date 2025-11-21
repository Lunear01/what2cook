package use_case.cookinglist.cookinglist.note;

public class NoteOutputData {
    private final String noteContent;
    private final String error;  // 正常为null

    public NoteOutputData(String noteContent, String error) {
        this.noteContent = noteContent;
        this.error = error;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public String getError() {
        return error;
    }
}
