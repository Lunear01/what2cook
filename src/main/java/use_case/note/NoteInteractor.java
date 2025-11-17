package use_case.note;

public class NoteInteractor implements NoteInputBoundary {
    private final NoteDataAccessInterface dataAccess;
    private final NoteOutputBoundary presenter;

    public NoteInteractor(NoteDataAccessInterface dataAccess, NoteOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(String note) {
        if (note == null) {
            // Load note
            String loadedNote = dataAccess.getNote("default_user");
            presenter.prepareSuccessView(loadedNote != null ? loadedNote : "");
        } else {
            // Save note
            dataAccess.saveNote("default_user", note);
            presenter.prepareSuccessView(note);
        }
    }
}

