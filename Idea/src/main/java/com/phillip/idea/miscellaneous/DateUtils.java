package com.phillip.idea.miscellaneous;

import java.util.Date;

public abstract class DateUtils {

	public static String convertDate(Long commentTimestamp){
		Long timeDiff = (new Date().getTime() - commentTimestamp) / 1000;
		
		if(timeDiff <= 3600){
			return timeDiff / 60 + " minutes ago";
		}else if(timeDiff <= 86400){
			return timeDiff / 3600 + " hours ago";
		}else if(timeDiff <= 604800){
			return timeDiff / 86400 + " days ago";
		}else if(timeDiff <= 2629800){
			return timeDiff / 7257600 + " weeks ago";
		}else if(timeDiff <= 31557600){
			return timeDiff / 2629800 + " months ago";
		}else{
			return timeDiff / 31557600 + " years ago";
		}
	}
}
