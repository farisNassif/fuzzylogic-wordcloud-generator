package ie.gmit.sw.ai.V4;

public final class UrlScore {
	private String url;
	private int score;

	public UrlScore(String url, int score) {
		super();
		this.url = url;
		this.score = score;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
