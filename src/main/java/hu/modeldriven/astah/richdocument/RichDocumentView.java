package hu.modeldriven.astah.richdocument;

import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

public class RichDocumentView extends JPanel implements IPluginExtraTabView {

    public RichDocumentView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(createContentPane(), BorderLayout.CENTER);
    }

    private Container createContentPane() {
        return new RichDocumentPanel();
    }

    @Override
    public void addSelectionListener(ISelectionListener listener) {
        // nothing to do here
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getDescription() {
        return "Rich Document View";
    }

    @Override
    public String getTitle() {
        return "Rich Document";
    }

    public void activated() {
        // nothing to do here
    }

    public void deactivated() {
        // nothing to do here
    }
}
