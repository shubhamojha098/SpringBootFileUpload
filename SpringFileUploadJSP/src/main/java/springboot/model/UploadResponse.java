package springboot.model;

public class UploadResponse {
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public UploadResponse(String message) {
			
			this.message = message;
		}
		
}
