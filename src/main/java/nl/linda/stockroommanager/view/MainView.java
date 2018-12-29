package nl.linda.stockroommanager.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import nl.linda.stockroommanager.controller.BatchController;
import nl.linda.stockroommanager.model.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route
@Theme(value = Lumo.class, variant = Lumo.DARK)
@StyleSheet("shared-styles.scss")
public class MainView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(MainView.class);

    private final BatchController batchController;

    private Button addNewBatch;

    private final BatchEditor batchEditor;

    private FormLayout form;

    public MainView(BatchController batchController, BatchEditor batchEditor) {
        this.batchController = batchController;
        this.batchEditor = batchEditor;

        this.addNewBatch = new Button("Add new batch");
        addNewBatch.addClickListener(event -> {
            batchEditor.editBatch(new Batch("", 0L, 0L, "", 0));
        });
        addNewBatch.setClassName("form-space");

        this.form = new FormLayout();
        createFormLayout();

        add(addNewBatch, batchEditor, form);

        // If the save button is clicked, changeHandler will be notified and do this:
        batchEditor.setChangeHandler(() -> {
            // Remove BatchEditor form
            batchEditor.setVisible(false);
            // Recreate form and add form to view
            createFormLayout();
            add(form);
            listBatches();
        });

//      Initialize batch grid
        listBatches();
    }

    private void createFormLayout() {
        form.removeAll();
        Div topHeader0 = new Div(new Label(" "));
        topHeader0.setClassName("first-column");
        Div topHeaderA = new Div(new Label("A"));
        Div topHeaderB = new Div(new Label("B"));
        Div topHeaderC = new Div(new Label("C"));
        Div topHeaderD = new Div(new Label("D"));
        Div topHeaderE = new Div(new Label("E"));
        Div topHeaderF = new Div(new Label("F"));
        Div topHeaderG = new Div(new Label("G"));
        Div topHeaderH = new Div(new Label("H"));
        Div topHeaderI = new Div(new Label("I"));
        topHeaderA.setClassName("text-align");
        topHeaderB.setClassName("text-align");
        topHeaderC.setClassName("text-align");
        topHeaderD.setClassName("text-align");
        topHeaderE.setClassName("text-align");
        topHeaderF.setClassName("text-align");
        topHeaderG.setClassName("text-align");
        topHeaderH.setClassName("text-align");
        topHeaderI.setClassName("text-align");
        form.add(topHeader0, topHeaderA, topHeaderB, topHeaderC, topHeaderD, topHeaderE, topHeaderF, topHeaderG, topHeaderH, topHeaderI);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 10));
        form.setClassName("grid");
    }

    private void listBatches() {
        List<Batch> batches = batchController.findAllBatches();

        int yPosition = 1;
        char xPosition = 'A';

        // Sorting batches list first by yPostion and then by xPosition
        Comparator<Batch> comparator = Comparator.comparing(Batch::getyPosition);
        comparator = comparator.thenComparing(Comparator.comparing(Batch::getxPosition));
        // Sort the stream:
        Stream<Batch> batchStream = batches.stream().sorted(comparator);
        // Make sure that the output is as expected:
        List<Batch> sortedBatches = batchStream.collect(Collectors.toList());

        // Add new row number (it's the first column)
        Label firstRow = new Label(Integer.toString(yPosition));
        firstRow.setClassName("first-column");
        Div firstRowDiv = new Div(firstRow);
        firstRowDiv.setClassName("first-column-div");
        form.add(firstRowDiv);

        for (Batch batch : sortedBatches) {
            // when batches of next row come in, empty divs will be added to the end of the previous row to fill it
            while (batch.getyPosition() > yPosition) {
                while (xPosition != 'J') {
                    createEmptyDiv(String.valueOf(xPosition), yPosition);
                    xPosition += 1;
                }
                // Moving on to next row and setting the row number in the first cell
                yPosition += 1;
                Label newRow = new Label(Integer.toString(yPosition));
                newRow.setClassName("first-column");
                Div newRowDiv = new Div(newRow);
                newRowDiv.setClassName("first-column-div");
                form.add(newRowDiv);
                // Start again with column A
                xPosition = 'A';
            }

            while (!batch.getxPosition().equals(String.valueOf(xPosition)) && batch.getyPosition() == yPosition) {
                // add empty divs to form as long as there is no match
                createEmptyDiv(String.valueOf(xPosition), yPosition);
                xPosition += 1;
            }
            // batch matches the position, so adding it to the grid
            createBatchDiv(batch);
            xPosition += 1;
        }

        // Add emtpy divs until the end of the grid after all batches have been inserted
        while (xPosition != 'J') {
            createEmptyDiv(String.valueOf(xPosition), yPosition);
            xPosition += 1;
        }
    }

    private void createEmptyDiv(String xPos, int yPos) {
        Label name = new Label("name");
        Label batchId = new Label("0");
        Label weight = new Label("0");
        Label position = new Label(xPos + yPos);
        name.setClassName("invisible-label");
        batchId.setClassName("invisible-label");
        weight.setClassName("invisible-label");
        position.setClassName("invisible-label");
        Div emptyDiv = new Div(name, batchId, weight, position);
        emptyDiv.setClassName("tile invisible");
        emptyDiv.addClickListener(event -> {
            batchEditor.editBatch(new Batch("", 0L, 0L, xPos, yPos));
        });

        form.add(emptyDiv);
    }

    private void createBatchDiv(Batch batch) {
        Label name = new Label(batch.getName());
        Label batchId = new Label("ID: " + batch.getBatchId().toString());
        Label weight = new Label(batch.getWeight().toString() + " kg");
        Label position = new Label(batch.getxPosition() + batch.getyPosition());
        name.setClassName("label");
        batchId.setClassName("label");
        weight.setClassName("label");
        position.setClassName("label");
        Div batchDiv = new Div(name, batchId, weight, position);
        batchDiv.setClassName("tile");
        batchDiv.addClickListener(event -> {
            batchEditor.editBatch(batch);
        });

        form.add(batchDiv);
    }

}
