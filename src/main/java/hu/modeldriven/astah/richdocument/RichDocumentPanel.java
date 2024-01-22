package hu.modeldriven.astah.richdocument;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.view.IEntitySelectionEvent;
import com.change_vision.jude.api.inf.view.IEntitySelectionListener;
import com.change_vision.jude.api.inf.view.IViewManager;
import hu.modeldriven.swinghtmleditor.SwingHTMLEditor;

import javax.swing.*;
import java.awt.BorderLayout;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RichDocumentPanel extends JPanel implements IEntitySelectionListener {

    private final SwingHTMLEditor editor;

    public RichDocumentPanel(){
        super();
        this.editor = new SwingHTMLEditor();
        initComponents();
    }

    private void initComponents() {

        System.err.println("Class loader is = " + getClass().getClassLoader());

        InputStream is = getClass().getClassLoader().getResourceAsStream("images/x24/add.png");

        System.err.println("Input stream is = " + is);

        this.setLayout(new BorderLayout());
        this.add(editor, BorderLayout.CENTER);

        try {
            IViewManager viewManager = AstahAPI.getAstahAPI().getViewManager();
            viewManager.getProjectViewManager().addEntitySelectionListener(this);
            viewManager.getDiagramViewManager().addEntitySelectionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void entitySelectionChanged(IEntitySelectionEvent iEntitySelectionEvent) {
        try {
            IViewManager viewManager = AstahAPI.getAstahAPI().getViewManager();
            List<IEntity> selectedEntities = new ArrayList<>();

            selectedEntities.addAll(Arrays.asList(viewManager.getProjectViewManager().getSelectedEntities()));
            selectedEntities.addAll(Arrays.asList(viewManager.getDiagramViewManager().getSelectedElements()));

            Optional<INamedElement> firstNamedElement = selectedEntities.stream()
                    .filter(entity -> entity instanceof INamedElement)
                    .map(INamedElement.class::cast)
                    .findFirst();

            if (firstNamedElement.isPresent()) {
                onElementSelected(firstNamedElement.get());
            } else {
                onNoElementSelected();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onElementSelected(INamedElement namedElement){
        editor.setText(namedElement.getDefinition());
    }

    void onNoElementSelected(){
        editor.setText("");
    }

}
