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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;

public class Borders_main extends Application {
    @Override
    public void start(Stage primaryStage) {
        int grid_size = 0;
        TextInputDialog grid = new TextInputDialog();
        grid.setTitle("Первичная настройка");
        grid.setHeaderText("Введите размер сетки(n)");
         // Получаем параметр командной строки, который отвечает за размер поля
        try {
            grid.showAndWait();
            grid_size = Integer.parseInt(grid.getEditor().getText()) * 2 + 1;// Количество колонн и рядов игровой сетки
        }
        catch(NumberFormatException e) { }
        primaryStage.setTitle("Borders: Red player's turn");
        ToggleButton[][] vhl = new ToggleButton[grid_size][grid_size]; // Линии
        Label[][] mark = new Label[grid_size][grid_size]; // Лейблы в  которых будут записаны Х, как факт захвата клетки
        GridPane game_grid = new GridPane();
        Scene scene = new Scene(game_grid, 18*grid_size, 18*grid_size, Color.RED);
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
        for(int v = 0; v < grid_size; v++){ // Цикл заполняющий ячейки в сетке
            for(int h = 0; h < grid_size; h++){
                if((v % 2) == 0 && (h % 2) != 0){ // Вертикальные линии
                    vhl[v][h] = new ToggleButton();
                    vhl[v][h].getStyleClass().add("vl");
                    game_grid.add(vhl[v][h], v, h);
                    if(v == 0 || v == grid_size-1){ // Крайние линии по дефолту уже закрашены
                        vhl[v][h].setSelected(true);
                    }

                }
                else if((v % 2) != 0 && (h % 2) == 0){ // Горизонтальные линии
                    vhl[v][h] = new ToggleButton();
                    vhl[v][h].getStyleClass().add("hl");
                    game_grid.add(vhl[v][h], v, h);
                    if(h == 0 || h == grid_size-1){ // Крайние линии по дефолту уже закрашены
                        vhl[v][h].setSelected(true);
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
        for(int v = 0; v < grid_size; v++) { // Цикл заполняющий ячейки в сетке
            for (int h = 0; h < grid_size; h++) {
                if((v % 2) != 0 && (h % 2) == 0) {
                    final int ver = v;
                    final int hor = h;
                    vhl[v][h].setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            boolean switch_color = true;
                            if (!vhl[ver][hor].isSelected()) {
                                vhl[ver][hor].setSelected(true);
                            } else {
                                vhl[ver][hor].setSelected(true);
                                //Head
                                if (vhl[ver - 1][hor - 1].isSelected()
                                        && vhl[ver][hor - 2].isSelected()
                                        && vhl[ver + 1][hor - 1].isSelected()) {
                                    mark[ver][hor - 1].setVisible(true);
                                    mark[ver][hor - 1].setTextFill(scene.getFill());
                                    switch_color = false;
                                }
                                //Tail
                                if (vhl[ver - 1][hor + 1].isSelected()
                                        && vhl[ver][hor + 2].isSelected()
                                        && vhl[ver + 1][hor + 1].isSelected()) {
                                    mark[ver][hor + 1].setVisible(true);
                                    mark[ver][hor + 1].setTextFill(scene.getFill());
                                    switch_color = false;
                                }
                            }
                            if(switch_color) {
                                if(scene.getFill() == Color.RED){

                                    primaryStage.setTitle("Borders: Blue player's turn");
                                    scene.setFill(Color.BLUE);
                                }
                                else{

                                    primaryStage.setTitle("Borders: Red player's turn");
                                    scene.setFill(Color.RED);
                                }
                            }
                        }
                    });
                }
                else if((v % 2) == 0 && (h % 2) != 0) {
                    final int ver = v;
                    final int hor = h;
                    vhl[v][h].setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            boolean switch_color = true;
                            if (!vhl[ver][hor].isSelected()) {
                                vhl[ver][hor].setSelected(true);
                            } else {
                                vhl[ver][hor].setSelected(true);
                                //Left-wing
                                if (vhl[ver - 2][hor].isSelected()
                                        && vhl[ver - 1][hor - 1].isSelected()
                                        && vhl[ver - 1][hor + 1].isSelected()) {
                                    mark[ver - 1][hor].setVisible(true);
                                    mark[ver - 1][hor].setTextFill(scene.getFill());
                                    switch_color = false;
                                }
                                //Right-wing
                                if (vhl[ver + 2][hor].isSelected()
                                        && vhl[ver + 1][hor - 1].isSelected()
                                        && vhl[ver + 1][hor + 1].isSelected()) {
                                    mark[ver + 1][hor].setVisible(true);
                                    mark[ver + 1][hor].setTextFill(scene.getFill());
                                    switch_color = false;
                                }
                            }
                            if(switch_color) {
                                if (scene.getFill() == Color.RED) {
                                    scene.setFill(Color.BLUE);
                                    primaryStage.setTitle("Borders: Blue player's turn");
                                } else {
                                    scene.setFill(Color.RED);
                                    primaryStage.setTitle("Borders: Red player's turn");
                                }
                            }
                        }
                    });
                }
            }
        }

        scene.getStylesheets().add(getClass().getResource("borders.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
