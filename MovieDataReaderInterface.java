import java.util.Scanner;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

public interface MovieDataReaderInterface {
  public List<MovieInterface> readDataSet(Scanner scanner) 
                               throws IOException, DataFormatException;
}