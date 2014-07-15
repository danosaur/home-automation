package com.dpingin.home.automation.audio.api.capture.test;

import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.Seekable;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * @author Chad McMillan
 */
public class SeekableStream implements PullSourceStream, Seekable
{


    protected ByteBuffer inputBuffer;

/**
 * a flag to indicate EOF reached
 */


    /**
     * Creates a new instance of SeekableStream
     */
    public SeekableStream(ByteBuffer byteBuffer)
    {
        inputBuffer = byteBuffer;
        this.seek((long) (0)); // set the ByteBuffer to to beginning
    }

    /**
     * Find out if the end of the stream has been reached.
     *
     * @return Returns true if there is no more data.
     */
    public boolean endOfStream()
    {
        return (!inputBuffer.hasRemaining());
    }

    /**
     * Get the current content type for this stream.
     *
     * @return The current ContentDescriptor for this stream.
     */
    public ContentDescriptor getContentDescriptor()
    {
        return null;
    }

    /**
     * Get the size, in bytes, of the content on this stream.
     *
     * @return The content length in bytes.
     */
    public long getContentLength()
    {
        return inputBuffer.capacity();
    }

    /**
     * Obtain the object that implements the specified
     * Class or Interface
     * The full class or interface name must be used.
     * <p/>
     * <p/>
     * <p/>
     * <p/>
     * The control is not supported.
     * null is returned.
     *
     * @return null.
     */
    public Object getControl(String controlType)
    {
        return null;
    }

    /**
     * Obtain the collection of objects that
     * control the object that implements this interface.
     * <p/>
     * <p/>
     * <p/>
     * <p/>
     * No controls are supported.
     * A zero length array is returned.
     *
     * @return A zero length array
     */
    public Object[] getControls()
    {
        Object[] objects = new Object[0];

        return objects;
    }

    /**
     * Find out if this media object can position anywhere in the
     * stream. If the stream is not random access, it can only be repositioned
     * to the beginning.
     *
     * @return Returns true if the stream is random access, false if the stream can only
     *         be reset to the beginning.
     */
    public boolean isRandomAccess()
    {
        return true;
    }

    /**
     * Block and read data from the stream.
     * <p/>
     * <p/>
     * <p/>
     * Reads up to length bytes from the input stream into
     * an array of bytes.
     * If the first argument is null, up to
     * length bytes are read and discarded.
     * Returns -1 when the end
     * of the media is reached.
     * <p/>
     * This method only returns 0 if it was called with
     * a length of 0.
     *
     * @param buffer The buffer to read bytes into.
     * @param offset The offset into the buffer at which to begin writing data.
     * @param length The number of bytes to read.
     * @return The number of bytes read, -1 indicating
     *         the end of stream, or 0 indicating read
     *         was called with length 0.
     * @throws IOException Thrown if an error occurs while reading.
     */
    public int read(byte[] buffer, int offset, int length) throws IOException
    {

// return n (number of bytes read), -1 (eof), 0 (asked for zero bytes)

        if (length == 0)
            return 0;
        try
        {
            inputBuffer.get(buffer, offset, length);
            return length;
        } catch (BufferUnderflowException E)
        {
            return -1;
        }
    }

    public void close()
    {

    }

    /**
     * Seek to the specified point in the stream.
     *
     * @param where The position to seek to.
     * @return The new stream position.
     */
    public long seek(long where)
    {
        try
        {
            inputBuffer.position((int) (where));
            return where;
        } catch (IllegalArgumentException E)
        {
            return this.tell(); // staying at the current position
        }
    }

    /**
     * Obtain the current point in the stream.
     */
    public long tell()
    {
        return inputBuffer.position();
    }

    /**
     * Find out if data is available now.
     * Returns true if a call to read would block
     * for data.
     *
     * @return Returns true if read would block; otherwise
     *         returns false.
     */
    public boolean willReadBlock()
    {
        return (inputBuffer.remaining() == 0);
    }
}