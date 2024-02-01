package hu.modeldriven.astah.richdocument;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.ITransactionManager;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IEntity;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.view.IEntitySelectionEvent;
import com.change_vision.jude.api.inf.view.IEntitySelectionListener;
import com.change_vision.jude.api.inf.view.IViewManager;
import hu.modeldriven.swinghtmleditor.SwingHTMLEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RichDocumentPanel extends JPanel implements IEntitySelectionListener {

    private final SwingHTMLEditor editor;

    private transient INamedElement selectedElement;

    private boolean documentChanged = false;

    public RichDocumentPanel() {
        super();
        this.editor = new SwingHTMLEditor();
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.add(editor, BorderLayout.CENTER);

        this.editor.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                onDocumentChanged(documentEvent);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                onDocumentChanged(documentEvent);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                onDocumentChanged(documentEvent);
            }
        });

        try {
            IViewManager viewManager = AstahAPI.getAstahAPI().getViewManager();
            viewManager.getProjectViewManager().addEntitySelectionListener(this);
            viewManager.getDiagramViewManager().addEntitySelectionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDocumentChanged(DocumentEvent documentEvent) {
        this.documentChanged = true;
    }

    @Override
    public void entitySelectionChanged(IEntitySelectionEvent iEntitySelectionEvent) {

        try {
            IViewManager viewManager = AstahAPI.getAstahAPI().getViewManager();
            List<IEntity> selectedEntities = new ArrayList<>();

            selectedEntities.addAll(Arrays.asList(viewManager.getProjectViewManager().getSelectedEntities()));
            selectedEntities.addAll(Arrays.asList(viewManager.getDiagramViewManager().getSelectedElements()));

            Optional<INamedElement> firstModelElement = selectedEntities.stream().filter(INamedElement.class::isInstance).filter(e -> e instanceof IDiagram == false).map(INamedElement.class::cast).findFirst();

            if (this.selectedElement != null && this.documentChanged) {
                saveDefinition(selectedElement, editor.getText());
            }

            if (firstModelElement.isPresent()) {
                onElementSelected(firstModelElement.get());
            } else {
                onNoElementSelected();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onElementSelected(INamedElement namedElement) {
        this.selectedElement = namedElement;
        editor.setText(namedElement.getDefinition());
        this.documentChanged = false;
    }

    void onNoElementSelected() {
        this.selectedElement = null;
        this.editor.setText("");
        this.documentChanged = false;
    }


    private void saveDefinition(INamedElement element, String definition) {
        try {
            AstahAPI api = AstahAPI.getAstahAPI();

            ProjectAccessor accessor = api.getProjectAccessor();
            ITransactionManager transactionManager = accessor.getTransactionManager();

            try {
                transactionManager.beginTransaction();
                element.setDefinition(definition);
                transactionManager.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.abortTransaction();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
