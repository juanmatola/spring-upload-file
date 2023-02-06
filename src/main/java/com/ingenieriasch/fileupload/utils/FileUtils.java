package com.ingenieriasch.fileupload.utils;

import java.util.HashMap;

public abstract class FileUtils {

	public static final HashMap<String, String> EXTENSIONS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("image/png", ".png");
			put("image/jpeg", ".jpeg");
			put("image/jpg", ".jpg");
			put("application/pdf", ".pdf");
		}
	};
	
}
