import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Frontend {

  private static Scanner scanner = new Scanner(System.in);
  private String[] filePath;
  private BackendMovieMapper backend;

  public static void main(String[] args) {
    Frontend frontend = new Frontend();

    // The main method takes a file path to a CSV file.
    frontend.filePath = args;
    frontend.run();
    System.out
        .println("Thank you for using the CS 400 Movie Mapper Project! May the force be with you!");
  }

  public void run() {
    String[] args = new String[] {"movies.csv"};
    backend = new BackendMovieMapper(args);
    // adding all ratings to the back end
    // When the system starts, all ratings will be selected.
    addAllRatings();

    // The base mode is the mode that Movie Mapper will be in when started.
    baseMode();
  }

  public void baseMode() {

    // welcome message
    System.out.println("==============================================================\n"
        + "--------  Welcome to the CS 400 Movie Mapper Project  --------\n"
        + "==============================================================\n\n"
        + "==============================================================\n"
        + "-------------------  Current Mode: Base  ---------------------\n"
        + "==============================================================\n"
        + "Press 'r' to enter ratings mode, 'g' to enter genres mode, and\n"
        + "to exit the program. Enter a number to scroll through the list\n"
        + "==============================================================\n");

    // list of the top 3 (by average rating) selected movies This list may be empty or contain less
    // than 3 movies if no 3 movies are in the selection set
    List<MovieInterface> threeMovies = new ArrayList<MovieInterface>();
    threeMovies = backend.getThreeMovies(0);
    for (int i = 0; i < threeMovies.size(); i++) {
      if (threeMovies.get(i) == null) {
        continue;
      }
      System.out.println(i + 1 + "  " + threeMovies.get(i).getTitle());
    }
    // Users will be able to scroll through the list by typing in numbers as commands of the rank
    // (by rating) of the movies to display.
    String input = "";
    boolean run = true;
    while (run) {

      System.out.println("\nPlease enter a valid command:");
      if (scanner.hasNextInt()) {
        input = scanner.nextLine();
        int index = Integer.valueOf(input);
        threeMovies = backend.getThreeMovies(index);
        for (int i = 0; i < threeMovies.size(); i++) {
          if (threeMovies.get(i) == null) {
            continue;
          }
          System.out.println(index++ + "  " + threeMovies.get(i).getTitle());
        }
      } else {
        input = scanner.nextLine();
        if (input.equals("x") || input.equals("g") || input.equals("r")) {
          break;
        }
      }
    }
    if (input.equals("g")) {
      genreMode();
    }

    if (input.equals("r")) {
      ratingsMode();
    }
  }

  public void genreMode() {
    System.out.println("==============================================================\n"
        + "---------------  Current Mode: Genre Select  -----------------\n"
        + "==============================================================\n"
        + "To select or de-select a genre, type the number corresponding\nto the genre and press "
        + "Enter. You can select multiple genres \nat once. To return to the base mode, type 'x'"
        + " into the command \nprompt.\n"
        + "==============================================================\n");

    // The genre selection mode will display a brief introduction for users of how to select or
    // deselect genres and how to interpret the list of selected and unselected genres. This is
    // followed by a list of all genres in which every genre is assigned a number. Users can type in
    // this number as a command to select and deselect each genre.


    while (true) {
      // displays all the genres and assigns a number
      List selectedGenres = new ArrayList();
      List allGenres = new ArrayList();
      selectedGenres = backend.getGenres();
      allGenres = backend.getAllGenres();
      int counter = 1;
      for (int i = 0; i < allGenres.size(); i++) {
        System.out.println(counter + "  " + allGenres.get(i));
        counter++;
      }

      // Genres are clearly marked as either selected or unselected in the list.
      System.out.println("\nYour selected genres:");
      for (int i = 0; i < selectedGenres.size(); i++) {
        System.out.println("   " + selectedGenres.get(i));
      }

      // The list of genres is followed by a brief description of how to return to the base mode
      // and a command prompt.
      System.out.println("==============================================================\n"
          + "To return to the base mode, type 'x' into the command prompt.\n"
          + "==============================================================\n");

      String input;

      // check if input is an integer
      System.out.println("Please enter a valid command:");
      if (scanner.hasNextInt()) {
        input = scanner.nextLine();
        // check if input is within the range of values
        int index = Integer.valueOf(input);
        if (index <= allGenres.size()) {
          // check if selected genre is already selected
          if (selectedGenres.contains(allGenres.get(index - 1))) {
            backend.removeGenre((String) allGenres.get(index - 1));
          } else {
            backend.addGenre((String) allGenres.get(index - 1));
          }
        }
      } else {
        // if not an integer check if it was the exit command
        input = scanner.nextLine();
        if (input.equals("x")) {
          break;
        }
      }
      // When multiple genres are selected, only movies that have all selected genres are
      // displayed.

      // When no genre is selected, the set of selected movies will be empty (initially).

      // Genres are clearly marked as either selected or unselected in the list.
    }
    baseMode();
  }

  public void ratingsMode() {
    System.out.println("==============================================================\n"
        + "--------------  Current Mode: Ratings Select  ----------------\n"
        + "==============================================================\n");
    while (true) {
      System.out.println("0    0-0.99\n" + "1    1-1.99\n" + "2    2-2.99\n" + "3    3-3.99\n"
          + "4    4-4.99\n" + "5    5-5.99\n" + "6    6-6.99\n" + "7    7-7.99\n" + "8    8-8.99\n"
          + "9    9-9.99\n" + "10   10");
      String input;

      // Ratings are clearly marked as either selected or unselected in the list.
      List selectedRatings = new ArrayList();
      selectedRatings = backend.getAvgRatings();
      System.out.println("\nYour selected ratings:");
      for (int i = 0; i < selectedRatings.size(); i++) {
        System.out.println("     " + selectedRatings.get(i));
      }

      System.out.println("Please enter a valid command:");
      // check if input is an integer
      if (scanner.hasNextInt()) {
        input = scanner.nextLine();
        // check if input is within the range of values
        int index = Integer.valueOf(input);
        if (!(index >= 0 && index <= 10)) {
          continue;
        }
        if (selectedRatings.contains(input)) {
          backend.removeAvgRating(input);
        } else {
          backend.addAvgRating(input);
        }
      } else {
        input = scanner.nextLine();
        if (input.equals("x")) {
          break;
        }
      }
    }
    baseMode();
  }

  public void addAllRatings() {
    for (int i = 0; i <= 10; i++) {
      backend.addAvgRating(String.valueOf(i));
    }
  }

  public void removeAllRatings() {
    for (int i = 0; i <= 10; i++) {
      backend.removeAvgRating(String.valueOf(i));
    }
  }

}
