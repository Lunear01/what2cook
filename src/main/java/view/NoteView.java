package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import entity.Recipe;
import interface_adapter.note.NoteController;
import interface_adapter.note.NoteState;
import interface_adapter.note.NoteViewModel;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.AddToCookingListController;
import java.util.Collections;

/**
 * The View for when the user is viewing a note in the program.
 */
public class NoteView extends JPanel implements ActionListener, PropertyChangeListener {

    private final NoteViewModel noteViewModel;
    private final CookingListViewModel cookingListViewModel;

    private final JLabel noteName = new JLabel("note for jonathan_calver2");
    private final JTextArea noteInputField = new JTextArea();

    private final JButton saveButton = new JButton("Save");
    private final JButton refreshButton = new JButton("Refresh");

    private final JButton addToCookingListButton = new JButton("Add to personal cooking list");
    private final DefaultListModel<String> cookingListModel = new DefaultListModel<>();
    private final JList<String> cookingListList = new JList<>(cookingListModel);

    private NoteController noteController;
    private AddToCookingListController addToCookingListController;

    public NoteView(NoteViewModel noteViewModel, CookingListViewModel cookingListViewModel) {

        noteName.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.noteViewModel = noteViewModel;
        CookingListViewModel cookingListViewModel
        this.noteViewModel.addPropertyChangeListener(this);
        this.cookingListViewModel.addPropertyChangeListener(evt -> refreshCookingList());

        final JPanel buttons = new JPanel();
        buttons.add(saveButton);
        buttons.add(refreshButton);
        buttons.add(addToCookingListButton);

        saveButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(saveButton)) {
                        noteController.execute(noteInputField.getText());

                    }
                }
        );

        refreshButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(refreshButton)) {
                        noteController.execute(null);

                    }
                }
        );

        addToCookingListButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(addToCookingListButton)) {
                        if (addToCookingListController == null) {
                            JOptionPane.showMessageDialog(this,
                                    "AddToCookingListController not set.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }


                        Recipe demoRecipe = new Recipe(
                                0,
                                "Tomato",
                                Collections.emptyList(),
                                0.0,
                                0,
                                noteInputField.getText()
                        );

                        String username = "Csc207";

                        addToCookingListController.add(username, demoRecipe);
                    }
                }
        );


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(noteName);
        this.add(noteInputField);
        this.add(buttons);

        JLabel cookingListLabel = new JLabel("Personal cooking list:");
        cookingListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(cookingListLabel);
        this.add(new JScrollPane(cookingListList));
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final NoteState state = (NoteState) evt.getNewValue();
        setFields(state);
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFields(NoteState state) {
        noteInputField.setText(state.getNote());
    }


    private void refreshCookingList() {
        cookingListModel.clear();
        for (Recipe r : cookingListViewModel.getPersonalCookingList()) {
            cookingListModel.addElement(r.getTitle());
        }
    }



    public void setNoteController(NoteController controller) {
        this.noteController = controller;
    }

    public void setAddToCookingListController(AddToCookingListController controller) {
        this.addToCookingListController = controller;
    }
}

