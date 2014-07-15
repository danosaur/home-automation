package com.dpingin.home.automation.audio.api.capture.test;

import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullDataSource;

import java.nio.ByteBuffer;
import java.io.IOException;

import javax.media.MediaLocator;
import javax.media.Duration;
import javax.media.Time;


/**
 *
 * @author Chad McMillan
 */

public class ByteBufferDataSource extends PullDataSource {

    protected ContentDescriptor contentType;
    protected SeekableStream[] sources;
    protected boolean connected;
    protected ByteBuffer anInput;

    protected ByteBufferDataSource(){
    }

    /**
     * Construct a ByteBufferDataSource from a ByteBuffer.
     * @param source The ByteBuffer that is used to create the
     * the DataSource.
     */
    public ByteBufferDataSource(ByteBuffer input, String contentType) throws IOException {
        anInput = input;
        this.contentType = new ContentDescriptor(contentType);
        connected = false;
    }

    /**
     * Open a connection to the source described by
     * the ByteBuffer/CODE>.
     *

     *
     * The connect method initiates communication with the source.
     *
     * @exception IOException Thrown if there are IO problems
     * when connect is called.
     */
    public void connect() throws java.io.IOException {
        connected = true;
        sources = new SeekableStream [1];
        sources[0] = new SeekableStream(anInput);
    }

    /**
     * Close the connection to the source described by the locator.
     *


     * The disconnect method frees resources used to maintain a
     * connection to the source.
     * If no resources are in use, disconnect is ignored.
     * If stop hasn't already been called,
     * calling disconnect implies a stop.
     *
     */
    public void disconnect() {
        if(connected) {
            sources[0].close();
            connected = false;
        }
    }

    /**
     * Get a string that describes the content-type of the media
     * that the source is providing.
     *


     * It is an error to call getContentType if the source is
     * not connected.
     *
     * @return The name that describes the media content.
     */
    public String getContentType() {
        if( !connected) {
            throw new java.lang.Error("Source is unconnected.");
        }
        return contentType.getContentType();
    }

    public Object getControl(String str) {
        return null;
    }

    public Object[] getControls() {
        return new Object[0];
    }

    public javax.media.Time getDuration() {
        return Duration.DURATION_UNKNOWN;
    }

    /**
     * Get the collection of streams that this source
     * manages. The collection of streams is entirely
     * content dependent. The MIME type of this
     * DataSource provides the only indication of
     * what streams can be available on this connection.
     *
     * @return The collection of streams for this source.
     */
    public javax.media.protocol.PullSourceStream[] getStreams() {
        if( !connected) {
            throw new java.lang.Error("Source is unconnected.");
        }
        return sources;
    }

    /**
     * Initiate data-transfer. The start method must be
     * called before data is available.
     *(You must call connect before calling start.)
     *
     * @exception IOException Thrown if there are IO problems with the source
     * when start is called.
     */
    public void start() throws IOException {
    }

    /**
     * Stop the data-transfer.
     * If the source has not been connected and started,
     * stop does nothing.
     */
    public void stop() throws IOException {
    }
}