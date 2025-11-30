package view;

import entity.Recipe;
import interface_adapter.cookinglist.CookingListState;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.SortCookingListController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.cookinglist.SortCookingListInputData.SortType;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CookingListView.
 */
class CookingListViewTest {

    private CookingListViewModel viewModel;
    private SortCookingListController sortController;
    private CookingListView view;

    // 通过反射拿到这些 private 组件，方便断言
    private JList<String> recipeList;
    private JLabel statusLabel;
    private JButton backButton;
    private JButton sortByHealthButton;
    private JButton sortByCaloriesButton;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        // mock ViewModel 和 Controller
        viewModel = mock(CookingListViewModel.class);
        sortController = mock(SortCookingListController.class);

        // 构造初始状态，用于构造函数中 updateFromState
        CookingListState initialState = mock(CookingListState.class);

        Recipe r1 = mockRecipe("Recipe 1");
        Recipe r2 = mockRecipe("Recipe 2");
        List<Recipe> recipes = Arrays.asList(r1, r2);

        when(initialState.getPersonalCookingList()).thenReturn(recipes);
        when(initialState.getStatusMessage()).thenReturn("Initial status");

        // CookingListView 构造函数里会调用 viewModel.getState()
        when(viewModel.getState()).thenReturn(initialState);

        // 创建被测对象
        view = new CookingListView(viewModel, sortController);

        // 用反射拿到 UI 组件
        recipeList = getPrivateField(view, "recipeList", JList.class);
        statusLabel = getPrivateField(view, "statusLabel", JLabel.class);
        backButton = getPrivateField(view, "backButton", JButton.class);
        sortByHealthButton = getPrivateField(view, "sortByHealthButton", JButton.class);
        sortByCaloriesButton = getPrivateField(view, "sortByCaloriesButton", JButton.class);
    }

    /**
     * 工具方法：mock 一个 Recipe，只关心 getTitle()
     */
    private Recipe mockRecipe(String title) {
        Recipe recipe = mock(Recipe.class);
        when(recipe.getTitle()).thenReturn(title);
        return recipe;
    }

    /**
     * 工具方法：通过反射读取 private 字段
     */
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(Object target, String name, Class<T> type) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return (T) f.get(target);
    }

    // ----------------- 测试 1：构造函数是否用初始 state 正确填充 -----------------

    @Test
    void constructor_populatesListAndStatusFromInitialState() {
        assertEquals(2, recipeList.getModel().getSize());
        assertEquals("Recipe 1", recipeList.getModel().getElementAt(0));
        assertEquals("Recipe 2", recipeList.getModel().getElementAt(1));

        assertEquals("Initial status", statusLabel.getText());
    }

    // ----------------- 测试 2：propertyChange 更新 UI -----------------

    @Test
    void propertyChange_updatesListAndStatus() {
        CookingListState newState = mock(CookingListState.class);

        Recipe a = mockRecipe("A");
        Recipe b = mockRecipe("B");
        Recipe c = mockRecipe("C");
        List<Recipe> recipes = Arrays.asList(a, b, c);

        when(newState.getPersonalCookingList()).thenReturn(recipes);
        when(newState.getStatusMessage()).thenReturn("Updated status");

        PropertyChangeEvent evt =
                new PropertyChangeEvent(this, "cookingListState", null, newState);

        view.propertyChange(evt);

        assertEquals(3, recipeList.getModel().getSize());
        assertEquals("A", recipeList.getModel().getElementAt(0));
        assertEquals("B", recipeList.getModel().getElementAt(1));
        assertEquals("C", recipeList.getModel().getElementAt(2));
        assertEquals("Updated status", statusLabel.getText());
    }

    @Test
    void propertyChange_ignoresNonCookingListState() {
        int beforeSize = recipeList.getModel().getSize();
        String beforeStatus = statusLabel.getText();

        PropertyChangeEvent evt =
                new PropertyChangeEvent(this, "anything", null, "not a state object");

        view.propertyChange(evt);

        assertEquals(beforeSize, recipeList.getModel().getSize());
        assertEquals(beforeStatus, statusLabel.getText());
    }

    // ----------------- 测试 3：Back 按钮 -----------------

    @Test
    void backButton_invokesOnBackRunnable() {
        final boolean[] called = {false};
        view.setOnBack(() -> called[0] = true);

        // 模拟点击 Back 按钮
        backButton.doClick();

        assertTrue(called[0], "Back runnable should be called");
    }

    // ----------------- 测试 4：Sort 按钮行为 -----------------

    @Test
    void sortByHealthButton_callsControllerWhenUsernameSet() {
        view.setCurrentUsername("alice");

        sortByHealthButton.doClick();

        verify(sortController, times(1))
                .sort("alice", SortType.BY_HEALTH_SCORE);
    }

    @Test
    void sortByCaloriesButton_callsControllerWhenUsernameSet() {
        view.setCurrentUsername("bob");

        sortByCaloriesButton.doClick();

        verify(sortController, times(1))
                .sort("bob", SortType.BY_CALORIES);
    }

    @Test
    void sortButtons_doNothingWhenUsernameNotSet() {
        sortByHealthButton.doClick();
        sortByCaloriesButton.doClick();

        verifyNoInteractions(sortController);
    }


    @Test
    void doubleClickList_invokesOnOpenRecipeWithSelectedRecipe() throws Exception {
        // 准备 viewModel 的 personalCookingList
        Recipe r1 = mockRecipe("R1");
        Recipe r2 = mockRecipe("R2");
        List<Recipe> recipes = Arrays.asList(r1, r2);

        when(viewModel.getPersonalCookingList()).thenReturn(recipes);

        // 通过 propertyChange 刷新列表（也可以用构造时的 state，但这里更接近真实流程）
        CookingListState state = mock(CookingListState.class);
        when(state.getPersonalCookingList()).thenReturn(recipes);
        when(state.getStatusMessage()).thenReturn("status");
        view.propertyChange(new PropertyChangeEvent(this, "state", null, state));

        recipeList.setSelectedIndex(1);

        final Recipe[] opened = {null};
        view.setOnOpenRecipe(recipe -> opened[0] = recipe);

        MouseEvent evt = new MouseEvent(
                recipeList,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                10,
                10,
                2,      // clickCount = 2 -> 双击
                false
        );
        Arrays.stream(recipeList.getMouseListeners())
                .forEach(listener -> listener.mouseClicked(evt));

        assertSame(r2, opened[0], "onOpenRecipe should be called with the selected Recipe");
    }
}
