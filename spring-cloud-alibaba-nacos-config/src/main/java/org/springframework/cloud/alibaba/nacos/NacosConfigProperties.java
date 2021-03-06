/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.alibaba.nacos;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

import javax.annotation.PostConstruct;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import static com.alibaba.nacos.api.PropertyKeyConst.ACCESS_KEY;
import static com.alibaba.nacos.api.PropertyKeyConst.CLUSTER_NAME;
import static com.alibaba.nacos.api.PropertyKeyConst.CONTEXT_PATH;
import static com.alibaba.nacos.api.PropertyKeyConst.ENCODE;
import static com.alibaba.nacos.api.PropertyKeyConst.ENDPOINT;
import static com.alibaba.nacos.api.PropertyKeyConst.NAMESPACE;
import static com.alibaba.nacos.api.PropertyKeyConst.SECRET_KEY;
import static com.alibaba.nacos.api.PropertyKeyConst.SERVER_ADDR;

/**
 * nacos properties
 *
 * @author leijuan
 * @author xiaojing
 */
@ConfigurationProperties("spring.cloud.nacos.config")
public class NacosConfigProperties {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NacosConfigProperties.class);

	/**
	 * nacos config server address
	 */
	private String serverAddr;

	/**
	 * encode for nacos config content.
	 */
	private String encode;

	/**
	 * nacos config group, group is config data meta info.
	 */
	private String group = "DEFAULT_GROUP";

	/**
	 * nacos config dataId prefix
	 */
	private String prefix;
	/**
	 * the suffix of nacos config dataId, also the file extension of config content.
	 */
	private String fileExtension = "properties";

	/**
	 * timeout for get config from nacos.
	 */
	private int timeout = 3000;

	/**
	 * endpoint for Nacos, the domain name of a service, through which the server address
	 * can be dynamically obtained.
	 */
	private String endpoint;

	/**
	 * namespace, separation configuration of different environments.
	 */
	private String namespace;

	/**
	 * access key for namespace.
	 */
	private String accessKey;

	/**
	 * secret key for namespace.
	 */
	private String secretKey;

	/**
	 * context path for nacos config server.
	 */
	private String contextPath;

	/**
	 * nacos config cluster name
	 */
	private String clusterName;

	@Value("${spring.application.name}")
	private String name;

	private String[] activeProfiles;

	private ConfigService configService;

	@Autowired
	private Environment environment;

	@PostConstruct
	public void init() {
		this.activeProfiles = environment.getActiveProfiles();
	}

	// todo sts support

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getName() {
		return name;
	}

	public String[] getActiveProfiles() {
		return activeProfiles;
	}

	@Override
	public String toString() {
		return "NacosConfigProperties{" + "serverAddr='" + serverAddr + '\''
				+ ", encode='" + encode + '\'' + ", group='" + group + '\'' + ", prefix='"
				+ prefix + '\'' + ", fileExtension='" + fileExtension + '\''
				+ ", timeout=" + timeout + ", endpoint='" + endpoint + '\''
				+ ", namespace='" + namespace + '\'' + ", accessKey='" + accessKey + '\''
				+ ", secretKey='" + secretKey + '\'' + ", contextPath='" + contextPath
				+ '\'' + ", clusterName='" + clusterName + '\'' + ", name='" + name + '\''
				+ ", activeProfiles=" + Arrays.toString(activeProfiles) + '}';
	}

	public ConfigService configServiceInstance() {

		if (null != configService) {
			return configService;
		}

		Properties properties = new Properties();
		properties.put(SERVER_ADDR, Objects.toString(this.serverAddr, ""));
		properties.put(ENCODE, Objects.toString(this.encode, ""));
		properties.put(NAMESPACE, Objects.toString(this.namespace, ""));
		properties.put(ACCESS_KEY, Objects.toString(this.accessKey, ""));
		properties.put(SECRET_KEY, Objects.toString(this.secretKey, ""));
		properties.put(CONTEXT_PATH, Objects.toString(this.contextPath, ""));
		properties.put(CLUSTER_NAME, Objects.toString(this.clusterName, ""));
		properties.put(ENDPOINT, Objects.toString(this.endpoint, ""));
		try {
			configService = NacosFactory.createConfigService(properties);
			return configService;
		}
		catch (Exception e) {
			LOGGER.error("create config service error!properties={},e=,", this, e);
			return null;
		}
	}
}
