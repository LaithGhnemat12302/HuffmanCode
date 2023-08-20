package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		Pane pane = new Pane();
		pane.setStyle("-fx-background-color: #9acd32;\r\n" + "        -fx-background-radius:100;\r\n");
		
		Label label=new Label("Compress/Decompress File");
		label.setFont(new Font(20));
		pane.getChildren().add(label);
		label.setTranslateX(160);
		label.setTranslateY(10);

		Button compress = new Button("Compress");
		compress.setPrefSize(180, 50);
		compress.setStyle("-fx-background-color: #00ff00;\r\n" + "        -fx-background-radius:100;\r\n");
		pane.getChildren().add(compress);
		compress.setTranslateX(70);
		compress.setTranslateY(50);
		compress.setFont(new Font(15));

		Button decompress = new Button("Decompress");
		decompress.setPrefSize(180, 50);
		decompress.setStyle("-fx-background-color: #00ff00;\r\n" + "        -fx-background-radius:100;\r\n");
		pane.getChildren().add(decompress);
		decompress.setTranslateX(330);
		decompress.setTranslateY(50);
		decompress.setFont(new Font(15));

		TextArea ta = new TextArea();
		ta.setPrefSize(440, 300);
		pane.getChildren().add(ta);
		ta.setTranslateX(70);
		ta.setTranslateY(120);
		ta.setFont(new Font(15));

		compress.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(primaryStage);
			try {
				Huffman.compress(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ImageView i = new ImageView("https://img.icons8.com/glyph-neue/512/checkmark.png");
			i.setFitHeight(20);
			i.setFitWidth(20);
			pane.getChildren().add(i);
			i.setTranslateX(222);
			i.setTranslateY(440);

			Button statistics = new Button("Show statistics");
			statistics.setPrefSize(180, 50);
			statistics.setStyle("-fx-background-color: #ff0110;\r\n" + "        -fx-background-radius:100;\r\n");
			pane.getChildren().add(statistics);
			statistics.setTranslateX(250);
			statistics.setTranslateY(430);
			statistics.setFont(new Font(15));

			statistics.setOnAction(s -> {
				ta.appendText("File path: " + file.getPath() + "\nCompressed file path: " + Huffman.outFileName
						+ "\n\nASCII\tCharacter\t\tFrequency\tHuffCode\n");
				for (int k = 0; k < Huffman.huffCodeArray.length; k++) {
					if((int)Huffman.huffCodeArray[k].character==10 || (int)Huffman.huffCodeArray[k].character==9)
						continue;
					ta.appendText(String.valueOf((int) Huffman.huffCodeArray[k].character) + "\t\t  "
							+ Huffman.huffCodeArray[k].character + "\t\t\t"
							+ String.valueOf(Huffman.huffCodeArray[k].counter) + "\t\t\t"
							+ Huffman.huffCodeArray[k].huffCode+"\n");
				}
			});

		});
		
		decompress.setOnAction(e->{
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(primaryStage);
			Huffman.deCompress(file);
			

			ImageView i = new ImageView("https://img.icons8.com/glyph-neue/512/checkmark.png");
			i.setFitHeight(20);
			i.setFitWidth(20);
			pane.getChildren().add(i);
			i.setTranslateX(235);
			i.setTranslateY(440);
		});

		Scene scene = new Scene(pane, 600, 500);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Huffman Project");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
