package interface_adapter.note;

import interface_adapter.note.NoteInputBoundary;

/**
 * Controller for our Note related Use Cases.
 */
public class NoteController {

    private final NoteInputBoundary noteInteractor;

    public NoteController(NoteInputBoundary noteInteractor) {
        this.noteInteractor = noteInteractor;
    }

    /**
     * Executes the Note related Use Cases.
     * @param note the note to be recorded
     */
    public void execute(String note) {
        if (note != null && !note.isEmpty()) {
            NoteInputData inputData = new NoteInputData(note);
            noteInteractor.execute(inputData);
        }else {
            noteInteractor.executeRefresh();
        }
    }
}
