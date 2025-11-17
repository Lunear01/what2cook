package interface_adapter.note;

public class NoteInteractor implements NoteInputBoundary{
    private final NoteDataAccessInterface noteDAO;
    private final NoteOutputBoundary presenter;

    public NoteInteractor(NoteDataAccessInterface noteDAO,
                          NoteOutputBoundary presenter) {
        this.noteDAO = noteDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(NoteInputData inputData) {
        String newContent = inputData.getNoteContent();


        noteDAO.saveNote(newContent);
        NoteOutputData outputData = new NoteOutputData(newContent, null);
        presenter.present(outputData);
    }


    public void executeRefresh() {
        String existing = noteDAO.getNote();
        NoteOutputData outputData = new NoteOutputData(existing, null);
        presenter.present(outputData);
    }

}
