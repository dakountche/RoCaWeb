/*******************************************************************************
 * This file is part of RoCaWeb.
 * 
 *  RoCaWeb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package com.rocaweb.commons.recording;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rocaweb.commons.fs.FSManager;
import com.rocaweb.commons.utils.Utils;

/**
 * Manages the connections to the Elasticsearch server
 * 
 * @author Yacine TAMOUDI
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 */
public class ElasticSearchConnector {

	private Logger logger = LogManager.getLogger(ElasticSearchConnector.class);

	/**
	 * The hostname of the ElasticSearch server
	 */
	private String host = "localhost";

	/**
	 * The listening port
	 */
	private int port = 9300;

	private static final int TOTALHITS = 0;

	private static final String MODSEC_ES_PREFIX = "rawSection";

	/**
	 * ModSecurity 2.x audit log entries always begin with the header part.
	 * 
	 * @see https://github.com/SpiderLabs/ModSecurity/wiki/ModSecurity-2-Data-Formats#audit-log-header-a
	 */
	public static final String MODSEC_AUDIT_LOG_HEADER = "A";

	/**
	 * The request headers part contains the request line and the request headers.
	 * 
	 * @see https://github.com/SpiderLabs/ModSecurity/wiki/ModSecurity-2-Data-Formats#request-headers-b
	 */
	public static final String MODSEC_REQUEST_HEADERS = "B";

	/**
	 * This part contains the request body of the transaction, after dechunking and
	 * decompression (if applicable).
	 */
	public static final String MODSEC_REQUEST_BODY = "C";

	/**
	 * This part contains the status line and the request headers that would have
	 * been delivered to the client had ModSecurity not intervened.
	 * 
	 * @see https://github.com/SpiderLabs/ModSecurity/wiki/ModSecurity-2-Data-Formats#intended-response-headers-d
	 */
	public static final String MODSEC_INTENDED_RESPONSE_HEADERS = "D";

	/**
	 * Contains the transaction response body (before compression and chunking,
	 * where used) that was either sent or would have been sent had ModSecurity not
	 * intervened. For more details:
	 * 
	 * @see https://github.com/SpiderLabs/ModSecurity/wiki/ModSecurity-2-Data-Formats#intended-response-body-e
	 */
	public static final String MODSEC_INTENDED_RESPONSE_BODY = "E";

	/**
	 * This part contains the actual response headers sent to the client. Since
	 * ModSecurity 2.x for Apache does not access the raw connection data, it
	 * constructs part F out of the internal Apache data structures that hold the
	 * response headers.
	 * 
	 * @see https://github.com/SpiderLabs/ModSecurity/wiki/ModSecurity-2-Data-Formats#response-headers-f
	 */
	public static final String MODSEC_RESPONSE_HEADERS = "F";

	/**
	 * When implemented, this part will contain the actual response body before
	 * compression and chunking.
	 */
	public static final String MODSEC_RESPONSE_BODY = "G";

	/**
	 * Elasticsearch definitions: A cluster is a collection of one or more nodes
	 * (servers) that together holds your entire data and provides federated
	 * indexing and search capabilities across all nodes. A cluster is identified by
	 * a unique name which by default is "elasticsearch". This name is important
	 * because a node can only be part of a cluster if the node is set up to join
	 * the cluster by its name.
	 */
	private String cluster = "rocaweb-elk";

	/**
	 * A type is a logical category/partition of your index whose semantics is
	 * completely up to you. In general, a type is defined for documents that have a
	 * set of common fields. For example, let’s assume you run a blogging platform
	 * and store all your data in a single index. In this index, you may define a
	 * type for user data, another type for blog data, and yet another type for
	 * comments data.
	 */
	private String type = "mod_security_alert";

	/**
	 * The Elastic transport client
	 */
	private TransportClient esclient = null;

	/**
	 * The Elastic settings
	 */
	private Settings settings = null;

	/**
	 * The separator to separate the different parameters in a HTTP request. The
	 * default values is "?".
	 */
	private String parameterSeparator = "?";

	private int size = 0;

	private SearchResponse response = null;

	private SearchHit[] shs = null;

	/**
	 * Constructor
	 * 
	 * @param host
	 *            the hostname of the Elasticsearch server
	 * @param port
	 *            the listening port of the server. The default port is 9200
	 * @param cluster
	 *            the cluster used to index the logs
	 * @param type
	 *            the type of documents used by RoCaWeb
	 */
	public ElasticSearchConnector(String host, int port, String cluster, String type) {
		setHost(host);
		setPort(port);
		setCluster(cluster);
		setType(type);
		setEsclient(createClient());
	}

	private void setHost(String host) {
		this.host = host;
	}

	private void setEsclient(TransportClient esclient) {
		this.esclient = esclient;
	}

	@SuppressWarnings("resource")
	private TransportClient createClient() {

		TransportClient esclient = null;
		try {

			esclient = new PreBuiltTransportClient(getSettings())
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(getHost()), getPort()));

		} catch (UnknownHostException e) {
			logger.debug("", e);
		}

		return esclient;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPort(int port) {

		this.port = port;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	/**
	 * Stops the client
	 */
	public void close() {
		getEsclient().close();
	}

	/**
	 * Get the indexes for a given pattern
	 * 
	 * @param pattern
	 * @return the indexes that matches the pattern
	 */
	public List<String> getIndexFromPattern(String pattern) {

		String[] metaresponse = getAllIndices();
		List<String> index = Lists.newArrayList();
		for (String mresponse : metaresponse) {
			if (mresponse.matches(pattern)) {
				index.add(mresponse);
			}
		}
		return index;
	}

	public TransportClient getEsclient() {

		if (esclient == null) {
			esclient = createClient();
		}
		return esclient;
	}

	/**
	 * Allows access to all available indices
	 * 
	 * @return All the concrete indices
	 */
	public String[] getAllIndices() {
		return getEsclient().admin().cluster().prepareState().execute().actionGet().getState().getMetaData()
				.getConcreteAllIndices();

	}

	private SearchResponse getSearchResponse(String index, int size) {
		return getEsclient().prepareSearch(index).setTypes(getType()).setSize(size).execute().actionGet();
	}

	private long getTotalHits(String index) {
		return getSearchResponse(index, TOTALHITS).getHits().getTotalHits();
	}

	/**
	 * Initializes the properties related to search methods
	 * 
	 * @param index:
	 *            the index from which the initialization should be made
	 */
	private void initSearchVariables(String index) {
		size = (int) getTotalHits(index);

		response = getSearchResponse(index, size);

		shs = response.getHits().getHits();
	}

	/**
	 * Parses the Elasticsearch index and writes to the root filesystem
	 * 
	 * @param index:
	 *            the index to querry
	 * @param root:
	 *            the filesystem to write
	 */
	public void parseByUrlBySiteFromES(String index, final FileObject root) {

		initSearchVariables(index);

		logger.debug("Number of event:" + shs.length);

		for (SearchHit sh : shs) {
			try {

				String httpMethod = getSourceByName(sh, "httpMethod");
				String requestedUri = getSourceByName(sh, "requestedUri");
				String host = getSourceByName(sh, "host");
				logger.debug("The HTTP method is : " + httpMethod);

				if (httpMethod.equals("GET")) {
					parseGET(sh, requestedUri, host, root);
				} else if (httpMethod.equals("POST")) {
					parsePost(sh, requestedUri, host, root);
				} else {
					logger.error("The HTTP method : " + httpMethod + " is not yet supported.");
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Event not added!");
			}
		}

	}

	private void parseGET(SearchHit sh, String requestedUri, String host, FileObject root) {

		if (requestedUri.contains(getParameterSeparator())) {
			parse(sh, requestedUri, host, root, "GET");
		}

	}

	public void parse(SearchHit sh, String requestedUri, String host, FileObject root, String method) {

		Map<String, List<String>> listget = Utils.parseURI(requestedUri);
		String murl;
		for (Entry<String, List<String>> entry : listget.entrySet()) {

			murl = Utils.moduloUrl(requestedUri);
			String content = Utils.listToString(entry.getValue());
			FSManager.putUrlMethodValue(host + "/" + murl, entry.getKey(), content, root, method);

		}

	}

	private void parsePost(SearchHit sh, String requestedUri, String host, FileObject root) {
		parse(sh, requestedUri, host, root, "POST");
	}

	@SuppressWarnings("unchecked")
	private String getSourceByName(SearchHit sh, String name) {
		Set<String> treatedResult = Sets.newHashSet();

		Object result = sh.getSource().get(name);
		String finalResult = "NOTFOUND";

		if (result instanceof ArrayList) {
			treatedResult.addAll((List<String>) result);
			finalResult = treatedResult.iterator().next();
		} else if (result instanceof String)
			finalResult = (String) result;

		return finalResult;

	}

	/**
	 * @param packetlist
	 * @return
	 */
	public void parseResponsesByUrlBySiteFromES(String index, final FileObject root) {
		initSearchVariables(index);
		for (SearchHit sh : shs) {
			writeToVFS(sh, root, MODSEC_ES_PREFIX + MODSEC_INTENDED_RESPONSE_BODY);
		}
	}

	/**
	 * @param packetlist
	 * @return
	 */
	public void parseResponsesByUrlBySiteFromESScroll(String index, final FileObject root) {

		SearchResponse response = getEsclient().prepareSearch(index).setTypes(type).setSize(0).execute().actionGet();

		response = getEsclient().prepareSearch(index).setScroll(new TimeValue(60000)).setTypes(type).setSize(100)
				.execute().actionGet();

		while (true) {

			SearchHit[] shs = response.getHits().getHits();

			for (SearchHit sh : shs) {

				writeToVFS(sh, root, MODSEC_ES_PREFIX + MODSEC_INTENDED_RESPONSE_BODY);

			}

			response = getEsclient().prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000))
					.execute().actionGet();
			// Break condition: No hits are returned
			if (response.getHits().getHits().length == 0) {
				break;
			}
		}

	}

	private void writeToVFS(SearchHit sh, FileObject root, String modSecType) {
		String requestedUri = this.getSourceByName(sh, "requestedUri");
		String host = this.getSourceByName(sh, "host");
		String murl;
		String httpMethod = this.getSourceByName(sh, "httpMethod");
		String responsebody = getSourceByName(sh, modSecType);

		murl = Utils.moduloUrl(requestedUri);

		FSManager.putUrlMethodResponseValue(host + "/" + murl, responsebody, root, httpMethod);
		FSManager.close(root);

	}

	private Settings getSettings() {

		if (settings == null) {
			settings = Settings.builder().put("cluster.name", getCluster()).build();
		}
		return settings;
	}

	private String getCluster() {
		return this.cluster;
	}

	private String getType() {
		return this.type;
	}

	private String getParameterSeparator() {
		return this.parameterSeparator;
	}

	@Override
	public void finalize() {
		this.close();
	}
}
