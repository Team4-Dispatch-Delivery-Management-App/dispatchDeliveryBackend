package dispatchApp.utils;

	
import org.joda.time.DateTime;

import lombok.Data;

public @Data class Element {
	
	private int carrierId;	
	private int orderId;
	private DateTime startTime;
	private DateTime departureTime;
	private DateTime deliveryTime;
	private DateTime endTime;
	
	private Element (ElementBuilder builder) {
		this.carrierId = builder.carrierId;
	}
	public static class ElementBuilder{

		private int carrierId;	
		private int orderId;
		private DateTime startTime;
		private DateTime departureTime;
		private DateTime deliveryTime;
		private DateTime endTime;
		
		public ElementBuilder carrierId(int carrierId) {
			this.carrierId = carrierId;
			return this;
		}

		public ElementBuilder orderId(int orderId) {
			this.orderId = orderId;
			return this;
		}

		public ElementBuilder startTime(DateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public ElementBuilder departureTime(DateTime departureTime) {
			this.departureTime = departureTime;
			return this;
		}

		public ElementBuilder deliveryTime(DateTime deliveryTime) {
			this.deliveryTime = deliveryTime;
			return this;
		}

		public ElementBuilder endTime(DateTime endTime) {
			this.endTime = endTime;
			return this;
		}

		public Element build() {
			return new Element(this);
		}
	}
	
	
}