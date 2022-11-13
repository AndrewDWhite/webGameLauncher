package LocalClient;

public class RequestURI {

	String start_uri;
	String end_regex;
	
	public RequestURI(String myRead_start_uri, String myRead_end_regex){
		start_uri = myRead_start_uri;
		end_regex = myRead_end_regex;
	}
	
	public String get_start_uri() {
		return start_uri;
	}
	public String get_end_regex() {
		return end_regex;
	}
}
