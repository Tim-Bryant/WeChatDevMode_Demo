package com.codeday.tb.domain;

import java.util.List;

/**
 * 图文消息对象
 * 
 * @author liuxf
 * 
 */
public class NewsMessage extends BaseMessage {
	private int ArticleCount;
	private List<News> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<News> getArticles() {
		return Articles;
	}

	public void setArticles(List<News> articles) {
		Articles = articles;
	}

}
