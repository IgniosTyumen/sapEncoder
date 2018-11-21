package Controllers;

import BasicClases.BreakTime;
import BasicClases.NormaliseMethod;
import BasicClases.Worker;
import BasicClases.WorkerPOJO;
import Reports.Reports;
import StartApp.MainClass;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;

public class UIController {
    @FXML
    private TabPane tp_Main;

    @FXML
    private Tab tConfigure;

    @FXML
    private Button bHostessInfoOpen;

    @FXML
    private ToggleButton bHostessCommit;

    @FXML
    private Button bShiftsInfoOpen;

    @FXML
    private ToggleButton bShiftsCommit;


    @FXML
    private ToggleButton bClosedReportsCommit;

    @FXML
    private Tab tUpload;

    @FXML
    private DatePicker dpMonthPicker;

    @FXML
    private Button bMonthCofirm;

    @FXML
    private Button bUploadWeek1;

    @FXML
    private Circle cUploadWeek1;

    @FXML
    private Label lUploadWeek1;

    @FXML
    private Button bUploadWeek2;

    @FXML
    private Circle cUploadWeek2;

    @FXML
    private Button bNonFoundShifts;

    @FXML
    private Label lUploadWeek2;

    @FXML
    private Button bUploadWeek3;

    @FXML
    private Circle cUploadWeek3;

    @FXML
    private Label lUploadWeek3;

    @FXML
    private Button bUploadWeek4;

    @FXML
    private Circle cUploadWeek4;

    @FXML
    private Label lUploadWeek4;

    @FXML
    private Button bUploadWeek5;

    @FXML
    private Circle cUploadWeek5;

    @FXML
    private Label lUploadWeek5;

    @FXML
    private Button bUploadWeek6;

    @FXML
    private Circle cUploadWeek6;

    @FXML
    private Label lUploadWeek6;

    @FXML
    private Button bCodeShifts;

    @FXML
    private Tab tQuickStatistic;

    @FXML
    private TableView<WorkerPOJO> tvShifts;

    @FXML
    private TableColumn<WorkerPOJO, Integer> ctv_number;

    @FXML
    private TableColumn<WorkerPOJO, String> ctv_Surname;

    @FXML
    private TableColumn<WorkerPOJO, String> ctv_Name;

    @FXML
    private TableColumn<WorkerPOJO, String> ctv_Contract;

    @FXML
    private TableColumn<WorkerPOJO, String> ctv_Manager;

    @FXML
    private TextField bNormalTimeInput;


    @FXML
    private TableColumn<WorkerPOJO, Integer> ctv_dayOffs;
    @FXML
    private TableColumn<WorkerPOJO, Integer> ctv_OTdays;
    @FXML
    private TableColumn<WorkerPOJO, Integer> ctv_AbnormalDays;

    @FXML
    private TableColumn<WorkerPOJO, Integer> ctv_workDays;

    @FXML
    private TableColumn<WorkerPOJO, Double> ctv_SapHours;

    @FXML
    private TableColumn<WorkerPOJO, Double> ctv_SaturneHours;

    @FXML
    public Slider sl_break;

    @FXML
    private TableColumn<WorkerPOJO, Double> ctv_NormalHours;

    @FXML
    private Button bNormalize;

    @FXML
    private Button bNextReports;

    @FXML
    private Tab tReports;

    @FXML
    private Button bSaveShiftCodes;

    @FXML
    private Button bAbsentReport;

    @FXML
    private Button bUknownStatistic;

    @FXML
    private Button bReportMovedShift;

    @FXML
    private Button bBusySausageSave;

    @FXML
    private Label lbWeeksInPeriod;

    @FXML
    private HBox hbWeek1;

    @FXML
    private HBox hbWeek2;
    @FXML
    private HBox hbWeek3;
    @FXML
    private HBox hbWeek4;
    @FXML
    private HBox hbWeek5;
    @FXML
    private HBox hbWeek6;
    @FXML
    private HBox hbStartCalculate;

    private ObservableList<WorkerPOJO> workersOL = FXCollections.observableArrayList();
    private boolean shiftsConfirmed = false;
    private boolean hostessConfirmed = false;
    private boolean reportsConfirmed = false;
    private boolean[] weeksUploaded;
    private static String month;
    public static Date datePicked;
    public static Double normalFor40;

    @FXML
    public void initialize() {
        bNormalTimeInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    bNormalTimeInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        bClosedReportsCommit.setStyle("-fx-background-color: red");
        bShiftsCommit.setStyle("-fx-background-color: red");
        bHostessCommit.setStyle("-fx-background-color: red");

        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        dpMonthPicker.setConverter(converter);

    }

    public void openShiftsInfo() {
        displayURL("file://" + "./HostessConfig/shifts.xls");
    }


    public static void displayURL(String url) {
        try {
            if (url.toLowerCase().startsWith("file://"))
                url = url.substring(7);
            Object desktop = Class.forName("java.awt.Desktop").getMethod(
                    "getDesktop", new Class[0]).invoke(null, new Object[0]);
            desktop.getClass().getMethod("open",
                    new Class[]{java.io.File.class}).invoke(desktop,
                    new Object[]{new java.io.File(url)});
        } catch (Throwable ex1) {
            if (!url.toLowerCase().startsWith("file://"))
                url = "file://" + url;
            boolean windows = false;
            String os = System.getProperty("os.name");
            if (os != null && os.startsWith("Windows"))
                windows = true;

            String cmd = null;
            try {
                if (windows) {
                    cmd = "rundll32 url.dll,FileProtocolHandler " + url;
                    Process p = Runtime.getRuntime().exec(cmd);
                } else {
                    cmd = "netscape -remote openURL(" + url + ")";
                    Process p = Runtime.getRuntime().exec(cmd);
                    try {
                        int exitCode = p.waitFor();
                        if (exitCode != 0) {
                            cmd = "netscape " + url;
                            p = Runtime.getRuntime().exec(cmd);
                        }
                    } catch (InterruptedException ex) {
                        System.out.println(
                                "Error while showing local document (cmd='"
                                        + cmd + "':\n" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error while local document (cmd='" + cmd
                        + "':\n");
                ex.printStackTrace();
            }
        }
    }

    public void openHostessInfo(ActionEvent actionEvent) {
        try {
            displayURL("file://" + "./HostessConfig/hostesses.xlsx");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка существования файла");
            alert.setHeaderText(null);
            alert.setContentText("Файл hostesses.xlsx не найден");
            alert.show();
        }
    }

    public void hostessConfirm(ActionEvent actionEvent) {
        hostessConfirmed = true;
        bHostessCommit.setStyle("-fx-background-color: green");
        bHostessCommit.setText("Подтверждено");
        checkReportsToMove();
    }

    public void shiftsConfirm(ActionEvent actionEvent) {
        shiftsConfirmed = true;
        bShiftsCommit.setStyle("-fx-background-color: green");
        bShiftsCommit.setText("Подтверждено");
        checkReportsToMove();
    }


    public void reportsConfirm(ActionEvent actionEvent) {
        reportsConfirmed = true;
        bClosedReportsCommit.setStyle("-fx-background-color: green");
        bClosedReportsCommit.setText("Подтверждено");
        checkReportsToMove();
    }

    public static String getMonth() {
        return month;
    }

    public static void setMonth(String month) {
        UIController.month = month;
    }

    public void checkReportsToMove(){
     if (reportsConfirmed && hostessConfirmed && shiftsConfirmed && hostessConfirmed){
         Double breaktime=sl_break.getValue();
         BreakTime.setBreakTime(breaktime.intValue());
         tConfigure.setDisable(true);
         tp_Main.getSelectionModel().select(tUpload);
     }


    }
    public void monthWeeksCalculate(ActionEvent actionEvent) {
        bMonthCofirm.setVisible(false);
        LocalDate sd = LocalDate.of(dpMonthPicker.getValue().getYear(), dpMonthPicker.getValue().getMonth(), 1);
        MainClass.setDateOfMonday(sd);
        LocalDate fd = LocalDate.of(dpMonthPicker.getValue().getYear(), dpMonthPicker.getValue().getMonth(), dpMonthPicker.getValue().lengthOfMonth());
        int week1 = sd.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        MainClass.setDayOfWeekFirstDay(dayOfWeek(DayOfWeek.from(sd)));
        int weekLast = fd.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        MainClass.setDayOfWeekLastDay(dayOfWeek(DayOfWeek.from(fd)));
        Integer weeksDiap;
        if (weekLast<week1){
            weekLast = fd.minusWeeks(1).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)+1;
            weeksDiap = weekLast - week1 + 1;

        } else {
             weeksDiap = weekLast - week1 + 1;
        }
        lbWeeksInPeriod.setText("Необходимо загрузить " + weeksDiap.toString() + " недель для месяца " + sd.getMonth().toString());
        UIController.month = sd.getMonth().toString();
        showVisibleWeeksUpload(weeksDiap);
        MainClass.setDaysInMonth(dpMonthPicker.getValue().lengthOfMonth());
        MainClass.setWeeksCount(weeksDiap);
        if (weeksUploaded == null) weeksUploaded = new boolean[weeksDiap];
        dpMonthPicker.setVisible(false);
        datePicked=new Date(sd.getYear(),sd.getMonth().getValue(),sd.getDayOfMonth());
    }

    private int dayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return 0;
            case TUESDAY:
                return 1;
            case THURSDAY:
                return 3;
            case WEDNESDAY:
                return 2;
            case FRIDAY:
                return 4;
            case SATURDAY:
                return 5;
            case SUNDAY:
                return 6;
            default:
                return 7;
        }
    }

    public void dateInputed(ActionEvent actionEvent) {
        bMonthCofirm.setDisable(false);
    }

    private void showVisibleWeeksUpload(int i) {
        hbWeek1.setVisible(true);
        hbWeek2.setVisible(true);
        switch (i) {
            case 3: {
                hbWeek3.setVisible(true);
                break;
            }
            case 4: {
                hbWeek3.setVisible(true);
                hbWeek4.setVisible(true);
                break;
            }
            case 5: {
                hbWeek3.setVisible(true);
                hbWeek4.setVisible(true);
                hbWeek5.setVisible(true);
                break;
            }
            case 6: {
                hbWeek3.setVisible(true);
                hbWeek4.setVisible(true);
                hbWeek5.setVisible(true);
                hbWeek6.setVisible(true);
                break;
            }
        }
    }

    public void uploadWeekPdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        try {
            fileChooser.setInitialDirectory(new File(".."));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        fileChooser.setTitle("Загрузите файл pdf содержащий расписание");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".pdf", "*.pdf");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File choosed = fileChooser.showOpenDialog(new Alert(Alert.AlertType.INFORMATION, "S", ButtonType.APPLY).getOwner());

        Button activator = (Button) actionEvent.getSource();
        if (choosed!=null) {
            switch (activator.getId()) {
                case "bUploadWeek1": {
                    uploadWeek(0, choosed);
                    cUploadWeek1.setFill(Color.GREEN);
                    lUploadWeek1.setText(choosed.getName().toString());
                    break;
                }
                case "bUploadWeek2": {
                    uploadWeek(1, choosed);
                    cUploadWeek2.setFill(Color.GREEN);
                    lUploadWeek2.setText(choosed.getName().toString());
                    break;
                }
                case "bUploadWeek3": {
                    uploadWeek(2, choosed);
                    cUploadWeek3.setFill(Color.GREEN);
                    lUploadWeek3.setText(choosed.getName().toString());
                    break;
                }
                case "bUploadWeek4": {
                    uploadWeek(3, choosed);
                    cUploadWeek4.setFill(Color.GREEN);
                    lUploadWeek4.setText(choosed.getName().toString());
                    break;
                }
                case "bUploadWeek5": {
                    uploadWeek(4, choosed);
                    cUploadWeek5.setFill(Color.GREEN);
                    lUploadWeek5.setText(choosed.getName().toString());
                    break;
                }
                case "bUploadWeek6": {
                    uploadWeek(5, choosed);
                    cUploadWeek6.setFill(Color.GREEN);
                    lUploadWeek6.setText(choosed.getName().toString());
                    break;
                }
            }
        }
    }

    private void uploadWeek(int i, File upload) {
        MainClass.setFileForWeek(i, upload);
        weeksUploaded[i] = true;
        if (isAllReadyForConvertation()) hbStartCalculate.setVisible(true);
    }

    private boolean isAllReadyForConvertation() {
        boolean result = true;
        if (!reportsConfirmed) return false;
        if (!shiftsConfirmed) return false;
        if (!hostessConfirmed) return false;
        for (boolean b : weeksUploaded
                ) {
            if (!b) return false;

        }
        bCodeShifts.setVisible(true);
        return true;
    }

    public void generateMonthShifts(ActionEvent actionEvent) throws IOException {
        try {
            normalFor40 = Double.parseDouble(bNormalTimeInput.getText());
        }catch (Exception ex){
            normalFor40 = 168.0;
        }
        MainClass.buildWeeks();
        uploadTableView(MainClass.getWorkers());
    }

    public void uploadTableView(ArrayList<Worker> workers) {
        ArrayList<WorkerPOJO> workerPOJOS = new ArrayList<>();
        tvShifts.setEditable(true);
        ctv_number.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Integer>("number"));
        ctv_Surname.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, String>("surname"));
        ctv_Name.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, String>("name"));
        ctv_Contract.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, String>("contractVisible"));
        ctv_dayOffs.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Integer>("dayOffs"));
        ctv_workDays.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Integer>("workDays"));
        ctv_SapHours.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Double>("workedMinutesBySup"));
        ctv_SaturneHours.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Double>("workedMinutesBySaturne"));
        ctv_Manager.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, String>("managerName"));
        ctv_AbnormalDays.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Integer>("abnormalDays"));
        ctv_OTdays.setCellValueFactory(new PropertyValueFactory<WorkerPOJO, Integer>("OTdays"));
//        ctv_NormalHours.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<WorkerPOJO, Double>, ObservableValue<Double>>() {
//            @Override
//            public ObservableValue<Double> call(TableColumn.CellDataFeatures<WorkerPOJO, Double> param) {
//                return new ReadOnlyObjectWrapper(new SimpleDoubleProperty(param.getValue().getNormalHours())).getReadOnlyProperty();
//            }
//        });
      ctv_NormalHours.setCellFactory(TextFieldTableCell.<WorkerPOJO,Double>forTableColumn(new DoubleStringConverter()));
        ctv_NormalHours.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
       ctv_NormalHours.setCellValueFactory(new PropertyValueFactory<WorkerPOJO,Double>("normalHours"));

//        ctv_NormalHours.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WorkerPOJO, Double>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent<WorkerPOJO, Double> event) {
//                acceptHour(event);
//            }
//        });
        uploadTVData(workers);
        tvShifts.setItems(workersOL);
        tUpload.setDisable(true);
        tReports.setDisable(false);
        tp_Main.getSelectionModel().select(tQuickStatistic);

    }


    public void uploadTVData(ArrayList<Worker> workers) {
        for (Worker worker : workers
                ) {
            workersOL.add(new WorkerPOJO(worker));
        }
    }

public void normalizeHours(){
        for (WorkerPOJO worker:workersOL){
            worker.getWorker().setNormalized(false);
            worker.normalise(NormaliseMethod.NORMALIZE_BY_SATURNE);

        }
        tvShifts.refresh();
}

    public void saveShiftCodes(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Введите файл для сохранения");
        fc.setInitialDirectory(new File (".."));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".xls", "*.xls");
        fc.getExtensionFilters().add(extensionFilter);
        File choosed = fc.showSaveDialog(new Alert(Alert.AlertType.INFORMATION, "S", ButtonType.APPLY).getOwner());
        try {
            Reports.saveEncodedCodesToXLS(choosed,workersOL);
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

//    public void saveBusySausage(ActionEvent actionEvent) {
//        FileChooser fc = new FileChooser();
//        fc.setTitle("Введите файл для сохранения");
//        fc.setInitialDirectory(new File (".."));
//        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".xls", "*.xls");
//        fc.getExtensionFilters().add(extensionFilter);
//        File choosed = fc.showSaveDialog(new Alert(Alert.AlertType.INFORMATION, "S", ButtonType.APPLY).getOwner());
//        try {
//            Reports.saveBusySausage(choosed,workersOL);
//        } catch (IOException ex){
//            ex.printStackTrace();
//        }
//    }

    public void saveAbsentReport(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Введите файл для сохранения");
        fc.setInitialDirectory(new File (".."));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".xls", "*.xls");
        fc.getExtensionFilters().add(extensionFilter);
        File choosed = fc.showSaveDialog(new Alert(Alert.AlertType.INFORMATION, "S", ButtonType.APPLY).getOwner());
        try {
            Reports.saveAbsent(choosed,workersOL);
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void normaliseMonth(ActionEvent actionEvent) {
        for (WorkerPOJO worker:workersOL){
            worker.getWorker().setNormalized(false);
            worker.normalise(NormaliseMethod.NORMALIZE_BY_CONTRACT);

        }
        tvShifts.refresh();
    }

    public void nextPage(ActionEvent actionEvent) {
    tp_Main.getSelectionModel().select(tReports);
    }

    public void changeBreakTime(DragEvent dragEvent) {

    }


    public void acceptHour(TableColumn.CellEditEvent<WorkerPOJO,Double> workerPOJODoubleCellEditEvent) {
        WorkerPOJO wp = tvShifts.getSelectionModel().getSelectedItem();
        wp.setNormalHours(workerPOJODoubleCellEditEvent.getNewValue());

    }

    public void saveNonFoundShifts(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Введите файл для сохранения");
        fc.setInitialDirectory(new File (".."));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(".xls", "*.xls");
        fc.getExtensionFilters().add(extensionFilter);
        File choosed = fc.showSaveDialog(new Alert(Alert.AlertType.INFORMATION, "S", ButtonType.APPLY).getOwner());
        try {
            Reports.saveMissingShifts(choosed);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
