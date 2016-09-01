package com.dpingin.homeautomation.spice.request.manager;

import android.support.annotation.NonNull;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Denis on 22.08.2016.
 */
public class RequestManager implements Runnable
{
	protected long interval = 100;

	protected Deque<Entry> requests = new ConcurrentLinkedDeque<>();

	private boolean running = false;
	private Thread requestThread = new Thread(this, "RequestThread");

	private SpiceManager spiceManager;

	public RequestManager(@NonNull SpiceManager spiceManager)
	{
		this.spiceManager = spiceManager;
	}

	public RequestManager start()
	{
		running = true;
		requestThread.start();
		return this;
	}

	public RequestManager stop()
	{
		running = false;
		requestThread.interrupt();
		try
		{
			requestThread.join();
			return this;
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException("Interrupting while waiting for thread to join", e);
		}
	}

	public void submit(@NonNull SpiceRequest request, RequestListener listener)
	{
		requests.clear();
		requests.addLast(new Entry(request, listener));
	}

	@Override
	public void run()
	{
		while (running)
		{
			if (requests.size() > 0)
			{
				Entry entry = requests.getLast();
				spiceManager.execute(entry.getRequest(), entry.getListener());
				requests.clear();
			}
			try
			{
				Thread.sleep(interval);
			}
			catch (InterruptedException e)
			{
				return;
			}
		}
	}

	private class Entry
	{
		private SpiceRequest request;
		private RequestListener listener;

		public Entry(SpiceRequest request, RequestListener listener)
		{
			this.request = request;
			this.listener = listener;
		}

		public SpiceRequest getRequest()
		{
			return request;
		}

		public RequestListener getListener()
		{
			return listener;
		}
	}
}
