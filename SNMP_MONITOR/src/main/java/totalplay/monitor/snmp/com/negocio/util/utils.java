package totalplay.monitor.snmp.com.negocio.util;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class utils {
	public class CustomProjectAggregationOperation implements AggregationOperation {
	    private String jsonOperation;

	    public CustomProjectAggregationOperation(String jsonOperation) {
	        this.jsonOperation = jsonOperation;
	    }

	    @Override
	    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
	        return aggregationOperationContext.getMappedObject(Document.parse(jsonOperation));
	    }

		
	}

	public static LocalDateTime getLocaDateTime() {
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss.SSS" );
		return LocalDateTime.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(6, ChronoUnit.HOURS));
	}


}
