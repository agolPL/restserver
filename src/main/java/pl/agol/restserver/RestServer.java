package pl.agol.restserver;

import static io.undertow.servlet.Servlets.servlet;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;

import java.io.File;

import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener;

/**
 * 
 * @author Andrzej Goławski
 * 
 */
public class RestServer {
	
	private final String host;
	private final int port;
	private final String  contextPath;
	private final String springContextConfigLocation;
	private final PathHandler root;
	
	private Undertow server;
	
	private RestServer(Builder builder) {
		
		this.host = builder.host;
		this.port = builder.port;
		this.contextPath = builder.contextPath;
		this.springContextConfigLocation = builder.springContextConfigLocation;
		this.root = new PathHandler();
	}

	public void start() {
		
		try {
			createAndStartServer();
			DeploymentInfo restDeploymentInfo  = createRestDeploymant();
			deploy(restDeploymentInfo);
		} catch (ServletException e) {
			throw new RestServerException(e);
		}
	}
	
	private void createAndStartServer() {
		
		server = Undertow.builder()
				.addHttpListener(port, host)
				.setHandler(root)
				.build();
		server.start();
	}
	
	private DeploymentInfo createRestDeploymant() {
		
		ServletInfo restServlet = servlet("ResteasyServlet", HttpServletDispatcher.class)
				.addMapping("/*");

		return new DeploymentInfo()
			.setClassLoader(RestServer.class.getClassLoader())
			.setContextPath(contextPath)
			.setDeploymentName("resteasyApplication.war")
			.addServlet(restServlet)
			.addListener(new ListenerInfo(ResteasyBootstrap.class))
			.addListener(new ListenerInfo(SpringContextLoaderListener.class))
			.addInitParameter("contextConfigLocation", springContextConfigLocation)	
			.setResourceManager(new FileResourceManager(new File(""), 0L));
	}
	
	private void deploy(DeploymentInfo restDeploymentInfo) throws ServletException{
		
		ServletContainer container = ServletContainer.Factory.newInstance();
		DeploymentManager manager = container.addDeployment(restDeploymentInfo);
		manager.deploy();
		root.addPrefixPath(restDeploymentInfo.getContextPath(), manager.start());
	}
	
	public void stop() {
		server.stop();
	}
	
	public static Builder builder(){
		return new Builder();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getSpringContextConfigLocation() {
		return springContextConfigLocation;
	}

	public static final class Builder {
		
		private String host = "localhost";
		private int port = 8081;
		private String  contextPath = "/";
		private String springContextConfigLocation = "src/main/resources/applicationContext.xml";
		
		public Builder setHost(String host) {
			this.host = host;
			return this;
		}
		
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}
	
		public Builder setContextPath(String contextPath) {
			this.contextPath = contextPath;
			return this;
		}
	
		public Builder setSpringContextConfigLocation(String springContextConfigLocation) {
			this.springContextConfigLocation = springContextConfigLocation;
			return this;
		}
		
		public RestServer build(){
			return new RestServer(this);
		}
	}
}
