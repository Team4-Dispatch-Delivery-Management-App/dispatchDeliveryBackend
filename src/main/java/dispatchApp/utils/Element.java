package dispatchApp.utils;

	
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Element {
	
	private int carrierId;	
	private int orderId;
	private DateTime startTime;
	private DateTime departureTime;
	private DateTime deliveryTime;
	private DateTime endTime;
	
//	private Element (ElementBuilder builder) {
//		this.carrierId = builder.carrierId;
//	}
//	public static class ElementBuilder{
//
//		private int carrierId;	
//		private int orderId;
//		private DateTime startTime;
//		private DateTime departureTime;
//		private DateTime deliveryTime;
//		private DateTime endTime;
//		
//		public ElementBuilder carrierId(int carrierId) {
//			this.carrierId = carrierId;
//			return this;
//		}
//
//		public ElementBuilder orderId(int orderId) {
//			this.orderId = orderId;
//			return this;
//		}
//
//		public ElementBuilder startTime(DateTime startTime) {
//			this.startTime = startTime;
//			return this;
//		}
//
//		public ElementBuilder departureTime(DateTime departureTime) {
//			this.departureTime = departureTime;
//			return this;
//		}
//
//		public ElementBuilder deliveryTime(DateTime deliveryTime) {
//			this.deliveryTime = deliveryTime;
//			return this;
//		}
//
//		public ElementBuilder endTime(DateTime endTime) {
//			this.endTime = endTime;
//			return this;
//		}
//
//		public Element build() {
//			return new Element(this);
//		}
//	}
	public static void main (String[] args) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		DateTime endTime = formatter.parseDateTime("2020-01-01 09:12");
		
		Element carrierElement = new Element.ElementBuilder().orderId(1).carrierId(2)
				.endTime(endTime).build();
		System.out.println(carrierElement.toString());
		
	}
	
	
}