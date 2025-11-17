package interface_adapter.note;

public class NoteInputData {
    private final String noteContent;

    public NoteInputData(String noteContent) {
        this.noteContent = noteContent;
    }
    public String getNoteContent() {
        return noteContent;
    }
}
