package nlhomework.io;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class S3CsvReaderAndWriterFactory implements CsvReaderFactory, CsvWriterFactory {

    private final S3Client s3Client;
    private final String inputBucketName;
    private final String inputKey;
    private final String outputBucketName;
    private final String outputKey;

    public S3CsvReaderAndWriterFactory(S3Client s3Client, String inputBucketName, String inputKey, String outputBucketName, String outputKey) {
        Objects.requireNonNull(s3Client, "s3Client must not be null");
        Objects.requireNonNull(inputBucketName, "inputBucketName must not be null");
        Objects.requireNonNull(inputKey, "inputKey must not be null");
        Objects.requireNonNull(inputBucketName, "outputBucketName must not be null");
        Objects.requireNonNull(inputKey, "outputKey must not be null");

        this.s3Client = s3Client;
        this.inputBucketName = inputBucketName;
        this.inputKey = inputKey;
        this.outputBucketName = outputBucketName;
        this.outputKey = outputKey;
    }

    @Override
    public Reader buildReader() throws IOException {
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(inputBucketName)
                .key(inputKey)
                .build();
        return new BufferedReader(new InputStreamReader(s3Client.getObject(getObjectRequest), StandardCharsets.UTF_8));
    }


    @Override
    public Writer buildWriter() throws IOException {
        var writer = new StringWriter(1024 * 1024) {  // 1MB
            @Override
            public void close() throws IOException {
                super.close();

                var putRequest = PutObjectRequest.builder()
                        .bucket(outputBucketName)
                        .key(outputKey)
                        .contentType("text/csv")
                        .build();
                // This loads the whole result into memory, for larger documents finish piped stream implementation below.
                var payload = getBuffer().toString().getBytes(StandardCharsets.UTF_8);
                s3Client.putObject(putRequest, RequestBody.fromInputStream(new ByteArrayInputStream(payload), payload.length));
            }
        };
        return writer;
    }

    public Writer buildPipedWriter() throws IOException {
        var outputStream = new PipedOutputStream();
        var inputStream = new PipedInputStream(outputStream, 1024 * 1024);  // 1MB
        var putRequest = PutObjectRequest.builder()
                .bucket(outputBucketName)
                .key(outputKey)
                .contentType("text/csv")
                .build();
        // TODO The putObject should run in a different thread from the CSV processing thread, otherwise the pipes will block each other.
        s3Client.putObject(putRequest, RequestBody.fromContentProvider(ContentStreamProvider.fromInputStream(inputStream), "text/csv"));
        return new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
    }
}
