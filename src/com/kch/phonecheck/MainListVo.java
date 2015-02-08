package com.kch.phonecheck;

public class MainListVo {

	private String content, title;
	private int image;
	
	public MainListVo() {
		// TODO Auto-generated constructor stub
	}

	public MainListVo(int image, String title, String content) {
		super();
		this.image = image;
		this.title = title;
		this.content = content;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
