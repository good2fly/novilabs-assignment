package nlhomework.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import nlhomework.CsvFileProcessorApp;
import nlhomework.io.S3CsvReaderAndWriterFactory;
import nlhomework.processor.CsvProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

public class S3RequestHandler implements RequestHandler<S3Event, String> {

    private final static Logger LOG = LoggerFactory.getLogger(S3RequestHandler.class);

    private final static String OUTPUT_FOLDER = "output/";

    private final static S3Client S3_CLIENT = S3Client.builder().region(Region.US_WEST_1).build();
    private final static CsvProcessor CSV_PROC = new CsvProcessor(CsvFileProcessorApp.buildDefaultColumnsConfig());

    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        LOG.debug("handleRequest: invoked with {} records", s3Event.getRecords().size());
        for (var record : s3Event.getRecords()) {
            var s3 = record.getS3();
            var bucketName = s3.getBucket().getName();
            var key = s3.getObject().getKey();
            var slashIdx = key.lastIndexOf("/");
            var outputKey = OUTPUT_FOLDER + (slashIdx == -1 ? key : key.substring(slashIdx + 1));
            LOG.debug("handleRequest: bucket name={}, input key={}, output key={}", bucketName, key, outputKey);

            var ioFactory = new S3CsvReaderAndWriterFactory(S3_CLIENT, bucketName, key, bucketName, outputKey);
            try {
                CSV_PROC.process(ioFactory, ioFactory);
            } catch (Exception e) {
                // TODO this can be made more sophisticated with retry, re-throw, DLQ, etc.
                LOG.error("Failed to process key=%s, in bucket=%s".formatted(key, bucketName), e);
            }
        }
        return "SUCCESS";
    }
}
