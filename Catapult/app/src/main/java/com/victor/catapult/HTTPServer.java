package com.victor.catapult;

import fi.iki.elonen.NanoHTTPD;
import java.util.List;
import java.util.Map;

/**
 * @author Víctor Galán Grande
 */
public class HTTPServer extends NanoHTTPD {

	private static final int PORT = 8888;

	public interface OnCatapultActionsListener {
		void onFire();

		void onRecharge();
	}

	private OnCatapultActionsListener listener;

	public HTTPServer(OnCatapultActionsListener listener) {
		super(PORT);
		this.listener = listener;
	}

	@Override
	public Response serve(IHTTPSession session) {
		Map<String, List<String>> parameters = session.getParameters();
		if (parameters.get("fire") != null) {
			listener.onFire();
		}

		if (parameters.get("recharge") != null) {
			listener.onRecharge();
		}

		String html = "<html><head><script type=\"text/javascript\">"
			+ "  function fire() { window.location = '?fire=true'; }"
			+ "  function recharge() { window.location = '?recharge=true'; }"
			+ "</script></head>"
			+ "<body style=\"display:flex\">"
			+ "  <button style=\"flex: 50%; height: 100%; font-size: 4em;\" onclick=\"fire();\">FIRE!</button>"
			+ "  <button style=\"flex: 50%; height: 100%; font-size: 4em;\" onclick=\"recharge();\">Recharge</button>"
			+ "</body></html>";

		return newFixedLengthResponse(html);
	}
}

