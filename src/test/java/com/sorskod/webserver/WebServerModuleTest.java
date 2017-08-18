package com.sorskod.webserver;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.sorskod.webserver.annotations.DefaultConnector;
import com.sorskod.webserver.annotations.SecureConnector;
import com.sorskod.webserver.connectors.http.HTTPConnectorModule;
import com.sorskod.webserver.connectors.https.HTTPSConnectorModule;
import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * @author Aleksandar Babic
 */
public class WebServerModuleTest {

  Injector injector;

  @Before
  public void initialize() throws Exception {
    injector = Guice.createInjector(new WebServerModule(), new AbstractModule() {
      @Override
      protected void configure() {
        install(new HTTPConnectorModule());
        install(new HTTPSConnectorModule());

        bind(RootResource.class).to(RootResourceImpl.class).asEagerSingleton();
      }

      @DefaultConnector
      @Provides
      Configurator configurator() {
        return new Configurator() {
          @Override
          public int getPort() {
            return 8090;
          }
        };
      }

      @SecureConnector
      @Provides
      Configurator secureConfigurator() {
        return new Configurator() {
          @Override
          public int getPort() {
            return 8091;
          }
        };
      }
    });
  }

  @Test
  public void testWebServerStart() throws Exception {
    injector.getInstance(Server.class).start();
    injector.getInstance(Server.class).stop();
  }


  public static void main(String[] args) throws Exception {
    WebServerModuleTest test = new WebServerModuleTest();

    test.initialize();
    test.injector.getInstance(Server.class).start();
  }


  @Path("/")
  @Produces("application/json")
  public interface RootResource {
    @GET
    Response get();
  }


  /**
   * RootResource implementation
   */
  @Singleton
  public static class RootResourceImpl implements RootResource {
    private final Service service;

    @Inject
    public RootResourceImpl(Service service) {
      this.service = service;
    }

    public Response get() {
      return Response.ok(Collections.singletonMap("message", service.getMessage())).build();
    }
  }

  @Singleton
  public static class Service {

    public String getMessage() {
      return "Hello.";
    }
  }
}