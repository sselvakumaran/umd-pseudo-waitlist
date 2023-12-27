package webscraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class webscraper {
	private final static String LINKS_FNAME = "links.txt",
			OPENSEATS_HTML = "open-seats-count",
			COURSEID_HTML = "course",
			SECTIONDIV_HTML = "section-info-container",
			SECTIONID_HTML = "section-id",
			EXPECTED_TITLE = "Schedule of Classes",
			RESULT_FORMAT = "\n%s - %s has %d seats open!";
	private final static int TIMEOUT= 10000, //ms it should take to load URL
			DELAY= 60; //sec between attempts

	private final static ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(1);

	private final static DateTimeFormatter timeFormatter= DateTimeFormatter.ISO_LOCAL_TIME;
	private static ScheduledFuture<?> schedule= null;

	private static ArrayList<String> extractLinksFromFile(String filePath) {
		ArrayList<String> linksList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				linksList.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return linksList;
	}
	
	private static String getSeats(String url) {
		Document doc;
		Elements occurrences;
		String result = "";
		try {
			doc = Jsoup.connect(url).timeout(TIMEOUT).get();
			if (!doc.title().equals(EXPECTED_TITLE)) {
				System.out.println("Website was not named \"Schedule of Classes\".");
				return "";
			}
		} catch (SocketTimeoutException e) {
			System.out.println("Loading document timed out.");
			return "";
		} catch (Exception e) {
			System.out.println("Could not connect to URL.");
			return "";
		}
		occurrences = doc.getElementsByClass(OPENSEATS_HTML);
		String courseid, sectionid = null, seatsOpenString;
		int seatsOpen = 0;
		for (Element occurrence : occurrences) {
			seatsOpenString = occurrence.text();
			try {
				seatsOpen = Integer.parseInt(seatsOpenString);
			} catch (NumberFormatException e) {
				// Handle parsing error if needed
				System.out.println("Could not parse integer.");
				return "";
			}
			if (seatsOpen > 0) {
				Element parent = occurrence;
				while (parent != null && !parent.hasClass(SECTIONDIV_HTML)) {
		            parent = parent.parent();
		        }
				if (parent != null)
					sectionid = parent.selectFirst("." + SECTIONID_HTML).text().strip();
				else {
					System.out.println("Error with finding section.");
					return ""; // No open seats found
				}
				while (parent != null && !parent.hasClass(COURSEID_HTML)) {
		            parent = parent.parent();
		        }
				if (parent != null)
					courseid = parent.id();
				else {
					System.out.println("Error with finding course.");
					return ""; // No open seats found
				}
				result += String.format(RESULT_FORMAT, courseid, sectionid, seatsOpen);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String fname = LINKS_FNAME;
		if (args.length > 0)
            fname = args[0];
		System.out.println("Using file \'" + fname + "\'...");
		ArrayList<String> files = extractLinksFromFile(fname);
		if (files.size() == 0) {
			System.out.println("No sections found.");
			return;
		}	
		final Runnable event = new Runnable() {
			@Override
			public void run() {
				String result = "";
				for (String url : files)
					result += getSeats(url);
				if (result.isEmpty())
					System.out.println(timeFormatter.format(LocalDateTime.now()) + ": no seats found.");
				else {
					JOptionPane.showMessageDialog(null, result);
					schedule.cancel(true);
				}
			}
		};
		schedule = scheduler.scheduleAtFixedRate(event, 0, DELAY, TimeUnit.SECONDS);
	}

}
