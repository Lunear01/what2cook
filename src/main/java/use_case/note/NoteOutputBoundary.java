package use_case.note;

public interface NoteOutputBoundary {
    void prepareSuccessView(String note);
    void prepareFailView(String errorMessage);
}

