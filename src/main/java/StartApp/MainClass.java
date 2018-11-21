package StartApp;

import BasicClases.*;
import Logic.PdfTranslate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class MainClass extends Application{
    private ArrayList<MonthSchedule> monthSchedule;
    private static int daysInMonth;
    static LocalDate dateOfMonday;
    static File[] weeksUpload;
    private static int weeksCount;
    private static ArrayList<Worker> workers;

    public static ArrayList<GeneratedSchedule> getAllWeeksSchedules() {
        return allWeeksSchedules;
    }

    public static void setAllWeeksSchedules(ArrayList<GeneratedSchedule> allWeeksSchedules) {
        MainClass.allWeeksSchedules = allWeeksSchedules;
    }

    private static ArrayList <GeneratedSchedule> allWeeksSchedules;
    static int dayOfWeekFirstDay;
    static int dayOfWeekLastDay;
    static int breakTime;
    private  static ArrayList<ShiftMatrix> shiftMatrix = new ArrayList<>();

      public static ArrayList<Worker> getWorkers() {
        return workers;
    }

    public static void setDayOfWeekFirstDay(int dayOfWeekFirstDay) {
        MainClass.dayOfWeekFirstDay = dayOfWeekFirstDay;
    }

    public static void setDayOfWeekLastDay(int dayOfWeekLastDay) {
        MainClass.dayOfWeekLastDay = dayOfWeekLastDay;
    }

    int currentday = 0;
    public static void setDaysInMonth(int days) {
        daysInMonth = days;
    }
    public static void setWeeksCount(int weeks){
        weeksCount=weeks;
        weeksUpload = new File[weeks];
    }

    public static void setDateOfMonday(LocalDate dateOfMonday) {
        MainClass.dateOfMonday = dateOfMonday;
    }

    public void start(Stage primaryStage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("UI/UserInterface.fxml"));
       BreakTime.setBreakTime(45);
        //String resourcePath = "../UI/UserInterface.fxml";
       //URL location = getClass().getResource(resourcePath);
       //FXMLLoader fxmlLoader = new FXMLLoader(location);
       //Scene scene = new Scene(fxmlLoader.load());
        Scene scene = new Scene(root,1000,600);
       primaryStage.setScene(scene);
       primaryStage.setTitle("sapEncoder by a.koryakov@auchan.ru");
       primaryStage.getIcons().add(new Image("/images/auch.jpg"));

       //scene.getStylesheets().add(MainClass.class.getResource("../css/appStyle.css").toExternalForm());

       primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setFileForWeek(int i, File upload) {
        weeksUpload[i]=upload;
    }

    public static int getWeeksCount() {
        return weeksCount;
    }

    public static int getDaysInMonth() {
        return daysInMonth;
    }

    public File[] deliver(){
        return weeksUpload;
    }

    public  static void buildWeeks() throws IOException {
        workers = XLSReader.uploadWorkersFromXLS(new File("HostessConfig/hostesses.xls"));
        uploadShiftMatrix();
        allWeeksSchedules = new ArrayList<>();
        PdfTranslate translator = new PdfTranslate();
        int dow = dateOfMonday.getDayOfWeek().getValue();
        int cou=0;
        for (File file: weeksUpload) {
            allWeeksSchedules.add(
                    translator.translatePdf(file,dateOfMonday.minusDays(dow).plusDays(cou*7)));
            cou++;
        }
        workers = UknownHostessUpdater.updateHostessFromWorkerSchedule(workers,allWeeksSchedules.get(0).getWorkerSchedules());
        for (Worker worker:workers) {
            worker.setUpFromGeneratedSchedules(allWeeksSchedules,dayOfWeekFirstDay+1,dayOfWeekLastDay+1,weeksCount);
        }


    }

    public static ArrayList<ShiftMatrix> getShiftMatrix() {
        return shiftMatrix;
    }


    private static void uploadShiftMatrix(){
        File table = new File("HostessConfig/shifts.xls");
        try {

            shiftMatrix = XLSReader.uploadShiftsFromXls(table);
        } catch (IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Файл shifts.xls не найден или отсутствует",ButtonType.CLOSE);
            alert.show();
        }

    }


}
