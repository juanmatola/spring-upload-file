package com.ingenieriasch.fileupload.web.payload;

import java.text.DecimalFormat;

public class UploadResponse {

	private String url;
	private String contentType;
	private String size;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSize(Long numOfBytes) {

		Double auxSize;
		String unit;

		if (numOfBytes < 1024) {

			auxSize = numOfBytes.doubleValue();
			unit = "Bytes";

		} else if (numOfBytes < 1_048_576) {

			auxSize = (double) (numOfBytes / 1024D);
			unit = "KB";

		} else {

			auxSize = (double) (numOfBytes / 1_048_576D);
			unit = "MB";

		}

		this.size = new DecimalFormat("0.00 " + unit).format(auxSize);

	}

	public String getSize() {
		return size;
	}

}
