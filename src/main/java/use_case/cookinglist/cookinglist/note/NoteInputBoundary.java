package use_case.cookinglist.cookinglist.note;

public interface NoteInputBoundary {
    void execute(NoteInputData inputData);
    void executeRefresh();
}

