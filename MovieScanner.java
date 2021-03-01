// --== CS400 File Header Information ==--
// Name: Jake Armbruster
// Email: earmbruster@wisc.edu
// Team: Red
// Role: Data Wrangler
// TA: Daniel
// Lecturer: Gary Dahl
// Notes to Grader: N/A
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class MovieScanner implements MovieDataReaderInterface {
 
  @Override
  public List<MovieInterface> readDataSet(Scanner scanner)
      throws IOException, DataFormatException {
    List<String[]> movies = new ArrayList<>();
    
    scanner.nextLine();
    while (scanner.hasNextLine()) {
        String[] line = scanner.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        for (int i = 0; i < line.length; i++) {
        	if (line[i] == null || line[i].isEmpty()) {
        		line[i] = "null";
        	}
        }
        
        movies.add(line);

    }
    
    for (int i = 0; i < movies.get(5).length; i++) {
    	System.out.println(movies.get(5)[i]);
    }
    System.out.println(movies.size());
   // HashTableMap allMovies = new HashTableMap();
    List<MovieInterface> totalMovies = new ArrayList<MovieInterface>();
    if (movies.get(0).length != 13) {
    	
      throw new DataFormatException();
    }
    for (int i = 0; i < movies.size(); i++) {
      String title = movies.get(i)[0];
      //System.out.println(movies.get(i)[2]);
      System.out.println(i);
      Integer year = Integer.parseInt(movies.get(i)[2]);
      String[] genresList = movies.get(i)[3].split(",");
      List<String> genres = Arrays.asList(genresList);
      String director = movies.get(i)[7];
      String description = movies.get(i)[11];
      Float avgVote = Float.parseFloat(movies.get(i)[12]);
      MovieInterface newMovie = new Movie(title, year, genres, director, description, avgVote);
      totalMovies.add(newMovie);
    }
    Collections.sort(totalMovies);

    return totalMovies;
  }
}
