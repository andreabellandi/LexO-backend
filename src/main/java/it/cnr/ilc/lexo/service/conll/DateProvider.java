package it.cnr.ilc.lexo.service.conll;

import java.util.Date;

public class DateProvider {
	private static DateProvider instance;
	private Date staticDate;

	private DateProvider(Date staticDate) {
		this.staticDate = staticDate;
	}

	public static DateProvider getInstance(Date staticDate) {
		if (instance == null)
			instance = new DateProvider(staticDate);
		return instance;
	}

	public static DateProvider getInstance() {
		return getInstance(null);
	}

	public Date getDate() {
		return staticDate != null? staticDate : new Date();
	}
}
