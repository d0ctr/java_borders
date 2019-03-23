/*
Игра Borders расчитана на двух игроков, последовательно совершающих ходы.
Полем игры является сетка, в форме квдрата.
Во время хода игрок закрашивает одну границу шириной в одну клетку.
Если игрок закрашивает своим ходом все границы вокруг клетки 1х1, он её захватывает.
Если игрок захватывает клетку, он может закрасить еще одну границу.
Цель игры захватить больше половины клеток на поле.

 Сетка размечается с левого верхнего угла до нижнего правого
 В ячейках на пересечении четных столбцов и рядов находятся Х
 В ячейках на пересечении нечетных столбцов и четных рядов находятся  вертикальные линии
 В ячейках на пересечении четных столбцов и нечетных рядов находятся горизональные линии

 Нужно:
 Проработать евенты нажатия кнопок:
    Если уже нажата, то не отжимать
    Если нажаты все вокруг клетки, то лейбл в клетке сделать видимым(жедательно при этом еще разобраться с окраской, но это потом)
 Сделать поочередные ходы со всеми вытекающими
    Разные цвета захваченных целей
    Лейбл показывающие какой игрок сейчас делает ход
 Собрать apk
 */
package borders_javafx;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import java.util.List;

public class Borders_main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Borders");
        Parameters params = getParameters();
        List<String> args = params.getRaw(); // Получаем параметр командной строки, который отвечает за размер поля
        int grid_size = Integer.parseInt(args.get(0).toString()) * 2 + 1; // Количество колонн и рядов игровой сетки
        ToggleButton[][] vl = new ToggleButton[grid_size][grid_size]; // Вертикальные линии
        ToggleButton[][] hl = new ToggleButton[grid_size][grid_size]; // Горизонтальные линии
        Label[][] mark = new Label[grid_size][grid_size]; // Лейблы в  которых будут записаны Х, как факт захвата клетки
        GridPane game_grid = new GridPane();
        game_grid.setAlignment(Pos.CENTER);
        for(int i = 0; i < grid_size; i++){ // Цикл создающий игровую сетку
            if((i % 2) == 0){
                game_grid.getColumnConstraints().add(new ColumnConstraints(5));
                game_grid.getRowConstraints().add(new RowConstraints(5));
            }
            else{
                game_grid.getColumnConstraints().add(new ColumnConstraints(30));
                game_grid.getRowConstraints().add(new RowConstraints(30));
            }
        }
        //game_grid.setGridLinesVisible(true);
        for(int v = 0; v < grid_size; v++){ // Цикл заполняющий ячейки в сетке
            for(int h = 0; h < grid_size; h++){
                if((v % 2) == 0 && (h % 2) != 0){ // Вертикальные линии
                    vl[v][h] = new ToggleButton();
                    vl[v][h].getStyleClass().add("vl");
                    game_grid.add(vl[v][h], v, h);
                    if(v == 0 || v == grid_size-1){ // Крайние линии по дефолту уже закрашены
                        vl[v][h].setSelected(true);
                    }
                }
                else if((v % 2) != 0 && (h % 2) == 0){ // Горизонтальные линии
                    hl[v][h] = new ToggleButton();
                    hl[v][h].getStyleClass().add("hl");
                    game_grid.add(hl[v][h], v, h);
                    if(h == 0 || h == grid_size-1){ // Крайние линии по дефолту уже закрашены
                        hl[v][h].setSelected(true);
                    }
                }
                else if((v % 2) != 0 && (h % 2) != 0 ){ // Лейблы, в которые кладутся Х
                    mark[v][h] = new Label("X");
                    mark[v][h].getStyleClass().add("mark");
                    game_grid.add(mark[v][h], v, h);
                    mark[v][h].setVisible(false); // По дефолту невидимые
                    GridPane.setHalignment(mark[v][h], HPos.CENTER);
                }

            }
        }
        Scene scene = new Scene(game_grid, 600, 600);
        scene.getStylesheets().add(getClass().getResource("borders.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
