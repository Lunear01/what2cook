package interface_adapter.note;


/**
 * The presenter for our Note viewing and editing program.
 */
public class NotePresenter implements NoteOutputBoundary {

    private final NoteViewModel noteViewModel;

    public NotePresenter(NoteViewModel noteViewModel) {
        this.noteViewModel = noteViewModel;
    }


    @Override
    public void present(NoteOutputData outputData) {
        if (outputData.getError() == null) {
            prepareSuccessView(outputData.getNoteContent());
        } else {
            prepareFailView(outputData.getError());
        }
    }
    /**
     * Prepares the success view for the Note related Use Cases.
     *
     * @param note the output data
     */
    @Override
    public void prepareSuccessView(String note) {
        noteViewModel.getState().setNote(note);
        noteViewModel.getState().setError(null);
        noteViewModel.firePropertyChanged();
    }

    /**
     * Prepares the failure view for the Note related Use Cases.
     *
     * @param errorMessage the explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {
        noteViewModel.getState().setError(errorMessage);
        noteViewModel.firePropertyChanged();
    }
}
