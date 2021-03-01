// --== CS400 File Header Information ==--
// Name: Annie Krillenberger
// Email: krillenberge@wisc.edu
// Team: red
// Group: FF
// TA: Daniel
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra >


import java.io.File;
import java.io.FileNotFoundException;
// import java.io.FileReader;
import java.io.IOException;
// import java.io.Reader;
// import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * This class instantiates the Data Wrangler’s implementation of the 
 * MovieDataReaderInterface & stores lists of movies in a hash table.
 */
public class BackendMovieMapper implements BackendInterface {

	private List<MovieInterface> movies = new ArrayList<MovieInterface>(); 			// list of all movies
	private List<MovieInterface> fitCriteria = new ArrayList<MovieInterface>(); 	// list of movies that fit the criteria
	
	private List<String> genres = new ArrayList<String>();				// list of all genres of movies
	private List<String> userInputedGenres = new ArrayList<String>();	// list of all user inputed genres
	private HashTableMap<String,ArrayList<MovieInterface>> genresMap;	// hash table of the genres
	
	private List<String> userInputedRatings = new ArrayList<String>();		// list of user inputed ratings
	private HashTableMap<String,ArrayList<MovieInterface>> ratingsMap;		// hash table of the ratings
	
	
	//////////		CONSTRUCTORS	//////////
	
	/**
	 * constructor that takes in command line arguments and reads movie data
	 * from a file that is passed as an argument to the program (Piazza @375)
	 * 
	 * instantiates the Data Wrangler’s implementation of the MovieDataReaderInterface
	 * 
	 * takes the command line arguments to the application as a parameter, parses them,
	 * and instantiates the Data Wrangler’s implementation of the MovieDataReaderInterface 
	 * interface to read in the data file passed as a command line argument to the application.
	 * It will then store the list of movies that the MovieDataReaderInterface provides.
	 */
	public BackendMovieMapper(String[] args) {
		if(args.length > 0) {
			
			Scanner fileReader = null;
			MovieScanner movieReader; // change to whatever corresponds to Data Wrangler's class -- MovieDataReader?
			File file;
			
			try {
				file =  new File(args[0]);
				fileReader = new Scanner(file);
				movieReader = new MovieScanner();	// change to whatever corresponds to Data Wrangler's class -- MovieDataReader?
					
				this.movies = movieReader.readDataSet(fileReader);	// method implemented by data wrangler
				
				this.genresMap = createGenreHashTableMap();		// creates genre HashTableMap
				this.ratingsMap = createRatingsHashTableMap();	// creates rating HashTableMap
				
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
			} catch (IOException e) {
				System.out.println("The file cannot be opened for reading or there is an error reading the file.");
			} catch (DataFormatException e) {
				e.printStackTrace();
				//System.out.println("The file does not have the correct format.");
			} finally {
				fileReader.close();
			}
		}
	}
	
	
	/**
	 * constructor that takes in a Reader instance in 
	 * order to make the testing easier (Piazza @375)
	 */
	public BackendMovieMapper(Scanner reader) {
		try {
			MovieScanner dummyDataWrangler = new MovieScanner();
			this.movies = dummyDataWrangler.readDataSet(reader);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException e) {
			System.out.println("The file cannot be opened for reading or there is an error reading the file.");
		} catch (DataFormatException e) {
			System.out.println("The file does not have the correct format.");
		}
	}
	
	
	//////////		INTERFACE METHODS		//////////
	
	
	/**
	 * used by the front end to add another genre to select movies by
	 */
	@Override
	public void addGenre(String genre) {
		userInputedGenres.add(genre);
	}

	
	/**
	 * used by the front end to add another genre or rating to select movies by
	 */
	@Override
	public void addAvgRating(String rating) {
		userInputedRatings.add(rating);
	}

	
	/**
	 * used by the front end to remove a genre to select movies by
	 */
	@Override
	public void removeGenre(String genre) {
		for(int i=userInputedGenres.size()-1; i>=0; i--) {
			if(userInputedGenres.get(i).equals(genre)) {
				userInputedGenres.remove(i);
				break;
			}
		}
		// no such genre found
		throw new NoSuchElementException("Genre has not been inputed by user.");
	}

	@Override
	public void removeAvgRating(String rating) {
		boolean isFound = false;
		for(int i=userInputedRatings.size() - 1; i>=0; i--) {
			if(userInputedRatings.get(i).equals(rating)) {
				userInputedRatings.remove(i);
				isFound = true;
				break;
			}
		}
		// no such rating found
		if (!isFound) {
			throw new NoSuchElementException("Rating has not been inputed by user.");

		}
		
	}

	
	/**
	 *  returns the list of currently selected genres
	 */
	@Override
	public List<String> getGenres() { 
		return this.userInputedGenres;
	}

	
	/**
	 *  returns the list of currently selected ratings
	 */
	@Override
	public List<String> getAvgRatings() {
		return this.userInputedRatings;
	}

	@Override
	public int getNumberOfMovies() {
		getFitCriteria();
		return fitCriteria.size();
	}

	
	/**
	 * returns a list of all genres in the dataset
	 */
	@Override
	public List<String> getAllGenres() {
		List<String> result = new ArrayList<String>();
		for(int i=0; i<movies.size(); i++) {
			for(int j=0; j<movies.get(i).getGenres().size(); j++) {
				if(!result.contains(movies.get(i).getGenres().get(j)))
					result.add(movies.get(i).getGenres().get(j));
			}
		}
		return result;
	}

	@Override
	public List<MovieInterface> getThreeMovies(int startingIndex) {
		
		List<MovieInterface> result = new ArrayList<MovieInterface>();
		if(getGenres().size() == 0) {
			//System.out.println("Genres list is empty");

			return result;
		}
		
		getFitCriteria();
		List<MovieInterface> temp = this.fitCriteria;

		// sort temp in ascending order based on avg. ratings
		for(int i=0; i<temp.size(); i++) {
			float starter = 0;
			for(int j=0; j<=i; j++) {
				if(temp.get(j).getAvgVote() >= starter) {
					Collections.swap(temp, i, j);
				}
			}
		}
		
		// find starting index for result List
		float highestAvgRating = fitCriteria.get(startingIndex).getAvgVote();
		double epsilon = 0.00000001;
		for(int k=fitCriteria.size()-1; k>=0; k--) {
			if(Math.abs(fitCriteria.get(k).getAvgVote() - highestAvgRating) < epsilon) { // way to compare floats for equality
				result.add(fitCriteria.get(k));
				break;
			}
		}
		
		// find starting index for temp arrayList
		int index = 0;
		for(int m=temp.size()-1; m>=0; m--) {
			if(Math.abs(temp.get(m).getAvgVote() - highestAvgRating) < epsilon) { // way to compare floats for equality
				index = m;
				break;
			}
		}
		
		// populate result List
		int l = index-1;
		while(l>=0 || result.size()==3) {
			result.add(temp.get(l));
			l--;
		}
		
		return result;
		
	}
	
	
	//////////		PRIVATE HELPER METHODS		//////////
	
	
	/**
	 * creates a HashTableMap for genres
	 */
	private HashTableMap createGenreHashTableMap() {
		HashTableMap result = new HashTableMap();
		
		// populate all genres in a list
		List<String> tempGenre = new ArrayList<String>();
		for(int i=0; i<movies.size(); i++) {				// iterate through all movies
			tempGenre = movies.get(i).getGenres();			// list all genres of given movie
			for(int j=0; j<tempGenre.size(); j++) {			// iterate through all genres of given movie
				if(!this.genres.contains(tempGenre.get(j)))	// if this.genre does not already contain the given genre
					genres.add(tempGenre.get(j));			// store all genres in this.genres
			}
		}
		
		// initializes masterList, adds the empty arrays, sets up the decoding array
		ArrayList<ArrayList<MovieInterface>> masterList = 
				new ArrayList<ArrayList<MovieInterface>>();		// master list of genres
		ArrayList<String> genreName = new ArrayList<String>(); 	// list to decode which index in masterList corresponds to which genre
		for (int k=0; k<genres.size(); k++) { 					// iterate through all genres
			masterList.add(new ArrayList<MovieInterface>());	// adds empty ArrayList for movies at each index of the masterList
			genreName.add(genres.get(k));						// adds the name of each genre to the corresponding List
		}
		
		// populates masterList
		for(int l=0; l<movies.size(); l++) {						// iterate through list of movies
			tempGenre = movies.get(l).getGenres();					// get a genres of given movie
			for(int m=0; m<tempGenre.size(); m++) {					// iterate through all genres of that movie
				for(int n=0; n<genreName.size(); n++) {				// iterate through names of genre in order to find where to sort movie
					if(genreName.get(n).equals(tempGenre.get(m)))	// if genre of masterList & movie match...
						masterList.get(n).add(movies.get(l));		// add movie to given genre
				}
			}
		}
		
		// create HashTableMap
		for(int o=0; o<masterList.size(); o++) {				// iterate through masterList
			result.put(genreName.get(o), masterList.get(o));	// add ArrayList of movies to HashTableMap
		}
		
		return result;
	}
	
	
	/**
	 * creates a HashTableMap for ratings
	 */
	public HashTableMap createRatingsHashTableMap() {
		
		HashTableMap result = new HashTableMap();
		
		// initializes masterList, adds the empty arrays, sets up the decoding array
		ArrayList<ArrayList<MovieInterface>> masterList = 
				new ArrayList<ArrayList<MovieInterface>>();		// master list of ratings
		for (int i=0; i<11; i++) { 								// iterate through all ratings
			masterList.add(new ArrayList<MovieInterface>());	// adds empty ArrayList for movies at each index of the masterList
		}
		
		int index;
		float rating;
		for(int j=0; j<movies.size(); j++) {				// iterate through all movies
			rating = movies.get(j).getAvgVote();			// get rating of given movie
			index = (int) rating;							// convert float rating to int
			masterList.get(index).add(movies.get(j));		// add movie to respective rating
		}
		
		// create HashTableMap
		for(int k=0; k<masterList.size(); k++) {	// iterate through masterList
			result.put(k, masterList.get(k));		// add ArrayList of movies to HashTableMap
		}
		
		return result;
		
	}
	
	
	private void getFitCriteria() {
		List<MovieInterface> genreTemp = new ArrayList<MovieInterface>();
		List<MovieInterface> genreFound = new ArrayList<MovieInterface>();
		List<MovieInterface> ratingTemp = new ArrayList<MovieInterface>();
		List<MovieInterface> ratingFound = new ArrayList<MovieInterface>();
		
		for(int i=0; i<userInputedGenres.size(); i++) {
			genreTemp = genresMap.get(userInputedGenres.get(i));
			for(int m=0; m<genreTemp.size(); m++) {
				if(!genreFound.contains(genreTemp.get(m)))
					genreFound.add(genreTemp.get(m));
			}
		}
		for(int j=0; j<userInputedRatings.size(); j++) {
			ratingTemp = ratingsMap.get(userInputedRatings.get(j));
			for(int n=0; n<ratingTemp.size(); n++) {
				if(!ratingFound.contains(ratingTemp.get(n)))
					ratingFound.add(ratingTemp.get(n));
			}
		}
		
		for(int k=0; k<genreFound.size(); k++) {
			for(int l=0; l<ratingFound.size(); l++) {
				if(genreFound.get(k).equals(ratingFound.get(l)))
					fitCriteria.add(genreFound.get(k));
			}
		}
	}

}
