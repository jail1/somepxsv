package ro.activemall.photoxserver.json;

/**
 * 
 * @author Badu
 *
 *         Object for storing user preferences //TODO : describe more
 */
public class UserPreferencesDataJSON {
	public String preferedLanguage;
	public boolean hasLearnedHelp1 = false;
	public boolean hasLearnedHelp2 = false;
	public boolean hasLearnedHelp3 = false;

	public UserPreferencesDataJSON() {

	}

	public UserPreferencesDataJSON(String lang) {
		this.preferedLanguage = lang;
	}
}