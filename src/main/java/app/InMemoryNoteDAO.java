package app;

import use_case.cookinglist.cookinglist.note.NoteDataAccessInterface;

public class InMemoryNoteDAO implements NoteDataAccessInterface {

    private String note = ""; // 内存变量，用来保存当前 note

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void saveNote(String note) {
        this.note = note;
    }
}