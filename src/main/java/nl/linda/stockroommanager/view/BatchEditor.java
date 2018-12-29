package nl.linda.stockroommanager.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import nl.linda.stockroommanager.controller.BatchController;
import nl.linda.stockroommanager.model.Batch;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@SpringComponent
@UIScope
public class BatchEditor extends FormLayout {
    private final BatchController batchController;
    private Batch batch;

    private Binder<Batch> binder = new Binder<>(Batch.class);
    private ChangeHandler changeHandler;

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private Button cancel = new Button("Cancel");
    private Div saveError = new Div(new Label("Please fill in all the fields"));

    private TextField name = new TextField("Name");
    private TextField batchId = new TextField("BatchId");
    private TextField weight = new TextField("Weight");
    private TextField xPosition = new TextField("Position X");
    private TextField yPosition = new TextField("Position Y");

    @Autowired
    public BatchEditor(BatchController batchController) {
        this.batchController = batchController;

        saveError.setVisible(false);
        saveError.setClassName("save-error");

        HorizontalLayout messages = new HorizontalLayout(saveError);
        HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
        VerticalLayout parent = new VerticalLayout(actions, messages);
        parent.setClassName("space");
        add(name, batchId, weight, xPosition, yPosition, parent);

        // bind using naming convention
        // Had to manually convert them all, so I couldn't simply do bindInstanceFields(this)
        // binder.bindInstanceFields(this);
        binder.forField(name)
                .bind(Batch::getName, Batch::setName);
        binder.forField(batchId)
                .withConverter(new StringToLongConverter("Must be a Long"))
                .withValidator(Objects::nonNull, "BatchId should not be empty")
                .bind(Batch::getBatchId, Batch::setBatchId);
        binder.forField(weight)
                .withConverter(new StringToLongConverter("Must be a Long"))
                .bind(Batch::getWeight, Batch::setWeight);
        binder.forField(xPosition)
                .withValidator(Objects::nonNull, "X position should not be empty")
                .bind(Batch::getxPosition, Batch::setxPosition);
        binder.forField(yPosition)
                .withConverter(new StringToIntegerConverter("Must be an int"))
                .withValidator(Objects::nonNull, "Y position should not be empty")
                .bind(Batch::getyPosition, Batch::setyPosition);

        setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        setWidth("25%");
        setClassName("form-space");
        setVisible(false);
    }

    private void createNewBatch() {
        if (this.batch.getBatchId() != null && this.batch.getxPosition() != null && !this.batch.getxPosition().isEmpty() && this.batch.getyPosition() != 0) {
            // TODO: create regex for correct value input (like xPos should only be 1 char and no number)
            batchController.createBatch(this.batch);
        }
        changeHandler.onChange();
    }

    private void deleteBatch(Batch batch) {
        if (batch.getId() != null) {
            // deleted this.batch, because using batch it removed all previously selected beans
            batchController.deleteBatch(this.batch);
        }
        changeHandler.onChange();
    }

    void editBatch(Batch batch) {
        if (batch.getId() != null) {
            this.batch = batchController.findBatch(batch.getId());
        } else {
            this.batch = batch;
        }

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(this.batch);

        save.addClickListener(event -> {
            if (this.batch.getBatchId() != null && this.batch.getxPosition() != null && !this.batch.getxPosition().isEmpty() && this.batch.getyPosition() != 0) {
                createNewBatch();
            } else {
                saveError.setVisible(true);
            }
        });
        delete.addClickListener(event -> deleteBatch(batch));
        cancel.addClickListener(event -> {
            saveError.setVisible(false);
            setVisible(false);
        });

        setVisible(true);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when save is clicked
        changeHandler = h;
    }
}
